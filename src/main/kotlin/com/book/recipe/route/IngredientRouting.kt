package com.book.recipe.route

import com.book.recipe.data.Ingredient
import com.book.recipe.service.IngredientService
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject

private const val MISSING_ID = "Missing or malformed id"

fun Route.registerIngredientRoutes() {
    route("/ingredients") {
        ingredientRouting()
    }
}

fun Route.ingredientRouting() {

    val service: IngredientService by inject()

    get("search/{query}") {
        val query = call.parameters["query"] ?: ""
        call.respond(service.search(query))
    }

    get {
        call.respond(service.findAll())
    }

    get("{id}") {
        val id = call.parameters["id"]?.toIntOrNull() ?: return@get call.respondText(
            MISSING_ID,
            status = HttpStatusCode.BadRequest
        )
        call.respond(service.getOne(id))
    }

    put("{id}") {
        val id = call.parameters["id"]?.toIntOrNull() ?: return@put call.respondText(
            MISSING_ID,
            status = HttpStatusCode.BadRequest
        )
        val request = call.receive<Ingredient>()
        service.update(id, request)
        call.respond(HttpStatusCode.OK)
    }

    post {
        val request = call.receive<Ingredient>()
        call.respond(HttpStatusCode.Created, service.create(request))
    }

    delete("{id}") {
        val id = call.parameters["id"]?.toIntOrNull() ?: return@delete call.respondText(
            MISSING_ID,
            status = HttpStatusCode.BadRequest
        )
        service.delete(id)
        call.respond(HttpStatusCode.NoContent)
    }
}
