package org.alba.hortus.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class City(
    val insee: String,
    val cp: Int,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val altitude: Int
)

@Serializable
data class ForecastItem(
    val insee: String,
    val cp: Int,
    val latitude: Double,
    val longitude: Double,
    val day: Int,
    val datetime: String,
    val wind10m: Int,
    val gust10m: Int,
    val dirwind10m: Int,
    val rr10: Double,
    val rr1: Double,
    val probarain: Int,
    val weather: Int,
    val tmin: Int,
    val tmax: Int,
    val sun_hours: Int,
    val etp: Int,
    val probafrost: Int,
    val probafog: Int,
    val probawind70: Int,
    val probawind100: Int,
    val gustx: Int
)

@Serializable
data class WeatherResponse(
    val city: City,
    val update: String,
    val forecast: ForecastItem
)

enum class WeatherCode(val code: Int, val description: String) {
    SUNNY(0, "Sunny"),
    PARTLY_CLOUDY(1, "Partly Cloudy"),
    HAZY(2, "Hazy"),
    CLOUDY(3, "Cloudy"),
    VERY_CLOUDY(4, "Very Cloudy"),
    OVERCAST(5, "Overcast"),
    FOG(6, "Fog"),
    FREEZING_FOG(7, "Freezing Fog"),
    LIGHT_RAIN(10, "Light Rain"),
    MODERATE_RAIN(11, "Moderate Rain"),
    HEAVY_RAIN(12, "Heavy Rain"),
    LIGHT_FREEZING_RAIN(13, "Light Freezing Rain"),
    MODERATE_FREEZING_RAIN(14, "Moderate Freezing Rain"),
    HEAVY_FREEZING_RAIN(15, "Heavy Freezing Rain"),
    DRIZZLE(16, "Drizzle"),
    LIGHT_SNOW(20, "Light Snow"),
    MODERATE_SNOW(21, "Moderate Snow"),
    HEAVY_SNOW(22, "Heavy Snow"),
    LIGHT_SLEET(30, "Light Sleet"),
    MODERATE_SLEET(31, "Moderate Sleet"),
    HEAVY_SLEET(32, "Heavy Sleet"),
    LOCAL_LIGHT_RAIN_SHOWERS(40, "Local Light Rain Showers"),
    LOCAL_RAIN_SHOWERS(41, "Local Rain Showers"),
    LOCAL_HEAVY_RAIN_SHOWERS(42, "Local Heavy Rain Showers"),
    LIGHT_RAIN_SHOWERS(43, "Light Rain Showers"),
    RAIN_SHOWERS(44, "Rain Showers"),
    HEAVY_RAIN_SHOWERS(45, "Heavy Rain Showers"),
    LIGHT_FREQUENT_RAIN_SHOWERS(46, "Light Frequent Rain Showers"),
    FREQUENT_RAIN_SHOWERS(47, "Frequent Rain Showers"),
    HEAVY_FREQUENT_RAIN_SHOWERS(48, "Heavy Frequent Rain Showers"),
    LOCAL_LIGHT_SNOW_SHOWERS(60, "Local Light Snow Showers"),
    LOCAL_SNOW_SHOWERS(61, "Local Snow Showers"),
    LOCAL_HEAVY_SNOW_SHOWERS(62, "Local Heavy Snow Showers"),
    LIGHT_SNOW_SHOWERS(63, "Light Snow Showers"),
    SNOW_SHOWERS(64, "Snow Showers"),
    HEAVY_SNOW_SHOWERS(65, "Heavy Snow Showers"),
    LIGHT_FREQUENT_SNOW_SHOWERS(66, "Light Frequent Snow Showers"),
    FREQUENT_SNOW_SHOWERS(67, "Frequent Snow Showers"),
    HEAVY_FREQUENT_SNOW_SHOWERS(68, "Heavy Frequent Snow Showers"),
    LOCAL_LIGHT_SLEET_SHOWERS(70, "Local Light Sleet Showers"),
    LOCAL_SLEET_SHOWERS(71, "Local Sleet Showers"),
    LOCAL_HEAVY_SLEET_SHOWERS(72, "Local Heavy Sleet Showers"),
    LIGHT_SLEET_SHOWERS(73, "Light Sleet Showers"),
    SLEET_SHOWERS(74, "Sleet Showers"),
    HEAVY_SLEET_SHOWERS(75, "Heavy Sleet Showers"),
    LIGHT_NUMEROUS_SLEET_SHOWERS(
        76,
        "Light Numerous Sleet Showers"
    ), // "nombreuses" traduit par "numerous" pour plus de précision
    FREQUENT_SLEET_SHOWERS(77, "Frequent Sleet Showers"),
    HEAVY_FREQUENT_SLEET_SHOWERS(78, "Heavy Frequent Sleet Showers"),
    LOCAL_LIGHT_THUNDERSTORMS(100, "Local Light Thunderstorms"),
    LOCAL_THUNDERSTORMS(101, "Local Thunderstorms"),
    LOCAL_HEAVY_THUNDERSTORMS(102, "Local Heavy Thunderstorms"),
    LIGHT_THUNDERSTORMS(103, "Light Thunderstorms"),
    THUNDERSTORMS(104, "Thunderstorms"),
    HEAVY_THUNDERSTORMS(105, "Heavy Thunderstorms"),
    LIGHT_FREQUENT_THUNDERSTORMS(106, "Light Frequent Thunderstorms"),
    FREQUENT_THUNDERSTORMS(107, "Frequent Thunderstorms"),
    HEAVY_FREQUENT_THUNDERSTORMS(108, "Heavy Frequent Thunderstorms"),
    LOCAL_LIGHT_SNOW_OR_HAIL_THUNDERSTORMS(120, "Local Light Snow or Hail Thunderstorms"),
    LOCAL_SNOW_OR_HAIL_THUNDERSTORMS(121, "Local Snow or Hail Thunderstorms"),
    LOCAL_HEAVY_SNOW_OR_HAIL_THUNDERSTORMS(122, "Local Heavy Snow or Hail Thunderstorms"),
    LIGHT_SNOW_OR_HAIL_THUNDERSTORMS(123, "Light Snow or Hail Thunderstorms"),
    SNOW_OR_HAIL_THUNDERSTORMS(124, "Snow or Hail Thunderstorms"),
    HEAVY_SNOW_OR_HAIL_THUNDERSTORMS(125, "Heavy Snow or Hail Thunderstorms"),
    LIGHT_FREQUENT_SNOW_OR_HAIL_THUNDERSTORMS(126, "Light Frequent Snow or Hail Thunderstorms"),
    FREQUENT_SNOW_OR_HAIL_THUNDERSTORMS(127, "Frequent Snow or Hail Thunderstorms"),
    HEAVY_FREQUENT_SNOW_OR_HAIL_THUNDERSTORMS(128, "Heavy Frequent Snow or Hail Thunderstorms"),
    LOCAL_LIGHT_SLEET_OR_HAIL_THUNDERSTORMS(130, "Local Light Sleet or Hail Thunderstorms"),
    LOCAL_SLEET_OR_HAIL_THUNDERSTORMS(131, "Local Sleet or Hail Thunderstorms"),
    LOCAL_HEAVY_SLEET_OR_HAIL_THUNDERSTORMS(132, "Local Heavy Sleet or Hail Thunderstorms"),
    LIGHT_SLEET_OR_HAIL_THUNDERSTORMS(133, "Light Sleet or Hail Thunderstorms"),
    SLEET_OR_HAIL_THUNDERSTORMS(134, "Sleet or Hail Thunderstorms"),
    HEAVY_SLEET_OR_HAIL_THUNDERSTORMS(135, "Heavy Sleet or Hail Thunderstorms"),
    LIGHT_FREQUENT_SLEET_OR_HAIL_THUNDERSTORMS(136, "Light Frequent Sleet or Hail Thunderstorms"),
    FREQUENT_SLEET_OR_HAIL_THUNDERSTORMS(137, "Frequent Sleet or Hail Thunderstorms"),
    HEAVY_FREQUENT_SLEET_OR_HAIL_THUNDERSTORMS(138, "Heavy Frequent Sleet or Hail Thunderstorms"),
    STORMY_RAIN(140, "Stormy rain"),
    STORMY_SLEET(141, "Stormy sleet"),
    STORMY_SNOW(142, "Stormy snow"),
    LIGHT_INTERMITTENT_RAIN(210, "Light Intermittent Rain"),
    MODERATE_INTERMITTENT_RAIN(211, "Moderate Intermittent Rain"),
    HEAVY_INTERMITTENT_RAIN(212, "Heavy Intermittent Rain"),
    LIGHT_INTERMITTENT_SNOW(220, "Light Intermittent Snow"),
    MODERATE_INTERMITTENT_SNOW(221, "Moderate Intermittent Snow"),
    HEAVY_INTERMITTENT_SNOW(222, "Heavy Intermittent Snow"),
    SLEET(230, "Sleet"),
    SLEET_231(231, "Sleet"),
    SLEET_232(232, "Sleet"),
    HAIL_SHOWERS(235, "Hail Showers");


    companion object {
        fun fromCode(code: Int): WeatherCode? = values().find { it.code == code }
    }
}