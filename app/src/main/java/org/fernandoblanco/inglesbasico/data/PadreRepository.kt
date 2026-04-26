package org.fernandoblanco.inglesbasico.data

import org.fernandoblanco.inglesbasico.db.dao.PadreDao
import org.fernandoblanco.inglesbasico.db.entity.PadreEntity
import org.fernandoblanco.inglesbasico.security.PasswordHasher

class PadreRepository(
    private val dao: PadreDao,
    private val sesion: SesionUsuario
) {

    suspend fun registrar(
        usuario: String,
        nombreMostrar: String,
        contrasena: String
    ): Result<Long> {
        val u = usuario.trim()
        if (u.length < 3) return Result.failure(IllegalArgumentException("Usuario muy corto"))
        if (contrasena.length < 4) return Result.failure(IllegalArgumentException("Contraseña muy corta"))
        if (dao.obtenerPorUsuario(u) != null) {
            return Result.failure(IllegalArgumentException("Ese usuario ya existe"))
        }
        val sal = PasswordHasher.generarSal()
        val hash = PasswordHasher.hash(contrasena, sal)
        val entidad = PadreEntity(
            usuario = u,
            nombreMostrar = nombreMostrar.trim().ifEmpty { u },
            hashContrasena = hash,
            sal = sal
        )
        val id = dao.insertar(entidad)
        sesion.padreIdActivo = id
        return Result.success(id)
    }

    suspend fun iniciarSesion(usuario: String, contrasena: String): Result<Long> {
        val p = dao.obtenerPorUsuario(usuario.trim())
            ?: return Result.failure(IllegalArgumentException("Usuario o contraseña incorrectos"))
        if (!PasswordHasher.coincide(contrasena, p.sal, p.hashContrasena)) {
            return Result.failure(IllegalArgumentException("Usuario o contraseña incorrectos"))
        }
        sesion.padreIdActivo = p.id
        return Result.success(p.id)
    }

    suspend fun verificarContrasena(padreId: Long, contrasena: String): Boolean {
        val p = dao.obtenerPorId(padreId) ?: return false
        return PasswordHasher.coincide(contrasena, p.sal, p.hashContrasena)
    }

    suspend fun obtenerPorId(id: Long): PadreEntity? = dao.obtenerPorId(id)
}