package org.fernandoblanco.inglesbasico.data

import kotlinx.coroutines.flow.Flow
import org.fernandoblanco.inglesbasico.db.dao.HistorialDao
import org.fernandoblanco.inglesbasico.db.dao.NinoDao
import org.fernandoblanco.inglesbasico.db.dao.UsoDiarioDao
import org.fernandoblanco.inglesbasico.db.entity.HistorialActividadEntity
import org.fernandoblanco.inglesbasico.db.entity.NinoEntity
import org.fernandoblanco.inglesbasico.db.entity.UsoDiarioEntity

class NinoRepository(
    private val dao: NinoDao,
    private val historialDao: HistorialDao,
    private val usoDiarioDao: UsoDiarioDao,
    private val sesion: SesionUsuario
) {

    fun observarNinosDePadre(padreId: Long): Flow<List<NinoEntity>> = dao.observarPorPadre(padreId)

    fun observarNino(id: Long): Flow<NinoEntity?> = dao.observarPorId(id)

    fun observarHistorial(ninoId: Long, limite: Int = 80): Flow<List<HistorialActividadEntity>> =
        historialDao.observarPorNino(ninoId, limite)

    fun observarUsoDiario(ninoId: Long, limite: Int = 14): Flow<List<UsoDiarioEntity>> =
        usoDiarioDao.observarRecientes(ninoId, limite)

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
            if (sesion.ninoIdActivo == nino.id) {
                flushTiempoSesionActiva()
            }
            dao.eliminar(nino)
            if (sesion.ninoIdActivo == nino.id) {
                sesion.ninoIdActivo = null
                sesion.detenerMonitoreo()
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** Nueva sesión de juego: reinicia contador de sesión y arranca el cronómetro real. */
    suspend fun iniciarSesionNino(ninoId: Long) {
        val nino = dao.obtenerPorId(ninoId) ?: return
        dao.actualizar(nino.copy(segundosSesionActual = 0L))
        sesion.marcarInicioMonitoreo(ninoId)
    }

    /** Tras reiniciar la app con perfil activo: no borra la sesión en curso. */
    suspend fun reanudarMonitoreoTiempo(ninoId: Long) {
        sesion.reanudarMonitoreo(ninoId)
    }

    /** Guarda el tiempo transcurrido desde el último checkpoint (p. ej. al salir o pausar). */
    suspend fun flushTiempoSesionActiva() {
        val ninoId = sesion.ninoTiempoActivo ?: return
        val segundos = sesion.segundosDesdeCheckpoint()
        if (segundos < 1L) return
        sesion.consumirCheckpoint()
        val nino = dao.obtenerPorId(ninoId) ?: return
        val actualizado = aplicarTiempoUso(nino, segundos)
        dao.actualizar(actualizado)
    }

    suspend fun finalizarSesionNino() {
        flushTiempoSesionActiva()
        sesion.detenerMonitoreo()
    }

    suspend fun registrarResultadoActividad(
        ninoId: Long,
        tipo: TipoActividad,
        correcto: Boolean,
        puntosSiCorrecto: Int = 10
    ) {
        flushTiempoSesionActiva()
        val nino = dao.obtenerPorId(ninoId) ?: return
        val puntosExtra = if (correcto) puntosSiCorrecto else 0
        val ahora = System.currentTimeMillis()

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
            TipoActividad.CHAT -> nino.copy(
                partidasChat = nino.partidasChat + 1,
                aciertosChat = if (correcto) nino.aciertosChat + 1 else nino.aciertosChat
            )
            TipoActividad.VOCABULARIO -> nino
        }.let {
            val nuevoPuntaje = it.puntajeTotal + puntosExtra
            it.copy(
                puntajeTotal = nuevoPuntaje,
                nivel = (nuevoPuntaje / 50) + 1,
                ultimaActividad = ahora
            )
        }

        dao.actualizar(ninoActualizado)

        val titulo = tipo.tituloHistorial
        historialDao.insertar(
            HistorialActividadEntity(
                ninoId = ninoId,
                tipo = tipo.name,
                timestamp = ahora,
                exito = correcto,
                detalle = if (correcto) "$titulo · Acierto (+$puntosExtra pts)" else "$titulo · Intento"
            )
        )
    }

    suspend fun registrarSesionVocabulario(ninoId: Long, tarjetasCompletadas: Int) {
        flushTiempoSesionActiva()
        val nino = dao.obtenerPorId(ninoId) ?: return
        val ahora = System.currentTimeMillis()
        val actualizado = nino.copy(
            sesionesVocabulario = nino.sesionesVocabulario + 1,
            tarjetasVocabulario = nino.tarjetasVocabulario + tarjetasCompletadas,
            ultimaActividad = ahora
        )
        dao.actualizar(actualizado)
        historialDao.insertar(
            HistorialActividadEntity(
                ninoId = ninoId,
                tipo = TipoActividad.VOCABULARIO.name,
                timestamp = ahora,
                exito = true,
                detalle = "Aprende palabras · $tarjetasCompletadas tarjetas estudiadas"
            )
        )
    }

    private suspend fun aplicarTiempoUso(nino: NinoEntity, segundosAgregar: Long): NinoEntity {
        if (segundosAgregar < 1L) return nino
        val diaHoy = diaClaveActual()
        val mismoDia = nino.ultimoDiaUso == diaHoy
        val segundosHoy = if (mismoDia) nino.segundosUsoHoy + segundosAgregar else segundosAgregar
        val registroPrevio = usoDiarioDao.obtener(nino.id, diaHoy)
        usoDiarioDao.guardar(
            UsoDiarioEntity(
                ninoId = nino.id,
                diaClave = diaHoy,
                segundos = (registroPrevio?.segundos ?: 0L) + segundosAgregar
            )
        )
        return nino.copy(
            tiempoUsoTotalSegundos = nino.tiempoUsoTotalSegundos + segundosAgregar,
            segundosUsoHoy = segundosHoy,
            segundosSesionActual = nino.segundosSesionActual + segundosAgregar,
            ultimoDiaUso = diaHoy
        )
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

    enum class TipoActividad(val tituloHistorial: String) {
        IMAGEN("Elegir imagen correcta"),
        AUDIO("Escuchar audio y responder"),
        PALABRAS("Completar palabras"),
        CHAT("Conversar con la IA"),
        VOCABULARIO("Aprende palabras")
    }
}
