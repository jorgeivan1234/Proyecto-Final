package com.fic.dualhabit10.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [
        RegistroAguaEntity::class,
        RegistroAguaMascotaEntity::class,
        ActividadFisicaEntity::class,
        PerfilMascotaEntity::class,
        AlimentacionEntity::class,
        SuenoEntity::class,
        SugerenciaEntity::class,
        AlimentacionMascotaEntity::class,
        PaseoEntity::class
    ],
    version = 8,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun hidratacionDao(): HidratacionDao
    abstract fun hidratacionMascotaDao(): HidratacionMascotaDao
    abstract fun actividadFisicaDao(): ActividadFisicaDao
    abstract fun perfilMacotaDao(): PerfilMascotaDao
    abstract fun alimentacionDao(): AlimentacionDao
    abstract fun suenoDao(): SuenoDao
    abstract fun sugerenciaDao(): SugerenciaDao
    abstract fun alimentacionMascotaDao(): AlimentacionMascotaDao
    abstract fun paseoDao(): PaseoDao



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
