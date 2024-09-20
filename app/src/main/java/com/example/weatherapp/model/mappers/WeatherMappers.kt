package com.example.weatherapp.model.mappers

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.weatherapp.model.remote.WeatherResponse
import com.example.weatherapp.domain.weather.WeatherData
import com.example.weatherapp.domain.weather.WeatherType
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


private data class IndexWeatherData(
        val index : Int,
        val data: WeatherData
)

@RequiresApi(Build.VERSION_CODES.O)
fun WeatherResponse.toWeatherData(): List<WeatherData> {
    return dailyForecasts.map { dailyForecast ->
        val day = dailyForecast.date
        val time = dailyForecast.date ?: ""
        val temperature = dailyForecast.temperature?.maximum?.value ?: 0.0
        val precipitation = dailyForecast.day?.precipitationType
        val humidity = dailyForecast.day?.iconPhrase?.let {
            75.0 // Assuming a default value for humidity, adjust accordingly
        } ?: 75.0
        val windSpeed = dailyForecast.day?.icon ?: 0
        val weatherType = dailyForecast.day?.localSource?.weatherCode?.toInt() ?: 0
        val description = dailyForecast.day?.iconPhrase!!
        WeatherData(
            day = day,
            time = LocalDateTime.parse(time, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX")
            ),
            temperature = temperature,
            precipitation = precipitation,
            windSpeed = windSpeed.toDouble(),
            humidity = humidity,
            weatherType = WeatherType.fromWMO(weatherType),
            description = description
        )
    }
}
