package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.udacity.asteroidradar.Asteroid

@Dao
interface AsteroidDao {

    @Query("SELECT * FROM asteroid_table ORDER BY closeApproachDate ASC")
    fun getAllAsteroids():LiveData<List<Asteroid>>
    @Query("SELECT * FROM asteroid_table WHERE closeApproachDate <=:date ORDER BY closeApproachDate ASC")
    fun getTodayAsteroids(date:String):LiveData<List<Asteroid>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroid: Asteroid)
    @Query("DELETE FROM asteroid_table WHERE closeApproachDate =:date")
    fun deleteYesterday(date:String)
    @Query("DELETE FROM asteroid_table WHERE closeApproachDate < :date")
    fun deleteNew(date:String)
}