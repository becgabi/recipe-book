package com.book.recipe.util

import io.ktor.application.*
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.util.pipeline.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

private const val ID = "id"

suspend inline fun <reified T> httpRequest(url: String, params: Map<String, Any>): T {
    val response = HttpClient().use { client ->
        client.get<HttpResponse>(url) {
            params.forEach { parameter(it.key, it.value) }
        }.readText()
    }
    return Json { ignoreUnknownKeys = true }.decodeFromString(response)
}

suspend fun PipelineContext<Unit, ApplicationCall>.respondBadRequest(param: String = ID) {
    call.respondText(
        "Missing or malformed $param",
        status = HttpStatusCode.BadRequest
    )
}

fun PipelineContext<Unit, ApplicationCall>.getIdParamOrNull(): Int? {
    return call.parameters["id"]?.toIntOrNull()
}
