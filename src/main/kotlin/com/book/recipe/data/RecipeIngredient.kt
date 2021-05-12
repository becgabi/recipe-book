package com.book.recipe.data

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object RecipeIngredients : IntIdTable() {
    val weightInGram = integer("weight_in_gram")
    val ingredient = reference("ingredient", Ingredients)
}

class RecipeIngredientEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<RecipeIngredientEntity>(RecipeIngredients)

    var weightInGram by RecipeIngredients.weightInGram
    var ingredient by IngredientEntity referencedOn RecipeIngredients.ingredient

    fun toRecipeIngredient() = RecipeIngredient(id.value, weightInGram, ingredient.toIngredient())
}

@Serializable
data class RecipeIngredient(
    val id: Int? = null,
    val weightInGram: Int,
    val ingredient: Ingredient
)
