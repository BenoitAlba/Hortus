//
//  GenerativeModelIOS.swift
//  iosApp
//
//  Created by Benoit  on 09/12/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation
import FirebaseVertexAI
import ComposeApp

class GenerativeModelIOS: ComposeApp.GenerativeModel {
    static let shared = GenerativeModelIOS()

    let vertex = VertexAI.vertexAI()

    let jsonSchema = Schema.array(
      items: .object(
        properties: [
                   "commonName": .string(),
                   "scientificName": .string(),
                   "description": .string(),
                   "floweringMonths": .array(items: .string()),
                   "fruitingMonths": .array(items: .string()),
                   "isAFruitPlant": .boolean(),
                   "isAnAnnualPlant": .boolean(),
                   "maxHeight": .integer(),
                   "maxWidth": .integer(),
                   "exposure": .array(items: .string()),
                   "soilMoisture": .string(),
                   "pollination": .string(),
                   "harvestMonths": .array(items: .string()),
                   "hardiness": .float()
                ]
      )
    )

    func generateTextContent(prompt: String) async throws -> String? {
        let model = vertex.generativeModel(
            modelName: "gemini-1.5-flash"
        )

        return try await model.generateContent(prompt).text
    }

    func generateJsonContent(prompt: String) async throws -> String? {
        let model = vertex.generativeModel(
            modelName: "gemini-1.5-flash",
            generationConfig: GenerationConfig(
                responseMIMEType: "application/json",
                responseSchema: jsonSchema
            )
        )

        return try await model.generateContent(prompt).text
    }
}
