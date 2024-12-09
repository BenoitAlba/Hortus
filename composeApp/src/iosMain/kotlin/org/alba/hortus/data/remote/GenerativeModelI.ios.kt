package org.alba.hortus.data.remote

import org.alba.hortus.di.generativeModelIOS

class GenerativeModelIOS: GenerativeModel {
    override suspend fun generateTextContent(prompt: String): String? {
        return generativeModelIOS?.generateTextContent(prompt)
    }

    override suspend fun generateJsonContent(prompt: String): String? {
        return generativeModelIOS?.generateJsonContent(prompt)
    }
}