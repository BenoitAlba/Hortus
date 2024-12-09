package org.alba.hortus.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import hortus.composeapp.generated.resources.Res
import hortus.composeapp.generated.resources.baseline_cloud_24
import hortus.composeapp.generated.resources.baseline_sunny_24
import hortus.composeapp.generated.resources.baseline_sunny_snowing_24
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.DrawableResource

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

enum class Exposure(val value: String, val description: String, val drawableRes: DrawableResource) {
    SUN("Sun", "My plant is in full sun situation", Res.drawable.baseline_sunny_24),
    SHADE("Shade", "My plant is in shade situation", Res.drawable.baseline_cloud_24),
    PARTIAL_SHADE("Partial shade", "My plant is in partial shade situation", Res.drawable.baseline_sunny_snowing_24)
}