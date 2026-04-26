package org.fernandoblanco.inglesbasico.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import org.fernandoblanco.inglesbasico.db.entity.NinoEntity

@Dao
interface NinoDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertar(nino: NinoEntity): Long

    @Update
    suspend fun actualizar(nino: NinoEntity)

    @Delete
    suspend fun eliminar(nino: NinoEntity)

    @Query("SELECT * FROM ninos WHERE padreId = :padreId ORDER BY creadoEn ASC")
    suspend fun obtenerPorPadre(padreId: Long): List<NinoEntity>

    @Query("SELECT * FROM ninos WHERE padreId = :padreId ORDER BY creadoEn ASC")
    fun observarPorPadre(padreId: Long): Flow<List<NinoEntity>>

    @Query("SELECT * FROM ninos WHERE id = :id LIMIT 1")
    suspend fun obtenerPorId(id: Long): NinoEntity?

    @Query("SELECT * FROM ninos WHERE id = :id LIMIT 1")
    fun observarPorId(id: Long): Flow<NinoEntity?>
}