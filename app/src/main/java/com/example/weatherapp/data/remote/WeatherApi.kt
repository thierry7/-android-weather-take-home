package com.example.weatherapp.data.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WeatherApi {

        @GET("forecasts/v1/daily/5day/{locationKey}")
        suspend fun getWeatherForecast(@Path("locationKey") locationKey: String,
                                       @Query("apikey") apiKey: String,
                                       @Query("details") details: Boolean,
        ): WeatherResponse

}