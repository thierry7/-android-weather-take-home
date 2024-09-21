package com.example.weatherapp.ui.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.domain.weather.WeatherData
import com.example.weatherapp.domain.repository.WeatherRepo
import com.example.weatherapp.domain.util.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    val repo: WeatherRepo
): ViewModel() {



    // State to hold the weather forecast data
    private val _weatherForecast = MutableStateFlow<DataResult<List<WeatherData>>>(DataResult.Loading)
    val weatherForecast: StateFlow<DataResult<List<WeatherData>>> = _weatherForecast

    private val _dailyWeather = MutableStateFlow<DataResult<WeatherData>>(DataResult.Loading)
    val dailyWeather : StateFlow<DataResult<WeatherData>> = _dailyWeather

    // Method to fetch weather data from the repository
    @RequiresApi(Build.VERSION_CODES.O)
    fun getDailyForecast(zip: String) {
        viewModelScope.launch {
            repo.getListOfDailyForecast(zip).collect { result ->
                _weatherForecast.value = result
            }
        }
    }

    fun getDailyData(day : String){
        viewModelScope.launch {
            repo.getWeatherByDay(day).collect { result ->
                _dailyWeather.value = result
            }
        }
    }



}