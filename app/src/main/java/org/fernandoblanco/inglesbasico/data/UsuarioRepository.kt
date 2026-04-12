package org.fernandoblanco.inglesbasico.data

import kotlinx.coroutines.flow.Flow
import org.fernandoblanco.inglesbasico.db.dao.UsuarioDao
import org.fernandoblanco.inglesbasico.db.entity.UsuarioEntity
import org.fernandoblanco.inglesbasico.security.PasswordHasher

class UsuarioRepository(
    private val dao: UsuarioDao,
    private val sesion: SesionUsuario
) {

    fun observarUsuarioActual(id: Long): Flow<UsuarioEntity?> = dao.observarPorId(id)

    suspend fun registrar(usuario: String, nombreMostrar: String, contrasena: String): Result<Long> {
        val u = usuario.trim()
        if (u.length < 3) return Result.failure(IllegalArgumentException("Usuario muy corto"))
        if (contrasena.length < 4) return Result.failure(IllegalArgumentException("Contraseña muy corta"))
        if (dao.obtenerPorNombreUsuario(u) != null) {
            return Result.failure(IllegalArgumentException("Ese usuario ya existe"))
        }
        val sal = PasswordHasher.generarSal()
        val hash = PasswordHasher.hash(contrasena, sal)
        val entidad = UsuarioEntity(
            usuario = u,
            nombreMostrar = nombreMostrar.trim().ifEmpty { u },
            avatarUri = null,
            hashContrasena = hash,
            sal = sal
        )
        val id = dao.insertar(entidad)
        return Result.success(id)
    }

    suspend fun iniciarSesion(usuario: String, contrasena: String): Result<Long> {
        val u = dao.obtenerPorNombreUsuario(usuario.trim())
            ?: return Result.failure(IllegalArgumentException("Usuario o contraseña incorrectos"))
        if (!PasswordHasher.coincide(contrasena, u.sal, u.hashContrasena)) {
            return Result.failure(IllegalArgumentException("Usuario o contraseña incorrectos"))
        }
        sesion.usuarioIdActivo = u.id
        return Result.success(u.id)
    }

    suspend fun obtenerPorId(id: Long): UsuarioEntity? = dao.obtenerPorId(id)

    suspend fun actualizarPerfil(id: Long, nombreMostrar: String, nuevaContrasena: String?): Result<Unit> {
        val actual = dao.obtenerPorId(id) ?: return Result.failure(IllegalStateException("Usuario no encontrado"))
        val nombre = nombreMostrar.trim().ifEmpty { actual.usuario }
        val actualizado = if (nuevaContrasena.isNullOrBlank()) {
            actual.copy(nombreMostrar = nombre)
        } else {
            if (nuevaContrasena.length < 4) {
                return Result.failure(IllegalArgumentException("Contraseña muy corta"))
            }
            val sal = PasswordHasher.generarSal()
            actual.copy(
                nombreMostrar = nombre,
                sal = sal,
                hashContrasena = PasswordHasher.hash(nuevaContrasena, sal)
            )
        }
        dao.actualizar(actualizado)
        return Result.success(Unit)
    }

    suspend fun actualizarAvatarUri(id: Long, uri: String?): Result<Unit> {
        val actual = dao.obtenerPorId(id) ?: return Result.failure(IllegalStateException("Usuario no encontrado"))
        dao.actualizar(actual.copy(avatarUri = uri))
        return Result.success(Unit)
    }

    suspend fun eliminarPerfil(id: Long): Result<Unit> {
        val actual = dao.obtenerPorId(id) ?: return Result.failure(IllegalStateException("Usuario no encontrado"))
        dao.eliminar(actual)
        if (sesion.usuarioIdActivo == id) sesion.cerrarSesion()
        return Result.success(Unit)
    }

    suspend fun registrarResultadoActividad(
        usuarioId: Long,
        tipo: TipoActividad,
        correcto: Boolean,
        puntosSiCorrecto: Int = 10
    ) {
        val u = dao.obtenerPorId(usuarioId) ?: return
        val puntosExtra = if (correcto) puntosSiCorrecto else 0
        var pi = u.partidasImagen
        var ai = u.aciertosImagen
        var pa = u.partidasAudio
        var aa = u.aciertosAudio
        var pp = u.partidasPalabras
        var ap = u.aciertosPalabras
        when (tipo) {
            TipoActividad.IMAGEN -> {
                pi += 1
                if (correcto) ai += 1
            }
            TipoActividad.AUDIO -> {
                pa += 1
                if (correcto) aa += 1
            }
            TipoActividad.PALABRAS -> {
                pp += 1
                if (correcto) ap += 1
            }
        }
        val nuevoPuntaje = u.puntajeTotal + puntosExtra
        val nuevoNivel = calcularNivel(nuevoPuntaje)
        dao.actualizar(
            u.copy(
                puntajeTotal = nuevoPuntaje,
                nivel = nuevoNivel,
                partidasImagen = pi,
                aciertosImagen = ai,
                partidasAudio = pa,
                aciertosAudio = aa,
                partidasPalabras = pp,
                aciertosPalabras = ap
            )
        )
    }

    private fun calcularNivel(puntajeTotal: Int): Int {
        return (puntajeTotal / 50).coerceAtLeast(0) + 1
    }

    enum class TipoActividad { IMAGEN, AUDIO, PALABRAS }
    suspend fun actualizarRacha(usuarioId: Long): Int {
        val u = dao.obtenerPorId(usuarioId) ?: return 0
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

    suspend fun guardarMascota(usuarioId: Long, mascotaId: String): Result<Unit> {
        val u = dao.obtenerPorId(usuarioId) ?: return Result.failure(IllegalStateException("Usuario no encontrado"))
        dao.actualizar(u.copy(mascotaId = mascotaId))
        return Result.success(Unit)
    }
}
