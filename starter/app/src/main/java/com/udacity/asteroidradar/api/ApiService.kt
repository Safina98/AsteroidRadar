package com.udacity.asteroidradar.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Query
import java.text.SimpleDateFormat
import java.util.*

enum class DateFilter(val value: String) { SHOW_TODAY("today"), SHOW_WEEK("week"), SHOW_ALL("all") }

private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .baseUrl(Constants.BASE_URL)
        .build()
private val retrofit2 = Retrofit.Builder()
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .baseUrl(Constants.BASE_URL)
        .build()


interface NeoApiService{
    @GET("neo/rest/v1/feed")
    fun getAsteroids(@Query("start_date") start_date:String,@Query("api_key") type: String): Deferred<String>
}
object ApiNeo{
    val retroitServide : NeoApiService by lazy {
        retrofit2.create(NeoApiService::class.java)
    }
}

interface APODApiService{
    @GET("planetary/apod")
   fun getAPODImage(@Query("api_key")api_key:String) :Deferred<PictureOfDay>
}

object ApiAPOD{
    val retrofitService :APODApiService by lazy {
        retrofit.create(APODApiService::class.java)
    }
}