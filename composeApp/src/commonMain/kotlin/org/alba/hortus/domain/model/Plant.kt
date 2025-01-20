package org.alba.hortus.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import hortus.composeapp.generated.resources.Res
import hortus.composeapp.generated.resources.baseline_cloud_24
import hortus.composeapp.generated.resources.baseline_sunny_24
import hortus.composeapp.generated.resources.exposure_partial_shade
import hortus.composeapp.generated.resources.exposure_partial_shade_description
import hortus.composeapp.generated.resources.exposure_shade
import hortus.composeapp.generated.resources.exposure_shade_description
import hortus.composeapp.generated.resources.exposure_sun
import hortus.composeapp.generated.resources.exposure_sun_description
import hortus.composeapp.generated.resources.partly_cloudy_day_24dp
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

@Serializable
@Entity(tableName = "plants")
@TypeConverters(StringListConverters::class)
data class PlantDatabaseModel(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val commonName: String,
    val scientificName: String? = null,
    val description: String? = null,
    val floweringMonths: List<String>? = null, // months number from 1 to 12
    val fruitingMonths: List<String>? = null, // months number from 1 to 12
    val isAFruitPlant: Boolean? = false,
    val isAnAnnualPlant: Boolean? = false,
    val maxHeight: Int? = null, // cm
    val maxWidth: Int? = null, // cm
    val currentExposure: String = "",
    val exposure: List<String>? = null, // list of exposure types (sun, shade, partial shade)
    val exposureAdvise: String? = null,
    val soilMoisture: String? = null,
    val pollination: String? = null, // if fruit tree
    val harvestMonths: List<String>? = null, // if fruit tree months number from 1 to 12
    val hardiness: Float? = 0.0f,
    val createdAt: Long = 0,
    val updatedAt: Long? = 0,
    val image: String? = null
)

class StringListConverters {
    @TypeConverter
    fun fromList(value: List<String>) =
        Json.encodeToString(ListSerializer(String.serializer()), value)

    @TypeConverter
    fun toList(value: String) = Json.decodeFromString<List<String>>(value)
}

enum class Exposure(
    val value: StringResource,
    val description: StringResource,
    val drawableRes: DrawableResource
) {
    SUN(
        Res.string.exposure_sun,
        Res.string.exposure_sun_description,
        Res.drawable.baseline_sunny_24
    ),
    SHADE(
        Res.string.exposure_shade,
        Res.string.exposure_shade_description,
        Res.drawable.baseline_cloud_24
    ),
    PARTIAL_SHADE(
        Res.string.exposure_partial_shade,
        Res.string.exposure_partial_shade_description,
        Res.drawable.partly_cloudy_day_24dp
    );

    companion object {
        fun getAllNames() = entries.map { it.name }.joinToString(", ")
        fun getEnumForName(name: String) = entries.find { it.name == name }
    }
}