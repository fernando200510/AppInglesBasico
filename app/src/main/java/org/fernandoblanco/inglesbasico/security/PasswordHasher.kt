package org.fernandoblanco.inglesbasico.security

import java.security.MessageDigest
import java.security.SecureRandom

object PasswordHasher {

    private val aleatorioSeguro = SecureRandom()

    fun generarSal(): String {
        val bytes = ByteArray(16)
        aleatorioSeguro.nextBytes(bytes)
        return bytes.joinToString("") { "%02x".format(it) }
    }

    fun hash(contrasena: String, sal: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val primero = digest.digest((sal + contrasena).toByteArray(Charsets.UTF_8))
        digest.reset()
        val segundo = digest.digest(primero)
        return segundo.joinToString("") { "%02x".format(it) }
    }

    fun coincide(contrasenaPlano: String, sal: String, hashAlmacenado: String): Boolean {
        return hash(contrasenaPlano, sal) == hashAlmacenado
    }
}
