package org.fernandoblanco.inglesbasico.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "ninos",
    foreignKeys = [
        ForeignKey(
            entity = PadreEntity::class,
            parentColumns = ["id"],
            childColumns = ["padreId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["padreId"])]
)
data class NinoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val padreId: Long,
    val nombreMostrar: String,
    val avatarEmoji: String = "🐱",
    val mascotaId: String = "zorro",
    val puntajeTotal: Int = 0,
    val nivel: Int = 1,
    val partidasImagen: Int = 0,
    val aciertosImagen: Int = 0,
    val partidasAudio: Int = 0,
    val aciertosAudio: Int = 0,
    val partidasPalabras: Int = 0,
    val aciertosPalabras: Int = 0,
    val rachaActual: Int = 0,
    val rachaMaxima: Int = 0,
    val ultimaActividad: Long = 0L,
    val tiempoUsoTotalSegundos: Long = 0L,
    val creadoEn: Long = System.currentTimeMillis(),
    /** R15 — Límite de uso diario en minutos configurado para este perfil. */
    val limiteUsoDiarioMinutos: Int = 40,
    /** R13 / R14 — Actividades habilitadas para el perfil. */
    val actividadImagenHabilitada: Boolean = true,
    val actividadAudioHabilitada: Boolean = true,
    val actividadPalabrasHabilitada: Boolean = true,
    val actividadChatHabilitada: Boolean = true,
    val actividadVocabularioHabilitada: Boolean = true,
    val partidasChat: Int = 0,
    val aciertosChat: Int = 0,
    val sesionesVocabulario: Int = 0,
    val tarjetasVocabulario: Int = 0,
    val segundosUsoHoy: Long = 0L,
    val segundosSesionActual: Long = 0L,
    val ultimoDiaUso: Int = 0
)