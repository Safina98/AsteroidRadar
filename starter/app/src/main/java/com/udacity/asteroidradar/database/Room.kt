package com.udacity.asteroidradar.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay

@Database(entities = [Asteroid::class,PictureOfDay::class],version = 1)
abstract class AsteroidDatabase:RoomDatabase (){
    abstract val asteroidDao:AsteroidDao
    abstract val pictODDayDao:PictODDayDao

}

private lateinit var INSTANCE: AsteroidDatabase
fun getDatabase(context: Context): AsteroidDatabase {
    synchronized(AsteroidDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                    AsteroidDatabase::class.java,
                    "asteroids").fallbackToDestructiveMigration().build()
        }
    }
    return INSTANCE
}