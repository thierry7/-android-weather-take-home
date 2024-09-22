package com.example.weatherapp.ui.viewmodel

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.weatherapp.domain.repository.WeatherRepo
import com.example.weatherapp.domain.util.DataResult
import com.example.weatherapp.domain.weather.WeatherData
import com.example.weatherapp.domain.weather.WeatherType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.time.LocalDateTime

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: WeatherViewModel
    private val mockRepo: WeatherRepo = mock()

    private val weatherData = WeatherData(
        day  = "123",
        time =  LocalDateTime.of(2023, 9, 21, 12, 0) ,
        temperature = 3.0,
        windSpeed = 4.0,
        weatherType =  WeatherType.fromWMO(0),
        description =  "none",
        humidity = 2.2,
        rainDrop =4.1,
        id = 1
    )

    @Before
    fun setUp() {

        viewModel = WeatherViewModel(mockRepo)
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
    @Test
    fun `test getDailyForecast returns success`() = runTest {
        val zip = "12345"
        val weatherDataList = listOf(weatherData)

        whenever(mockRepo.getListOfDailyForecast(zip)).thenReturn(flow {
            emit(DataResult.Loading)
            emit(DataResult.Success(weatherDataList))
        })

        viewModel.getDailyForecast(zip)

        val result = viewModel.weatherForecast.take(2).toList()

        assert(result[0] is DataResult.Loading)

        assert(result[1] is DataResult.Success)
        assert((result[1] as DataResult.Success).data == weatherDataList)

    }

    @Test
    fun `test getDailyData returns success`() = runTest {
        val id = "1"

        whenever(mockRepo.getWeatherByDay(id)).thenReturn(flow {
            emit(DataResult.Loading)
            emit(DataResult.Success(weatherData))
        })

        viewModel.getDailyData(id)

        val result = viewModel.dailyWeather.take(2).toList()

        assert(result[0] is DataResult.Loading)

        assert(result[1] is DataResult.Success)
        assert((result[1] as DataResult.Success).data == weatherData)

    }

    @Test
    fun `test getDailyForecast returns error`() = runTest {
        val zip = "12345"
        val errorMessage = "Error message"
        whenever(mockRepo.getListOfDailyForecast(zip)).thenReturn(flow {
            emit(DataResult.Loading)
            emit(DataResult.Error(Throwable(errorMessage)))
        })

        viewModel.getDailyForecast(zip)

        val result = viewModel.weatherForecast.take(2).toList()

        assert(result[0] is DataResult.Loading)

        assert(result[1] is DataResult.Error)
        assert((result[1] as DataResult.Error).exception.message == errorMessage)
    }
}