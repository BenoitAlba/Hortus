package org.alba.hortus.data.remote

interface GenerativeModel {
    suspend fun generateTextContent(prompt: String): String?
    suspend fun generateJsonContent(prompt: String): String?
}

