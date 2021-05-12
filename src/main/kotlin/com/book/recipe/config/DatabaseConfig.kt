package com.book.recipe.config

import com.book.recipe.data.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction

fun initDB() {
    Database.connect(
        url = "jdbc:h2:file:./database/RECIPE/RECIPE;AUTO_SERVER=TRUE;",
        driver = "org.h2.Driver",
        user = "sa"
    )
    createTables()
    createTestData()
}

private fun createTables() = transaction {
    transaction {
        addLogger(StdOutSqlLogger)
        SchemaUtils.create(
            Ingredients,
            NutritionFacts,
            Recipes,
            Labels,
            RecipeLabels,
            RecipeIngredients,
            RecipeRecipeIngredients
        )
    }
}

fun createTestData() {
    transaction {
        Label.values().forEach { LabelEntity.new { name = it } }
    }
}
