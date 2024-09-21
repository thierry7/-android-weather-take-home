package com.example.weatherapp.model.local

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.weatherapp.domain.weather.WeatherType
import java.time.LocalDateTime

@Entity(tableName = "weather")
data class WeatherEntity(
    @PrimaryKey
    @NonNull
    val day : String,
    val time: LocalDateTime,
    val temperature: Double,
    val windSpeed: Double,
    val weatherType: WeatherType,
    val description: String,
    val humidity: Double,
    val rainDrop: Double
)
