package com.book.recipe.route

import com.book.recipe.data.Recipe
import com.book.recipe.data.SearchRequest
import com.book.recipe.service.RecipeService
import com.book.recipe.util.getIdParamOrNull
import com.book.recipe.util.respondBadRequest
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject

fun Route.registerRecipeRoutes() {
    route("/recipes") {
        recipeRouting()
    }
}

fun Route.recipeRouting() {

    val service: RecipeService by inject()

    get("{id}/portions/{portionNumber}") {
        val id = getIdParamOrNull() ?: return@get respondBadRequest()
        val portionNumber =
            call.parameters["portionNumber"]?.toFloatOrNull() ?: return@get respondBadRequest("portionNumber")
        call.respond(service.getPortion(id, portionNumber))
    }

    get("search") {
        val searchRequest = call.receive<SearchRequest>()
        call.respond(service.search(searchRequest))
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
        val request = call.receive<Recipe>()
        service.update(id, request)
        call.respond(HttpStatusCode.OK)
    }

    post {
        val request = call.receive<Recipe>()
        call.respond(HttpStatusCode.Created, service.create(request))
    }

    delete("{id}") {
        val id = getIdParamOrNull() ?: return@delete respondBadRequest()
        service.delete(id)
        call.respond(HttpStatusCode.NoContent)
    }
}
