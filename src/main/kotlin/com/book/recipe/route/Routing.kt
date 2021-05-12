package com.book.recipe.route

import io.ktor.application.*
import io.ktor.routing.*

fun Application.configureRouting() {

    routing {
        registerIngredientRoutes()
        registerRecipeRoutes()
    }
}
