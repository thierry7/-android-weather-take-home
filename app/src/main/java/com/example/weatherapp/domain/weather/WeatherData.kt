package com.example.weatherapp.domain.weather

import java.time.LocalDateTime

data class WeatherData(
    val id : Int =0,
    val day : String?,
    val time: LocalDateTime,
    val temperature: Number,
    val humidity: Double,
    val windSpeed: Double,
    val weatherType: WeatherType,
    val description: String,
    val rainDrop: Double
)
