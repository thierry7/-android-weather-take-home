package com.example.weatherapp.model.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherapp.domain.weather.WeatherData
@Dao
interface WeatherDao {

    @Query("SELECT * FROM weather WHERE day = :day")
    fun getWeatherByDay(day :String): WeatherEntity

    @Query("SELECT * FROM weather ")
    fun getWeatherForcast(): List<WeatherEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(weatherList: List<WeatherEntity>)
}