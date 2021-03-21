package com.example.weatherapp.cache

import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.Query
import com.example.weatherapp.entity.WeatherEntity

interface WeatherDao {

    @Insert
    fun insert(vararg weatherFullData: WeatherEntity?)

    @Query("SELECT * FROM weather_table")
    fun getAll(): LiveData<List<WeatherEntity>>

    @Query("SELECT * FROM weather_table")
    fun getAllListData(): List<WeatherEntity>

    @Query("DELETE FROM weather_table")
    fun deleteAll()

}