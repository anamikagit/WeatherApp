package com.example.weatherapp.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "weather_table")
class WeatherEntity (

    @ColumnInfo(name = "temp") val temp: String?,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "sys") val sys: String?,
    @ColumnInfo(name = "country") val country: String?

)
