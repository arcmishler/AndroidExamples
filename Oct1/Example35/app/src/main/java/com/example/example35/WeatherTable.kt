package com.example.example35

import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.Entity

// This does not need to be its own class. You could directly annotate the WeatherData class
// But since our Weather class is messy, it makes sense to strip out the parts
// that we want stored in the db (just for testing and maintainability).
// This does introduce the issue of drift between this class and the WeatherData class
@Entity(tableName = "weather_table")
data class WeatherTable(
    @field:ColumnInfo(name = "location")
    @field:PrimaryKey
    var location: String,
    @field:ColumnInfo(
    name = "weatherdata"
    ) var weatherJson: String
)