package com.book.recipe.data

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object Ingredients : IntIdTable() {
    val name = varchar("name", 100)
    val nutritionFact = reference("nutrition_fact", NutritionFacts)
}

class IngredientEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<IngredientEntity>(Ingredients)

    var name by Ingredients.name
    var nutritionFact by NutritionFactEntity referencedOn Ingredients.nutritionFact

    fun toIngredient() = Ingredient(id.value, name, nutritionFact.toNutritionFact())

    fun fromIngredient(ingredient: Ingredient) {
        name = ingredient.name
    }
}

@Serializable
data class Ingredient(
    val id: Int? = null,
    val name: String,
    val nutritionFact: NutritionFact
)
