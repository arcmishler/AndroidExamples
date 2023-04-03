package com.varunshankar.example25

class WeatherData {
    //Setters and Getters
    var locationData: LocationData? = null
    var currentCondition: CurrentCondition? = null
    var temperature: Temperature? = null
    var wind: Wind? = null
    var rain: Rain? = null
    var snow: Snow? = null
    var clouds: Clouds? = null

    inner class CurrentCondition {
        var weatherId: Long = 0
        var condition: String? = null
        var descr: String? = null
        var icon: String? = null
        var pressure = 0.0
        var humidity = 0.0
    }

    inner class Temperature {
        var temp = 0.0
        var minTemp = 0.0
        var maxTemp = 0.0
    }

    inner class Wind {
        var speed = 0.0
        var deg = 0.0
    }

    inner class Rain {
        var time: String? = null
        var amount = 0.0
    }

    inner class Snow {
        var time: String? = null
        var amount = 0.0
    }

    inner class Clouds {
        var perc: Long = 0
    }
}