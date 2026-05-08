package com.example.etbo5ly.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.etbo5ly.data.local.entities.CalendarEntity
import com.example.etbo5ly.data.local.entities.FavoriteEntity

@Database(
    entities = [CalendarEntity::class, FavoriteEntity::class],
    version = 2,
    exportSchema = false
)
abstract class Etbo5lyDataBase: RoomDatabase() {
    abstract fun mealDao(): MealDao

    companion object{
        @Volatile
        private var database: Etbo5lyDataBase? = null

        fun getDataBase(context: Context): Etbo5lyDataBase{
            return database ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    Etbo5lyDataBase::class.java,
                    "etbo5ly_database"
                )
                    .fallbackToDestructiveMigration(false)
                    .build()
                database = instance
                instance
            }
        }
    }

}