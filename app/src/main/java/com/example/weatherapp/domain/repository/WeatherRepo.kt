package com.example.weatherapp.domain.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.weatherapp.model.mappers.toWeatherData
import com.example.weatherapp.model.remote.WeatherApi
import com.example.weatherapp.domain.weather.WeatherData
import com.example.weatherapp.domain.util.DataResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class WeatherRepo @Inject constructor(
    private val weatherApi: WeatherApi,
    private val apiKey: String
) {

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getListOfDailyForecast(zip: String): Flow<DataResult<List<WeatherData>>>{

        return flow{
            emit(DataResult.Loading)
            val response = weatherApi.getWeatherForecast(zip, apiKey ).toWeatherData()
            emit(DataResult.Success(response))
        }.flowOn(Dispatchers.IO).catch { e ->
            println("====>" + e.message)
            emit(DataResult.Error(e))

        }
    }
}