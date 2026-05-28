package com.fic.dualhabit10.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [RegistroAguaEntity::class, RegistroAguaMascotaEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun hidratacionDao(): HidratacionDao
    abstract fun hidratacionMascotaDao(): HidratacionMascotaDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getDatebase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "dual_habit_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
