package org.alba.hortus.data.remote

import com.google.firebase.Firebase
import com.google.firebase.vertexai.type.Schema
import com.google.firebase.vertexai.type.generationConfig
import com.google.firebase.vertexai.vertexAI

/*ios version : GenerativeModelIOS*/
class GenerativeModelAndroid: GenerativeModel {
    private val jsonSchema = Schema.array(
        Schema.obj(
            mapOf(
                "commonName" to Schema.string(),
                "scientificName" to Schema.string(),
                "description" to Schema.string(),
                "floweringMonths" to Schema.array(Schema.string()),
                "fruitingMonths" to Schema.array(Schema.string()),
                "isAFruitPlant" to Schema.boolean(),
                "isAnAnnualPlant" to Schema.boolean(),
                "maxHeight" to Schema.integer(),
                "maxWidth" to Schema.integer(),
                "exposure" to Schema.array(Schema.string()),
                "soilMoisture" to Schema.string(),
                "pollination" to Schema.string(),
                "harvestMonths" to Schema.array(Schema.string()),
                "hardiness" to Schema.float()
            )
        )
    )

    override suspend fun generateTextContent(prompt: String): String? {
        val generativeModel = Firebase.vertexAI.generativeModel(
            modelName = "gemini-1.5-flash"
        )

        return generativeModel.generateContent(prompt).text
    }

    override suspend fun generateJsonContent(prompt: String): String? {
        val generativeModel = Firebase.vertexAI.generativeModel(
            modelName = "gemini-1.5-flash",
            generationConfig = generationConfig {
                responseMimeType = "application/json"
                responseSchema = jsonSchema
            }
        )

        return generativeModel.generateContent(prompt).text
    }

}