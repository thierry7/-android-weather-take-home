package com.example.weatherapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WeatherDao {

    @Query("SELECT * FROM weather WHERE id = :id")
    fun getWeatherByDay(id :String): WeatherEntity

    @Query("SELECT * FROM weather WHERE zipCode = :zipCode")
    fun getWeatherForecastByZip(zipCode: String): List<WeatherEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(weatherList: List<WeatherEntity>)

    @Query("DELETE FROM weather WHERE zipCode = :zipCode")
    suspend fun deleteByZipCode(zipCode: String)
}