package com.example.weatherapp.di

import android.content.Context
import androidx.room.Room
import com.example.weatherapp.BuildConfig
import com.example.weatherapp.data.local.MyDatabase
import com.example.weatherapp.data.local.WeatherDao
import com.example.weatherapp.data.remote.WeatherApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideApiKey(): String {
        return BuildConfig.API_KEY
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://dataservice.accuweather.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context): MyDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            MyDatabase::class.java,
            "ForcastData.db"
        ).fallbackToDestructiveMigration()
            .build()
    }


    @Provides
    @Singleton
    fun provideWeatherApi(retrofit: Retrofit): WeatherApi {
        return retrofit.create(WeatherApi::class.java)
    }

    @Provides
    fun provideWeatherDao(database: MyDatabase): WeatherDao = database.weatherDao()
}