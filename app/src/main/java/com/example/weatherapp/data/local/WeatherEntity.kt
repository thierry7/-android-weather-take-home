package com.example.weatherapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.weatherapp.domain.weather.WeatherType
import java.time.LocalDateTime

@Entity(tableName = "weather")
data class WeatherEntity(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val day : String,
    val time: LocalDateTime,
    val temperature: Double,
    val windSpeed: Double,
    val weatherType: WeatherType,
    val description: String,
    val humidity: Double,
    val rainDrop: Double,
    val zipCode : String
)
