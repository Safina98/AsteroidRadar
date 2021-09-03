package com.udacity.asteroidradar

import android.util.Log
import androidx.lifecycle.LiveData
import com.udacity.asteroidradar.Constants.API_KEY
import com.udacity.asteroidradar.api.ApiAPOD
import com.udacity.asteroidradar.api.ApiNeo
import com.udacity.asteroidradar.api.DateFilter
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.UnknownHostException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class DataRepository (private val database: AsteroidDatabase){
  //  var asteroids = database.asteroidDao.getAllAsteroids()
    val pictOfDay = database.pictODDayDao.getPict()
    val sdf = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT)
    val currentDate = sdf.format(Date())

    val dateFormat :DateFormat= SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT)
    val cal :Calendar= Calendar.getInstance()

    val day = cal.get(Calendar.DAY_OF_WEEK)
    val s = cal.add(Calendar.DATE,7-day)
    val d = dateFormat.format(cal.time)
    fun updateFilter(filter:DateFilter):LiveData<List<Asteroid>>{
        return when(filter){
            DateFilter.SHOW_TODAY ->database.asteroidDao.getTodayAsteroids(currentDate)
            DateFilter.SHOW_WEEK -> database.asteroidDao.getTodayAsteroids(d)//show asteroids from today till saturday
            else -> database.asteroidDao.getAllAsteroids()
        }
    }
    suspend fun refreshAsteroids(){
        withContext(Dispatchers.IO){
            try {
                val jsonObj =ApiNeo.retroitServide.getAsteroids(currentDate,API_KEY).await()
                val asteroids = parseAsteroidsJsonResult(JSONObject(jsonObj))
                database.asteroidDao.insertAll(*asteroids.toTypedArray())
            }catch (ex:UnknownHostException){
                Log.e("asteroid","error: "+ex.message)
            }
        }
    }
    suspend fun refreshPictOfDay() {
        withContext(Dispatchers.IO){
            try {
                val pict = ApiAPOD.retrofitService.getAPODImage(API_KEY).await()
                database.pictODDayDao.insert(pict)
            }catch (e:UnknownHostException){
                Log.e("asteroid","error: "+e.message)
            }
        }
    }
    suspend fun deleteYesterday(){
        withContext(Dispatchers.IO){
            database.pictODDayDao.deleteAll()
            database.asteroidDao.deleteNew(currentDate)
        }
    }
}

