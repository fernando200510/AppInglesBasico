package org.fernandoblanco.inglesbasico.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import org.fernandoblanco.inglesbasico.db.dao.UsuarioDao
import org.fernandoblanco.inglesbasico.db.entity.UsuarioEntity

@Database(
    entities = [UsuarioEntity::class],
    version = 2,
    exportSchema = false
)
abstract class InglesDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao

    companion object {
        private const val NOMBRE_DB = "ingles_basico.db"

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE usuarios ADD COLUMN avatarUri TEXT")
            }
        }

        @Volatile
        private var instancia: InglesDatabase? = null

        fun obtener(context: Context): InglesDatabase {
            return instancia ?: synchronized(this) {
                instancia ?: Room.databaseBuilder(
                    context.applicationContext,
                    InglesDatabase::class.java,
                    NOMBRE_DB
                ).addMigrations(MIGRATION_1_2)
                    .build().also { instancia = it }
            }
        }
    }
}
