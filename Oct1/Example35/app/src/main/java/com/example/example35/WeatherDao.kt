package com.example.example35

import androidx.room.*

import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    // Insert ignore
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(weatherTable: WeatherTable)

    // Delete all
    @Query("DELETE FROM weather_table")
    suspend fun deleteAll()

    // Get all the weather info that is currently in the database
    // automatically triggered when the db is updated because of Flow<List<WeatherTable>>
    @Query("SELECT * from weather_table where lower(weatherdata)!='dummy_data' ORDER BY weatherdata")
    fun getAllWeather(): Flow<List<WeatherTable>>
}