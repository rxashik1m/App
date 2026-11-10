package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [BuildJobEntity::class], version = 1, exportSchema = false)
abstract class AppForgeDatabase : RoomDatabase() {
    abstract fun buildJobDao(): BuildJobDao

    companion object {
        @Volatile
        private var INSTANCE: AppForgeDatabase? = null

        fun getDatabase(context: Context): AppForgeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppForgeDatabase::class.java,
                    "appforge_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
