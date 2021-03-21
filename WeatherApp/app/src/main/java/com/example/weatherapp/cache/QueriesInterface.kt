package com.example.weatherapp.cache

import com.example.weatherapp.entity.WeatherEntity

interface QueriesInterface {

    fun getAllData(): List<WeatherEntity>?

    fun insertThreads(vararg threads: WeatherEntity): Int

    fun checkIfImageAvailable(imageId: Int): Boolean

    fun deleteFavouriteImage(imageId: Int): Int

}