package com.example.weatherapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weatherapp.data.mappers.Converters


@Database(entities = [WeatherEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class MyDatabase: RoomDatabase() {

    abstract fun weatherDao(): WeatherDao
}