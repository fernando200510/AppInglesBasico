package org.fernandoblanco.inglesbasico.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "padres")
data class PadreEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val usuario: String,
    val nombreMostrar: String,
    val hashContrasena: String,
    val sal: String,
    val creadoEn: Long = System.currentTimeMillis()
)