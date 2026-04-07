package org.fernandoblanco.inglesbasico.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.fernandoblanco.inglesbasico.db.dao.UsuarioDao
import org.fernandoblanco.inglesbasico.db.entity.UsuarioEntity

@Database(
    entities = [UsuarioEntity::class],
    version = 1,
    exportSchema = false
)
abstract class InglesDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao

    companion object {
        private const val NOMBRE_DB = "ingles_basico.db"

        @Volatile
        private var instancia: InglesDatabase? = null

        fun obtener(context: Context): InglesDatabase {
            return instancia ?: synchronized(this) {
                instancia ?: Room.databaseBuilder(
                    context.applicationContext,
                    InglesDatabase::class.java,
                    NOMBRE_DB
                ).build().also { instancia = it }
            }
        }
    }
}
