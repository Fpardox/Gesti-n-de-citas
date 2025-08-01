package com.example.miprimeraapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// AÑADIMOS Appointment::class A LA LISTA DE ENTIDADES Y SUBIMOS LA VERSIÓN A 2
@Database(entities = [Client::class, Appointment::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun clientDao(): ClientDao
    // AÑADIMOS EL NUEVO DAO
    abstract fun appointmentDao(): AppointmentDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    // AÑADIMOS ESTA LÍNEA PARA MANEJAR LA MIGRACIÓN DE VERSIÓN
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}