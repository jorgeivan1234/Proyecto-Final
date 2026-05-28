package com.fic.dualhabit10.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.fic.dualhabit10.data.local.HidratacionDao
import com.fic.dualhabit10.data.local.RegistroAguaEntity

@Database(entities = [RegistroAguaEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun hidratacionDao(): HidratacionDao
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getDatebase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "dual-habit_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
