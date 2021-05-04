package com.book.recipe.config

import com.book.recipe.data.Ingredients
import com.book.recipe.data.NutritionFacts
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction

fun initDB() {
    Database.connect(
        "jdbc:h2:file:/Users/gaborbeckl/Projects/BME/2nd Semester/recipe-book/database/RECIPE/RECIPE;AUTO_SERVER=TRUE;",
        driver = "org.h2.Driver",
        user = "sa"
    )
    createTables()
}

private fun createTables() = transaction {
    transaction {
        addLogger(StdOutSqlLogger)
        SchemaUtils.create(Ingredients, NutritionFacts)
    }
}
