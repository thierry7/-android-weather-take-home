package com.example.weatherapp.domain.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.weatherapp.model.mappers.toWeatherData
import com.example.weatherapp.model.remote.WeatherApi
import com.example.weatherapp.domain.weather.WeatherData
import com.example.weatherapp.domain.util.DataResult
import com.example.weatherapp.model.local.WeatherDao
import com.example.weatherapp.model.mappers.toWeatherDataList
import com.example.weatherapp.model.mappers.toWeatherEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class WeatherRepo @Inject constructor(
    private val weatherApi: WeatherApi,
    private val apiKey: String,
    private val weatherDao: WeatherDao
) {

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getListOfDailyForecast(zip: String): Flow<DataResult<List<WeatherData>>> {
        return flow {
            emit(DataResult.Loading)
            try {
                // Attempt to fetch data from the API
                val response = weatherApi.getWeatherForecast(zip, apiKey)

                // Cache the response in the local database
                weatherDao.insertAll(response.toWeatherEntity())

                // Emit the cached data from the local database
                val data = DataResult.Success(weatherDao.getWeatherForcast().toWeatherDataList())
                emit(data)
            } catch (e: Exception) {
                // If the API call fails (e.g., no internet), fetch from the local database
                val localData = weatherDao.getWeatherForcast().toWeatherDataList()

                if (localData.isNotEmpty()) {
                    emit(DataResult.Success(localData)) // Return cached data
                } else {
                    emit(DataResult.Error(Exception("No internet and no cached data available.")))
                }
            }
        }.flowOn(Dispatchers.IO).catch { e ->
            emit(DataResult.Error(e))
        }
    }

    // Method to get weather by a specific day
    suspend fun getWeatherByDay(day: String): Flow<DataResult<WeatherData>> {
        return flow {
            emit(DataResult.Loading)
            try {
                // Attempt to fetch the weather data for the specific day from the database
                val weather = weatherDao.getWeatherByDay(day).toWeatherData()
                emit(DataResult.Success(weather))
            } catch (e: Exception) {
                emit(DataResult.Error(e))
            }
        }.flowOn(Dispatchers.IO).catch { e ->
            emit(DataResult.Error(e))
        }
    }
}
