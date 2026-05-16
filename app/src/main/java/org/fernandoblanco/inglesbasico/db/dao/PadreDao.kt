package org.fernandoblanco.inglesbasico.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import org.fernandoblanco.inglesbasico.db.entity.PadreEntity

@Dao
interface PadreDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertar(padre: PadreEntity): Long

    @Update
    suspend fun actualizar(padre: PadreEntity)

    @Delete
    suspend fun eliminar(padre: PadreEntity)

    @Query("SELECT * FROM padres WHERE usuario = :usuario LIMIT 1")
    suspend fun obtenerPorUsuario(usuario: String): PadreEntity?

    @Query("SELECT * FROM padres WHERE id = :id LIMIT 1")
    suspend fun obtenerPorId(id: Long): PadreEntity?
}