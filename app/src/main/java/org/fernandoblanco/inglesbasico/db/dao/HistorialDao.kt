package org.fernandoblanco.inglesbasico.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.fernandoblanco.inglesbasico.db.entity.HistorialActividadEntity

@Dao
interface HistorialDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertar(evento: HistorialActividadEntity)

    @Query("SELECT * FROM historial_actividades WHERE ninoId = :ninoId ORDER BY timestamp DESC LIMIT :limite")
    fun observarPorNino(ninoId: Long, limite: Int = 80): Flow<List<HistorialActividadEntity>>

    @Query("SELECT * FROM historial_actividades WHERE ninoId = :ninoId ORDER BY timestamp DESC LIMIT :limite")
    suspend fun listarPorNino(ninoId: Long, limite: Int = 80): List<HistorialActividadEntity>
}
