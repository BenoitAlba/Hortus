package org.alba.hortus.domain.model

import hortus.composeapp.generated.resources.*
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

@Serializable
data class City(
    val country: Int = 0,
    val city: String = "",
    val insee: Int = 0,
    val cp: Int = 0,
    val name: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val altitude: Int = 0
)

@Serializable
data class ForecastItem(
    val country: Int = 0,
    val cp: Int = 0,
    val insee: Int = 0,
    val city: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val day: Int = 0,
    val datetime: String = "",
    val wind10m: Int = 0,
    val gust10m: Int = 0,
    val dirwind10m: Int = 0,
    val rr10: Double = 0.0,
    val rr1: Double = 0.0,
    val probarain: Int = 0,
    val weather: Int = 0,
    val tmin: Int = 0,
    val tmax: Int = 0,
    val sun_hours: Int = 0,
    val etp: Int = 0,
    val probafrost: Int = 0,
    val probafog: Int = 0,
    val probawind70: Int = 0,
    val probawind100: Int = 0,
    val gustx: Int = 0
)

@Serializable
data class WeatherResponse(
    val city: City,
    val update: String,
    val forecast: ForecastItem
)

enum class WeatherCode(val code: Int, val description: StringResource, val image: DrawableResource) {
    SUNNY(0, Res.string.weather_sunny, Res.drawable.sun),
    PARTLY_CLOUDY(1, Res.string.weather_partly_cloudy, Res.drawable.sunandcloud),
    HAZY(2, Res.string.weather_hazy, Res.drawable.fog),
    CLOUDY(3, Res.string.weather_cloudy, Res.drawable.cloud),
    VERY_CLOUDY(4, Res.string.weather_very_cloudy, Res.drawable.cloud),
    OVERCAST(5, Res.string.weather_overcast, Res.drawable.cloud),
    FOG(6, Res.string.weather_fog, Res.drawable.fog),
    FREEZING_FOG(7, Res.string.weather_freezing_fog, Res.drawable.fog),
    LIGHT_RAIN(10, Res.string.weather_light_rain, Res.drawable.rain),
    MODERATE_RAIN(11, Res.string.weather_moderate_rain, Res.drawable.rain),
    HEAVY_RAIN(12, Res.string.weather_heavy_rain, Res.drawable.rain),
    LIGHT_FREEZING_RAIN(13, Res.string.weather_light_freezing_rain, Res.drawable.rain),
    MODERATE_FREEZING_RAIN(14, Res.string.weather_moderate_freezing_rain, Res.drawable.rain),
    HEAVY_FREEZING_RAIN(15, Res.string.weather_heavy_freezing_rain, Res.drawable.rain),
    DRIZZLE(16, Res.string.weather_drizzle, Res.drawable.rain),
    LIGHT_SNOW(20, Res.string.weather_light_snow, Res.drawable.snow),
    MODERATE_SNOW(21, Res.string.weather_moderate_snow, Res.drawable.snow),
    HEAVY_SNOW(22, Res.string.weather_heavy_snow, Res.drawable.snow),
    LIGHT_SLEET(30, Res.string.weather_light_sleet, Res.drawable.snow),
    MODERATE_SLEET(31, Res.string.weather_moderate_sleet, Res.drawable.snow),
    HEAVY_SLEET(32, Res.string.weather_heavy_sleet, Res.drawable.snow),
    LOCAL_LIGHT_RAIN_SHOWERS(40, Res.string.weather_local_light_rain_showers, Res.drawable.rain),
    LOCAL_RAIN_SHOWERS(41, Res.string.weather_local_rain_showers, Res.drawable.rain),
    LOCAL_HEAVY_RAIN_SHOWERS(42, Res.string.weather_local_heavy_rain_showers, Res.drawable.rain),
    LIGHT_RAIN_SHOWERS(43, Res.string.weather_light_rain_showers, Res.drawable.rain),
    RAIN_SHOWERS(44, Res.string.weather_rain_showers, Res.drawable.rain),
    HEAVY_RAIN_SHOWERS(45, Res.string.weather_heavy_rain_showers, Res.drawable.rain),
    LIGHT_FREQUENT_RAIN_SHOWERS(46, Res.string.weather_light_frequent_rain_showers, Res.drawable.rain),
    FREQUENT_RAIN_SHOWERS(47, Res.string.weather_frequent_rain_showers, Res.drawable.rain),
    HEAVY_FREQUENT_RAIN_SHOWERS(48, Res.string.weather_heavy_frequent_rain_showers, Res.drawable.rain),
    LOCAL_LIGHT_SNOW_SHOWERS(60, Res.string.weather_local_light_snow_showers, Res.drawable.snow),
    LOCAL_SNOW_SHOWERS(61, Res.string.weather_local_snow_showers, Res.drawable.snow),
    LOCAL_HEAVY_SNOW_SHOWERS(62, Res.string.weather_local_heavy_snow_showers, Res.drawable.snow),
    LIGHT_SNOW_SHOWERS(63, Res.string.weather_light_snow_showers, Res.drawable.snow),
    SNOW_SHOWERS(64, Res.string.weather_snow_showers, Res.drawable.snow),
    HEAVY_SNOW_SHOWERS(65, Res.string.weather_heavy_snow_showers, Res.drawable.snow),
    LIGHT_FREQUENT_SNOW_SHOWERS(66, Res.string.weather_light_frequent_snow_showers, Res.drawable.snow),
    FREQUENT_SNOW_SHOWERS(67, Res.string.weather_frequent_snow_showers, Res.drawable.snow),
    HEAVY_FREQUENT_SNOW_SHOWERS(68, Res.string.weather_heavy_frequent_snow_showers, Res.drawable.snow),
    LIGHT_THUNDERSTORMS(103, Res.string.weather_light_thunderstorms, Res.drawable.stormysnow),
    THUNDERSTORMS(104, Res.string.weather_thunderstorms, Res.drawable.stormysnow),
    HEAVY_THUNDERSTORMS(105, Res.string.weather_heavy_thunderstorms, Res.drawable.stormysnow),
    HAIL_SHOWERS(235, Res.string.weather_hail_showers, Res.drawable.hail);

    companion object {
        fun fromCode(code: Int): WeatherCode? = values().find { it.code == code }
    }
}
