package com.udacity.asteroidradar

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.getDatabase
import retrofit2.HttpException

class RefreshDataWorker (appContext:Context,params:WorkerParameters):CoroutineWorker(appContext,params){
    override suspend fun doWork(): Result {
        val database = getDatabase(applicationContext)
        val repository = DataRepository(database)
        return try {
            repository.deleteYesterday()
            repository.refreshAsteroids()
            repository.refreshPictOfDay()
            Result.success()
        }catch (e:HttpException){
            Result.retry()
        }
    }
    companion object {
        const val WORK_NAME = "RefreshDataWorker"
    }
}
