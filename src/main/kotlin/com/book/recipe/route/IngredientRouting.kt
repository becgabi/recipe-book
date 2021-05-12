package com.book.recipe.route

import com.book.recipe.data.Ingredient
import com.book.recipe.service.IngredientService
import com.book.recipe.util.getIdParamOrNull
import com.book.recipe.util.respondBadRequest
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject

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
        val id = getIdParamOrNull() ?: return@get respondBadRequest()
        call.respond(service.getOne(id))
    }

    put("{id}") {
        val id = getIdParamOrNull() ?: return@put respondBadRequest()
        val request = call.receive<Ingredient>()
        service.update(id, request)
        call.respond(HttpStatusCode.OK)
    }

    post {
        val request = call.receive<Ingredient>()
        call.respond(HttpStatusCode.Created, service.create(request))
    }

    delete("{id}") {
        val id = getIdParamOrNull() ?: return@delete respondBadRequest()
        service.delete(id)
        call.respond(HttpStatusCode.NoContent)
    }
}
