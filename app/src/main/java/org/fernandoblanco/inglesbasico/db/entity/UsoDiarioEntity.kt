package org.fernandoblanco.inglesbasico.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "uso_diario",
    primaryKeys = ["ninoId", "diaClave"],
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
data class UsoDiarioEntity(
    val ninoId: Long,
    /** Día del año + año×1000, mismo formato que [NinoEntity.ultimoDiaUso]. */
    val diaClave: Int,
    val segundos: Long = 0L
)
