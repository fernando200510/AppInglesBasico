package org.fernandoblanco.inglesbasico.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.fernandoblanco.inglesbasico.db.dao.NinoDao
import org.fernandoblanco.inglesbasico.db.dao.PadreDao
import org.fernandoblanco.inglesbasico.db.entity.NinoEntity
import org.fernandoblanco.inglesbasico.db.entity.PadreEntity

@Database(
    entities = [PadreEntity::class, NinoEntity::class],
    version = 2,
    exportSchema = false
)
abstract class InglesDatabase : RoomDatabase() {

    abstract fun padreDao(): PadreDao
    abstract fun ninoDao(): NinoDao

    companion object {
        private const val NOMBRE_DB = "ingles_basico_v2.db"

        @Volatile
        private var instancia: InglesDatabase? = null

        fun obtener(context: Context): InglesDatabase {
            return instancia ?: synchronized(this) {
                instancia ?: Room.databaseBuilder(
                    context.applicationContext,
                    InglesDatabase::class.java,
                    NOMBRE_DB
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { instancia = it }
            }
        }
    }
}