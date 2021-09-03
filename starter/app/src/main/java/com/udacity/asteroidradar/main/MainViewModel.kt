package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.Constants.API_KEY
import com.udacity.asteroidradar.DataRepository
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.*
import com.udacity.asteroidradar.database.getDatabase
import kotlinx.android.synthetic.main.fragment_detail.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.log

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val _asteroids = MutableLiveData<List<Asteroid>>()
   //val asteroids :LiveData<List<Asteroid>> get() =  _asteroids
    private val _selected_item = MutableLiveData<Asteroid>()
    val selected_item:LiveData<Asteroid> get() = _selected_item

    private val filter_ =  MutableLiveData<DateFilter>(DateFilter.SHOW_ALL)


    private val _pict_of_d_day = MutableLiveData<PictureOfDay>()
    //val pict_of_d_day:LiveData<PictureOfDay>get() = _pict_of_d_day

    private val database= getDatabase(application)
    private val asteroidRepository = DataRepository(database)
    var asteroids:LiveData<List<Asteroid>> = Transformations.switchMap(filter_){filter->
        asteroidRepository.updateFilter(filter)
    }
    //var asteroids = asteroidRepository.asteroids
    val pict_of_d_day = asteroidRepository.pictOfDay

    init {
        viewModelScope.launch {
           asteroidRepository.refreshPictOfDay()
            asteroidRepository.refreshAsteroids()
        }
        //getAsteroids()
        //getPictOfDDay()
    }



    fun updateFilter(filter:DateFilter){
        filter_.value = filter
    }
/*
    fun getPictOfDDay(){
        viewModelScope.launch {
            try {
               // _pict_of_d_day.value =ApiAPOD.retrofitService.getAPODImage("gZt6MUlSAOaMlGIPiHVJYPorEwvu3fmKNK3lkDKI")

            }catch (e:Exception){
                Log.e("APODInfo","err: $e")

            }
        }
    }

    fun getAsteroids(){
        viewModelScope.launch {
            val sdf = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT)
            val currentDate = sdf.format(Date())
            try {
               val jsonObj = ApiNeo.retroitServide.getAsteroids(currentDate,API_KEY)
              //  _asteroids.value = parseAsteroidsJsonResult(JSONObject(jsonObj))
            }catch (e:Exception){
                Log.e("NeoInfo","err: $e")
            }
        }
    }
 */

    fun navigateToDetail(asteroid: Asteroid){
        _selected_item.value = asteroid
    }
    fun navigatedToDetail(){
        _selected_item.value = null
    }
    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }

}