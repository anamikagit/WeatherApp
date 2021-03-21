package com.example.weatherapp.cache

import android.content.Context
import com.example.weatherapp.entity.WeatherEntity

class DatabaseService(context: Context) : QueriesInterface {


    private val dao: WeatherDao = AppDatabase.getInstance(context).WeatherDaoDao()

    override fun getAllData(): List<WeatherEntity>? {
        return dao.getAllListData()
    }

    override fun insertThreads(vararg threads: WeatherEntity): Int {
        return try {
            dao.insert(*threads)
            1
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }

    override fun checkIfImageAvailable(imageId: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun deleteFavouriteImage(imageId: Int): Int {
        TODO("Not yet implemented")
    }
}