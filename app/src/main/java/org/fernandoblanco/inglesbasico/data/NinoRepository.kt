package org.fernandoblanco.inglesbasico.data

import kotlinx.coroutines.flow.Flow
import org.fernandoblanco.inglesbasico.db.dao.NinoDao
import org.fernandoblanco.inglesbasico.db.entity.NinoEntity

class NinoRepository(
    private val dao: NinoDao,
    private val sesion: SesionUsuario
) {

    fun observarNinosDePadre(padreId: Long): Flow<List<NinoEntity>> = dao.observarPorPadre(padreId)

    fun observarNino(id: Long): Flow<NinoEntity?> = dao.observarPorId(id)

    suspend fun obtenerPorId(id: Long): NinoEntity? = dao.obtenerPorId(id)

    suspend fun crearNino(nombre: String, emoji: String): Result<Long> {
        val padreId = sesion.padreIdActivo ?: return Result.failure(Exception("No hay sesión activa"))
        val nombreLimpio = nombre.trim()

        if (dao.existeNombreParaPadre(padreId, nombreLimpio)) {
            return Result.failure(Exception("Ya tienes un perfil con el nombre '$nombreLimpio'"))
        }

        val nuevoNino = NinoEntity(
            padreId = padreId,
            nombreMostrar = nombreLimpio,
            avatarEmoji = emoji
        )

        return try {
            val id = dao.insertar(nuevoNino)
            Result.success(id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun actualizarNombre(id: Long, nuevoNombre: String): Result<Unit> {
        val actual = dao.obtenerPorId(id) ?: return Result.failure(Exception("No encontrado"))
        val nombreLimpio = nuevoNombre.trim()

        if (dao.existeNombreParaPadreExcluyendo(actual.padreId, nombreLimpio, id)) {
            return Result.failure(Exception("Ya tienes otro perfil con el nombre '$nombreLimpio'"))
        }

        dao.actualizar(actual.copy(nombreMostrar = nombreLimpio))
        return Result.success(Unit)
    }

    suspend fun actualizarAvatar(id: Long, nuevoEmoji: String): Result<Unit> {
        val actual = dao.obtenerPorId(id) ?: return Result.failure(Exception("No encontrado"))
        dao.actualizar(actual.copy(avatarEmoji = nuevoEmoji))
        return Result.success(Unit)
    }

    suspend fun actualizarMascota(id: Long, mascotaId: String): Result<Unit> {
        val actual = dao.obtenerPorId(id) ?: return Result.failure(Exception("No encontrado"))
        dao.actualizar(actual.copy(mascotaId = mascotaId))
        return Result.success(Unit)
    }

    suspend fun eliminarNino(nino: NinoEntity): Result<Unit> {
        return try {
            dao.eliminar(nino)
            if (sesion.ninoIdActivo == nino.id) {
                sesion.ninoIdActivo = null
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun registrarResultadoActividad(
        ninoId: Long,
        tipo: TipoActividad,
        correcto: Boolean,
        puntosSiCorrecto: Int = 10
    ) {
        val nino = dao.obtenerPorId(ninoId) ?: return
        val puntosExtra = if (correcto) puntosSiCorrecto else 0

        val ninoActualizado = when (tipo) {
            TipoActividad.IMAGEN -> nino.copy(
                partidasImagen = nino.partidasImagen + 1,
                aciertosImagen = if (correcto) nino.aciertosImagen + 1 else nino.aciertosImagen
            )
            TipoActividad.AUDIO -> nino.copy(
                partidasAudio = nino.partidasAudio + 1,
                aciertosAudio = if (correcto) nino.aciertosAudio + 1 else nino.aciertosAudio
            )
            TipoActividad.PALABRAS -> nino.copy(
                partidasPalabras = nino.partidasPalabras + 1,
                aciertosPalabras = if (correcto) nino.aciertosPalabras + 1 else nino.aciertosPalabras
            )
        }.let {
            val nuevoPuntaje = it.puntajeTotal + puntosExtra
            it.copy(
                puntajeTotal = nuevoPuntaje,
                nivel = (nuevoPuntaje / 50) + 1
            )
        }
        dao.actualizar(ninoActualizado)
    }

    suspend fun actualizarRacha(ninoId: Long): Int {
        val u = dao.obtenerPorId(ninoId) ?: return 0
        val ahora = System.currentTimeMillis()
        val unDia = 24 * 60 * 60 * 1000L
        val dosDias = 2 * unDia
        val nueva = when {
            u.ultimaActividad == 0L -> 1
            ahora - u.ultimaActividad < unDia -> u.rachaActual
            ahora - u.ultimaActividad < dosDias -> u.rachaActual + 1
            else -> 1
        }
        val nuevaMax = maxOf(u.rachaMaxima, nueva)
        dao.actualizar(u.copy(rachaActual = nueva, rachaMaxima = nuevaMax, ultimaActividad = ahora))
        return nueva
    }

    enum class TipoActividad { IMAGEN, AUDIO, PALABRAS }
}