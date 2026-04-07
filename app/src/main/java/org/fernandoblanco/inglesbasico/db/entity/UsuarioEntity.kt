package org.fernandoblanco.inglesbasico.db.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "usuarios",
    indices = [Index(value = ["usuario"], unique = true)]
)
data class UsuarioEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val usuario: String,
    val nombreMostrar: String,
    /** URI de imagen de galería (`content://`); null si usa avatar emoji por defecto. */
    val avatarUri: String? = null,
    val hashContrasena: String,
    val sal: String,
    val puntajeTotal: Int = 0,
    val nivel: Int = 1,
    val partidasImagen: Int = 0,
    val aciertosImagen: Int = 0,
    val partidasAudio: Int = 0,
    val aciertosAudio: Int = 0,
    val partidasPalabras: Int = 0,
    val aciertosPalabras: Int = 0,
    val creadoEn: Long = System.currentTimeMillis()
)
