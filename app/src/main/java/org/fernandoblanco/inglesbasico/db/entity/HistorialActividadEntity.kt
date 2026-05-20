package org.fernandoblanco.inglesbasico.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "historial_actividades",
    foreignKeys = [
        ForeignKey(
            entity = NinoEntity::class,
            parentColumns = ["id"],
            childColumns = ["ninoId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["ninoId"])]
)
data class HistorialActividadEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val ninoId: Long,
    /** IMAGEN, AUDIO, PALABRAS, CHAT, VOCABULARIO */
    val tipo: String,
    val timestamp: Long = System.currentTimeMillis(),
    val exito: Boolean,
    val detalle: String = ""
)
