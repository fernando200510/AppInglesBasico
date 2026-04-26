package org.fernandoblanco.inglesbasico.db.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "padres",
    indices = [Index(value = ["usuario"], unique = true)]
)
data class PadreEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val usuario: String,
    val nombreMostrar: String,
    val hashContrasena: String,
    val sal: String,
    val creadoEn: Long = System.currentTimeMillis()
)