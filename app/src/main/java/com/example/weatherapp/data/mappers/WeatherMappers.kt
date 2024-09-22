package com.example.weatherapp.data.mappers

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import com.example.weatherapp.data.remote.WeatherResponse
import com.example.weatherapp.domain.weather.WeatherData
import com.example.weatherapp.domain.weather.WeatherType
import com.example.weatherapp.data.local.WeatherEntity
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
fun WeatherResponse.toWeatherData(): List<WeatherData> {
    return dailyForecasts.map { dailyForecast ->
        val day = dailyForecast.date
        val time = dailyForecast.date ?: ""
        val temperature = dailyForecast.temperature?.maximum?.value ?: 0.0
        val humidity = dailyForecast.day?.relativeHumidity?.average ?: 0
        val windSpeed = dailyForecast.day?.wind?.speed?.value ?: 0
        val weatherType = dailyForecast.day?.localSource?.weatherCode?.toInt() ?: 0
        val description = dailyForecast.day?.iconPhrase!!
        val rainDrop = dailyForecast.day?.rain?.value ?: 0

        WeatherData(
            day = day,
            time = LocalDateTime.parse(time, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX")),
            temperature = temperature,
            windSpeed = windSpeed.toDouble(),
            humidity = humidity.toDouble(),
            weatherType = WeatherType.fromWMO(weatherType),
            description = description,
            rainDrop = rainDrop.toDouble()
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun WeatherResponse.toWeatherEntity(): List<WeatherEntity> {
    return dailyForecasts.map { dailyForecast ->
        val day = dailyForecast.date
        val time = dailyForecast.date ?: ""
        val temperature = dailyForecast.temperature?.maximum?.value ?: 0.0
        val humidity = dailyForecast.day?.relativeHumidity?.average ?: 0
        val windSpeed = dailyForecast.day?.wind?.speed?.value ?: 0
        val weatherType = dailyForecast.day?.localSource?.weatherCode?.toInt() ?: 0
        val description = dailyForecast.day?.iconPhrase!!
        val rainDrop = dailyForecast.day?.rain?.value?.times(100) ?: 0
        WeatherEntity(
            day = day!!,
            time = LocalDateTime.parse(time, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX")
            ),
            temperature = temperature.toDouble(),
            windSpeed = windSpeed.toDouble(),
            weatherType = WeatherType.fromWMO(weatherType),
            description = description,
            humidity = humidity.toDouble(),
            rainDrop = rainDrop.toDouble()

        )
    }
}
fun WeatherEntity.toWeatherData(): WeatherData {
    return WeatherData(
        day = this.day,
        time = this.time,
        temperature = this.temperature,
        humidity = this.humidity,
        windSpeed = this.windSpeed,
        weatherType = this.weatherType,
        description = this.description,
        rainDrop = this.rainDrop
    )
}

fun WeatherData.toWeatherEntity(): WeatherEntity {
    return WeatherEntity(
        day = this.day!!,
        time = this.time,
        temperature = this.temperature.toDouble(),
        windSpeed = this.windSpeed,
        weatherType = this.weatherType,
        description = this.description,
        humidity = this.humidity,
        rainDrop = this.rainDrop
    )
}
fun List<WeatherEntity>.toWeatherDataList(): List<WeatherData> {
    return this.map { it.toWeatherData() }
}

fun List<WeatherData>.toWeatherEntityList(): List<WeatherEntity> {
    return this.map { it.toWeatherEntity() }
}

@RequiresApi(Build.VERSION_CODES.O)
class Converters {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    // Convert LocalDateTime to a String
    @TypeConverter
    fun fromLocalDateTime(dateTime: LocalDateTime?): String? {
        return dateTime?.format(formatter)
    }

    // Convert String back to LocalDateTime
    @TypeConverter
    fun toLocalDateTime(dateTimeString: String?): LocalDateTime? {
        return dateTimeString?.let {
            LocalDateTime.parse(it, formatter)
        }
    }

    @TypeConverter
    fun fromWeatherType(weatherType: WeatherType): String {
        return weatherType.weatherDesc
    }

    @TypeConverter
    fun toWeatherType(weatherDesc: String): WeatherType {
        return when (weatherDesc) {
            "Clear sky" -> WeatherType.ClearSky
            "Mainly clear" -> WeatherType.MainlyClear
            "Partly cloudy" -> WeatherType.PartlyCloudy
            "Overcast" -> WeatherType.Overcast
            "Foggy" -> WeatherType.Foggy
            "Depositing rime fog" -> WeatherType.DepositingRimeFog
            "Light drizzle" -> WeatherType.LightDrizzle
            "Moderate drizzle" -> WeatherType.ModerateDrizzle
            "Dense drizzle" -> WeatherType.DenseDrizzle
            "Slight freezing drizzle" -> WeatherType.LightFreezingDrizzle
            "Dense freezing drizzle" -> WeatherType.DenseFreezingDrizzle
            "Slight rain" -> WeatherType.SlightRain
            "Rainy" -> WeatherType.ModerateRain
            "Heavy rain" -> WeatherType.HeavyRain
            "Heavy freezing rain" -> WeatherType.HeavyFreezingRain
            "Slight snow fall" -> WeatherType.SlightSnowFall
            "Moderate snow fall" -> WeatherType.ModerateSnowFall
            "Heavy snow fall" -> WeatherType.HeavySnowFall
            "Snow grains" -> WeatherType.SnowGrains
            "Slight rain showers" -> WeatherType.SlightRainShowers
            "Moderate rain showers" -> WeatherType.ModerateRainShowers
            "Violent rain showers" -> WeatherType.ViolentRainShowers
            "Light snow showers" -> WeatherType.SlightSnowShowers
            "Heavy snow showers" -> WeatherType.HeavySnowShowers
            "Moderate thunderstorm" -> WeatherType.ModerateThunderstorm
            "Thunderstorm with slight hail" -> WeatherType.SlightHailThunderstorm
            "Thunderstorm with heavy hail" -> WeatherType.HeavyHailThunderstorm
            else -> WeatherType.ClearSky // Default to clear sky if unknown
        }
    }
}