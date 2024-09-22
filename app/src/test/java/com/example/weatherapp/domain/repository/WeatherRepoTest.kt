package com.example.weatherapp.domain.repository

import com.example.weatherapp.data.local.WeatherDao
import com.example.weatherapp.data.local.WeatherEntity
import com.example.weatherapp.data.mappers.toWeatherDataList
import com.example.weatherapp.data.remote.WeatherApi
import com.example.weatherapp.data.remote.WeatherResponse
import com.example.weatherapp.domain.util.DataResult
import com.example.weatherapp.domain.weather.WeatherData
import com.example.weatherapp.domain.weather.WeatherType
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import java.time.LocalDateTime

class WeatherRepoTest {

    // Mock dependencies
    private lateinit var weatherApi: WeatherApi
    private lateinit var weatherDao: WeatherDao

    // Class under test
    private lateinit var weatherRepo: WeatherRepo

    private lateinit var mockApiResponse : WeatherResponse

    // Sample API key
    private val apiKey = "test_api_key"

    private val mockWeatherData = WeatherData(
        day  = "123",
        time =  LocalDateTime.of(2023, 9, 21, 12, 0) ,
        temperature = 3.0,
        windSpeed = 4.0,
        weatherType =  WeatherType.fromWMO(0),
        description =  "none",
        humidity = 2.2,
        rainDrop =4.1
    )

    private val mockWeatherEntity = WeatherEntity(
        day  = "123",
        time =  LocalDateTime.of(2023, 9, 21, 12, 0) ,
        temperature = 3.0,
        windSpeed = 04.0,
        weatherType =  WeatherType.fromWMO(0),
        description =  "none",
        humidity = 2.2,
        rainDrop =4.1,
        zipCode = "123"
    )

    @Before
    fun setUp() {
        // Initialize mocks
        weatherApi = mock(WeatherApi::class.java)
        weatherDao = mock(WeatherDao::class.java)

        mockApiResponse = mock(WeatherResponse::class.java)
        
        // Initialize the class under test
        weatherRepo = WeatherRepo(weatherApi, apiKey, weatherDao)
    }

    @Test
    fun `getListOfDailyForecast should return success with data when API call succeeds`() = runTest {

        // Given
        val zipCode = "12345"
        val mockWeatherDataList = listOf(mockWeatherData)

        `when`(weatherApi.getWeatherForecast(zipCode, apiKey, true)).thenReturn(mockApiResponse)
        `when`(weatherDao.getWeatherForecastByZip(zipCode)).thenReturn(listOf(mockWeatherEntity))

        // When
        val result = weatherRepo.getListOfDailyForecast(zipCode).toList()

        // Then
        assertTrue(result[0] is DataResult.Loading)
        assertTrue(result[1] is DataResult.Success)
        assertEquals(mockWeatherDataList, (result[1] as DataResult.Success).data)
    }

    @Test
    fun `getListOfDailyForecast should return success with cached data when API call fails`() = runTest {
        // Given
        val zipCode = "12345"
        val mockWeatherDataList = listOf(mockWeatherData)

        // Mock API call to throw an exception (e.g., no internet)
        `when`(weatherApi.getWeatherForecast(zipCode, apiKey, true)).thenThrow(RuntimeException("API call failed"))

        // Mock DAO to return cached data
        `when`(weatherDao.getWeatherForecastByZip(zipCode)).thenReturn(listOf(mockWeatherEntity))

        // When
        val result = weatherRepo.getListOfDailyForecast(zipCode).toList()

        // Then
        assertTrue(result[0] is DataResult.Loading)
        assertTrue(result[1] is DataResult.Success)
        assertEquals(mockWeatherDataList, (result[1] as DataResult.Success).data)
    }

    @Test
    fun `getListOfDailyForecast should return error when both API call and cache retrieval fail`() = runTest {
        // Given
        val zipCode = "12345"

        // Mock API call to throw an exception
        `when`(weatherApi.getWeatherForecast(zipCode, apiKey, true)).thenThrow(RuntimeException("API call failed"))

        // Mock DAO to return empty data
        `when`(weatherDao.getWeatherForecastByZip(zipCode).toWeatherDataList()).thenReturn(emptyList())

        // When
        val result = weatherRepo.getListOfDailyForecast(zipCode).toList()

        // Then
        assertTrue(result[0] is DataResult.Loading)
        assertTrue(result[1] is DataResult.Error)
        assertEquals("No internet and no cached data available.Caused By API call failed", (result[1] as DataResult.Error).exception.message)
    }

    @Test
    fun `getWeatherByDay should return success when data is found`() = runTest {
        // Given
        val day = "2023-09-21"

        // Mock DAO to return data for the specific day
        `when`(weatherDao.getWeatherByDay(day)).thenReturn(mockWeatherEntity)
        // When
        val result = weatherRepo.getWeatherByDay(day).last()

        // Then
        assertTrue(result is DataResult.Success)
        assertEquals(mockWeatherData, (result as DataResult.Success).data)
    }

    @Test
    fun `getWeatherByDay should return error when data is not found`() = runTest {
        // Given
        val day = "2023-09-21"

        // Mock DAO to throw an exception (e.g., data not found)
        `when`(weatherDao.getWeatherByDay(day)).thenThrow(RuntimeException("Data not found"))

        // When
        val result = weatherRepo.getWeatherByDay(day).last()

        // Then
        assertTrue(result is DataResult.Error)
        assertEquals("Data not found", (result as DataResult.Error).exception.message)
    }
}
