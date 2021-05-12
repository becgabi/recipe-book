package com.book.recipe.data

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object RecipeIngredients : IntIdTable() {
    val amountInGram = integer("amount_in_gram")
    val ingredientId = integer("ingredient_id")
}

class RecipeIngredientEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<RecipeIngredientEntity>(RecipeIngredients)

    var amountInGram by RecipeIngredients.amountInGram
    var ingredientId by RecipeIngredients.ingredientId

    fun toRecipeIngredient() = RecipeIngredient(id.value, amountInGram, ingredientId)
}

@Serializable
data class RecipeIngredient(
    val id: Int? = null,
    val amountInGram: Int,
    val ingredientId: Int
)
