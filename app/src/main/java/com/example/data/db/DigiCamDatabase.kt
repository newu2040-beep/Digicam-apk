package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [MediaItemEntity::class, PresetEntity::class], version = 1, exportSchema = false)
abstract class DigiCamDatabase : RoomDatabase() {
    abstract fun mediaDao(): MediaDao
    abstract fun presetDao(): PresetDao

    companion object {
        @Volatile
        private var INSTANCE: DigiCamDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): DigiCamDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DigiCamDatabase::class.java,
                    "digicam_database"
                )
                .addCallback(DigiCamDatabaseCallback(scope))
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }

        private class DigiCamDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateInitialPresets(database.presetDao())
                    }
                }
            }

            suspend fun populateInitialPresets(presetDao: PresetDao) {
                val defaults = listOf(
                    PresetEntity(name = "Classic Film 400", category = "Film", filterName = "Classic Film", intensity = 0.85f, grain = 0.25f, fade = 0.12f, temperature = 0.05f, contrast = 0.15f, isCustom = false),
                    PresetEntity(name = "Vintage warm '98", category = "Retro", filterName = "Vintage Film", intensity = 0.90f, grain = 0.35f, fade = 0.20f, temperature = 0.18f, contrast = -0.05f, isCustom = false),
                    PresetEntity(name = "Golden Hour Dream", category = "Mood", filterName = "Golden Hour", intensity = 0.80f, grain = 0.15f, fade = 0.08f, temperature = 0.25f, tint = 0.08f, isCustom = false),
                    PresetEntity(name = "Soft Portrait Bloom", category = "Portrait", filterName = "Soft Glow", intensity = 0.70f, grain = 0.05f, fade = 0.10f, highlights = -0.15f, isCustom = false),
                    PresetEntity(name = "B&W Monochrome Noir", category = "B&W", filterName = "Monochrome", intensity = 1.0f, grain = 0.30f, contrast = 0.25f, temperature = 0.0f, isCustom = false),
                    PresetEntity(name = "Sakura Pastel Pink", category = "Retro", filterName = "Pastel", intensity = 0.88f, grain = 0.10f, fade = 0.15f, temperature = 0.08f, tint = 0.18f, isCustom = false),
                    PresetEntity(name = "Cinematic Moody Teal", category = "Mood", filterName = "Cinematic", intensity = 0.82f, grain = 0.20f, fade = 0.05f, temperature = -0.15f, tint = -0.10f, isCustom = false),
                    PresetEntity(name = "Disposable 35mm", category = "Film", filterName = "Disposable Camera", intensity = 0.95f, grain = 0.40f, fade = 0.18f, temperature = 0.12f, contrast = 0.20f, isCustom = false)
                )
                defaults.forEach { presetDao.insertPreset(it) }
            }
        }
    }
}
