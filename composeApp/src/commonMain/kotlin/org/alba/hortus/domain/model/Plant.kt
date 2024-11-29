package org.alba.hortus.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json

@Entity(tableName = "plants")
@TypeConverters(StringListConverters::class)
data class PlantDatabaseModel(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val commonName: String,
    val scientificName: String? = null,
    val description: String? = null,
    val floweringPeriod: List<String>? = null, // months number from 1 to 12
    val fruitingPeriod: List<String>? = null, // months number from 1 to 12
    val maxHeight: Int? = null, // cm
    val maxWidth: Int? = null, // cm
    val exposure: List<String>? = null, // list of exposure types (sun, shade, partial shade)
    val soilMoisture: String? = null,
    val pollination: String? = null, // if fruit tree
    val harvestPeriod: List<String>? = null, // if fruit tree months number from 1 to 12
    val hardiness: Int = 0,
    val createdAt: Long = 0,
    val updatedAt: Long? = 0,
)

class StringListConverters {
    @TypeConverter
    fun fromList(value: List<String>) =
        Json.encodeToString(ListSerializer(String.serializer()), value)

    @TypeConverter
    fun toList(value: String) = Json.decodeFromString<List<String>>(value)
}