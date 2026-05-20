package org.fernandoblanco.inglesbasico.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.fernandoblanco.inglesbasico.db.entity.UsoDiarioEntity

@Dao
interface UsoDiarioDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardar(registro: UsoDiarioEntity)

    @Query("SELECT * FROM uso_diario WHERE ninoId = :ninoId AND diaClave = :diaClave LIMIT 1")
    suspend fun obtener(ninoId: Long, diaClave: Int): UsoDiarioEntity?

    @Query(
        """
        SELECT * FROM uso_diario
        WHERE ninoId = :ninoId
        ORDER BY diaClave DESC
        LIMIT :limite
        """
    )
    fun observarRecientes(ninoId: Long, limite: Int = 14): Flow<List<UsoDiarioEntity>>
}
