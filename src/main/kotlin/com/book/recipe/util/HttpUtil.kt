package com.book.recipe.util

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

suspend inline fun <reified T> httpRequest(url: String, params: Map<String, Any>): T {
    val response = HttpClient().use { client ->
        client.get<HttpResponse>(url) {
            params.forEach { parameter(it.key, it.value) }
        }.readText()
    }
    return Json { ignoreUnknownKeys = true }.decodeFromString(response)
}
