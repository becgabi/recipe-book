package com.book.recipe

import com.book.recipe.config.initDB
import com.book.recipe.config.recipeBookModule
import com.book.recipe.route.configureRouting
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.jetbrains.exposed.dao.exceptions.EntityNotFoundException
import org.koin.core.context.startKoin

fun Application.module() {
    initDB()

    install(DefaultHeaders)
    install(CallLogging)
    install(StatusPages) {
        exception<EntityNotFoundException> {
            call.respond(HttpStatusCode.NotFound)
        }
    }
    install(ContentNegotiation) {
        json()
    }

    startKoin {

        modules(recipeBookModule)
    }

    configureRouting()
}

fun main() {
    embeddedServer(Netty, port = 8080, module = Application::module).start(wait = true)
}
