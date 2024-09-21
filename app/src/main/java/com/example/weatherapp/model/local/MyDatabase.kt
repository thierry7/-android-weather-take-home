package com.example.weatherapp.model.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.weatherapp.model.mappers.Converters


@Database(entities = [WeatherEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class MyDatabase: RoomDatabase() {

    abstract fun weatherDao(): WeatherDao
}