package org.alba.hortus.domain.model

import hortus.composeapp.generated.resources.Res
import hortus.composeapp.generated.resources.cloud
import hortus.composeapp.generated.resources.fog
import hortus.composeapp.generated.resources.hail
import hortus.composeapp.generated.resources.rain
import hortus.composeapp.generated.resources.snow
import hortus.composeapp.generated.resources.stormysnow
import hortus.composeapp.generated.resources.sun
import hortus.composeapp.generated.resources.sunandcloud
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.DrawableResource

@Serializable
data class City(
    val country: Int,
    val city: String,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val altitude: Int
)

@Serializable
data class ForecastItem(
    val country: Int,
    val city: String,
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

enum class WeatherCode(val code: Int, val description: String, val image: DrawableResource) {
    SUNNY(0, "Sunny", Res.drawable.sun),
    PARTLY_CLOUDY(1, "Partly Cloudy", Res.drawable.sunandcloud),
    HAZY(2, "Hazy", Res.drawable.fog),
    CLOUDY(3, "Cloudy", Res.drawable.cloud),
    VERY_CLOUDY(4, "Very Cloudy", Res.drawable.cloud),
    OVERCAST(5, "Overcast", Res.drawable.cloud),
    FOG(6, "Fog", Res.drawable.fog),
    FREEZING_FOG(7, "Freezing Fog", Res.drawable.fog),
    LIGHT_RAIN(10, "Light Rain", Res.drawable.rain),
    MODERATE_RAIN(11, "Moderate Rain", Res.drawable.rain),
    HEAVY_RAIN(12, "Heavy Rain", Res.drawable.rain),
    LIGHT_FREEZING_RAIN(13, "Light Freezing Rain", Res.drawable.rain), // À adapter avec une icône plus spécifique si disponible
    MODERATE_FREEZING_RAIN(14, "Moderate Freezing Rain", Res.drawable.rain), // À adapter
    HEAVY_FREEZING_RAIN(15, "Heavy Freezing Rain", Res.drawable.rain), // À adapter
    DRIZZLE(16, "Drizzle", Res.drawable.rain),
    LIGHT_SNOW(20, "Light Snow", Res.drawable.snow),
    MODERATE_SNOW(21, "Moderate Snow", Res.drawable.snow),
    HEAVY_SNOW(22, "Heavy Snow", Res.drawable.snow),
    LIGHT_SLEET(30, "Light Sleet", Res.drawable.snow), // Grésil
    MODERATE_SLEET(31, "Moderate Sleet", Res.drawable.snow), // Grésil
    HEAVY_SLEET(32, "Heavy Sleet", Res.drawable.snow), // Grésil
    LOCAL_LIGHT_RAIN_SHOWERS(40, "Local Light Rain Showers", Res.drawable.rain),
    LOCAL_RAIN_SHOWERS(41, "Local Rain Showers", Res.drawable.rain),
    LOCAL_HEAVY_RAIN_SHOWERS(42, "Local Heavy Rain Showers", Res.drawable.rain),
    LIGHT_RAIN_SHOWERS(43, "Light Rain Showers", Res.drawable.rain),
    RAIN_SHOWERS(44, "Rain Showers", Res.drawable.rain),
    HEAVY_RAIN_SHOWERS(45, "Heavy Rain Showers", Res.drawable.rain),
    LIGHT_FREQUENT_RAIN_SHOWERS(46, "Light Frequent Rain Showers", Res.drawable.rain),
    FREQUENT_RAIN_SHOWERS(47, "Frequent Rain Showers", Res.drawable.rain),
    HEAVY_FREQUENT_RAIN_SHOWERS(48, "Heavy Frequent Rain Showers", Res.drawable.rain),
    LOCAL_LIGHT_SNOW_SHOWERS(60, "Local Light Snow Showers", Res.drawable.snow),
    LOCAL_SNOW_SHOWERS(61, "Local Snow Showers", Res.drawable.snow),
    LOCAL_HEAVY_SNOW_SHOWERS(62, "Local Heavy Snow Showers", Res.drawable.snow),
    LIGHT_SNOW_SHOWERS(63, "Light Snow Showers", Res.drawable.snow),
    SNOW_SHOWERS(64, "Snow Showers", Res.drawable.snow),
    HEAVY_SNOW_SHOWERS(65, "Heavy Snow Showers", Res.drawable.snow),
    LIGHT_FREQUENT_SNOW_SHOWERS(66, "Light Frequent Snow Showers", Res.drawable.snow),
    FREQUENT_SNOW_SHOWERS(67, "Frequent Snow Showers", Res.drawable.snow),
    HEAVY_FREQUENT_SNOW_SHOWERS(68, "Heavy Frequent Snow Showers", Res.drawable.snow),
    LOCAL_LIGHT_SLEET_SHOWERS(70, "Local Light Sleet Showers", Res.drawable.snow), // Grésil
    LOCAL_SLEET_SHOWERS(71, "Local Sleet Showers", Res.drawable.snow), // Grésil
    LOCAL_HEAVY_SLEET_SHOWERS(72, "Local Heavy Sleet Showers", Res.drawable.snow), // Grésil
    LIGHT_SLEET_SHOWERS(73, "Light Sleet Showers", Res.drawable.snow), // Grésil
    SLEET_SHOWERS(74, "Sleet Showers", Res.drawable.snow), // Grésil
    HEAVY_SLEET_SHOWERS(75, "Heavy Sleet Showers", Res.drawable.snow), // Grésil
    LIGHT_NUMEROUS_SLEET_SHOWERS(76, "Light Numerous Sleet Showers", Res.drawable.snow), // Grésil
    FREQUENT_SLEET_SHOWERS(77, "Frequent Sleet Showers", Res.drawable.snow), // Grésil
    HEAVY_FREQUENT_SLEET_SHOWERS(78, "Heavy Frequent Sleet Showers", Res.drawable.snow), // Grésil
    LOCAL_LIGHT_THUNDERSTORMS(100, "Local Light Thunderstorms", Res.drawable.stormysnow),
    LOCAL_THUNDERSTORMS(101, "Local Thunderstorms", Res.drawable.stormysnow),
    LOCAL_HEAVY_THUNDERSTORMS(102, "Local Heavy Thunderstorms", Res.drawable.stormysnow),
    LIGHT_THUNDERSTORMS(103, "Light Thunderstorms", Res.drawable.stormysnow),
    THUNDERSTORMS(104, "Thunderstorms", Res.drawable.stormysnow),
    HEAVY_THUNDERSTORMS(105, "Heavy Thunderstorms", Res.drawable.stormysnow),
    LIGHT_FREQUENT_THUNDERSTORMS(106, "Light Frequent Thunderstorms", Res.drawable.stormysnow),
    FREQUENT_THUNDERSTORMS(107, "Frequent Thunderstorms", Res.drawable.stormysnow),
    HEAVY_FREQUENT_THUNDERSTORMS(108, "Heavy Frequent Thunderstorms", Res.drawable.stormysnow),
    LOCAL_LIGHT_SNOW_OR_HAIL_THUNDERSTORMS(120, "Local Light Snow or Hail Thunderstorms", Res.drawable.stormysnow),
    LOCAL_SNOW_OR_HAIL_THUNDERSTORMS(121, "Local Snow or Hail Thunderstorms", Res.drawable.stormysnow),
    LOCAL_HEAVY_SNOW_OR_HAIL_THUNDERSTORMS(122, "Local Heavy Snow or Hail Thunderstorms", Res.drawable.stormysnow),
    LIGHT_SNOW_OR_HAIL_THUNDERSTORMS(123, "Light Snow or Hail Thunderstorms", Res.drawable.stormysnow),
    SNOW_OR_HAIL_THUNDERSTORMS(124, "Snow or Hail Thunderstorms", Res.drawable.stormysnow),
    HEAVY_SNOW_OR_HAIL_THUNDERSTORMS(125, "Heavy Snow or Hail Thunderstorms", Res.drawable.stormysnow),
    LIGHT_FREQUENT_SNOW_OR_HAIL_THUNDERSTORMS(126, "Light Frequent Snow or Hail Thunderstorms", Res.drawable.stormysnow),
    FREQUENT_SNOW_OR_HAIL_THUNDERSTORMS(127, "Frequent Snow or Hail Thunderstorms", Res.drawable.stormysnow),
    HEAVY_FREQUENT_SNOW_OR_HAIL_THUNDERSTORMS(128, "Heavy Frequent Snow or Hail Thunderstorms", Res.drawable.stormysnow),
    LOCAL_LIGHT_SLEET_OR_HAIL_THUNDERSTORMS(130, "Local Light Sleet or Hail Thunderstorms", Res.drawable.stormysnow),
    LOCAL_SLEET_OR_HAIL_THUNDERSTORMS(131, "Local Sleet or Hail Thunderstorms", Res.drawable.stormysnow),
    LOCAL_HEAVY_SLEET_OR_HAIL_THUNDERSTORMS(132, "Local Heavy Sleet or Hail Thunderstorms", Res.drawable.stormysnow),
    LIGHT_SLEET_OR_HAIL_THUNDERSTORMS(133, "Light Sleet or Hail Thunderstorms", Res.drawable.stormysnow),
    SLEET_OR_HAIL_THUNDERSTORMS(134, "Sleet or Hail Thunderstorms", Res.drawable.hail),
    HEAVY_SLEET_OR_HAIL_THUNDERSTORMS(135, "Heavy Sleet or Hail Thunderstorms", Res.drawable.hail),
    LIGHT_FREQUENT_SLEET_OR_HAIL_THUNDERSTORMS(136, "Light Frequent Sleet or Hail Thunderstorms", Res.drawable.hail),
    FREQUENT_SLEET_OR_HAIL_THUNDERSTORMS(137, "Frequent Sleet or Hail Thunderstorms", Res.drawable.hail),
    HEAVY_FREQUENT_SLEET_OR_HAIL_THUNDERSTORMS(138, "Heavy Frequent Sleet or Hail Thunderstorms", Res.drawable.snow),
    STORMY_RAIN(140, "Stormy rain", Res.drawable.rain),
    STORMY_SLEET(141, "Stormy sleet", Res.drawable.snow),
    STORMY_SNOW(142, "Stormy snow", Res.drawable.snow),
    LIGHT_INTERMITTENT_RAIN(210, "Light Intermittent Rain", Res.drawable.rain),
    MODERATE_INTERMITTENT_RAIN(211, "Moderate Intermittent Rain", Res.drawable.rain),
    HEAVY_INTERMITTENT_RAIN(212, "Heavy Intermittent Rain", Res.drawable.rain),
    LIGHT_INTERMITTENT_SNOW(220, "Light Intermittent Snow", Res.drawable.snow),
    MODERATE_INTERMITTENT_SNOW(221, "Moderate Intermittent Snow", Res.drawable.snow),
    HEAVY_INTERMITTENT_SNOW(222, "Heavy Intermittent Snow", Res.drawable.snow),
    SLEET(230, "Sleet", Res.drawable.snow),
    SLEET_231(231, "Sleet", Res.drawable.snow),
    SLEET_232(232, "Sleet",Res.drawable.snow),
    HAIL_SHOWERS(235, "Hail Showers",Res.drawable.hail);


    companion object {
        fun fromCode(code: Int): WeatherCode? = entries.find { it.code == code }
    }
}