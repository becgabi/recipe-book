package com.book.recipe.service

import com.book.recipe.data.Ingredient
import com.book.recipe.data.NutritionFact
import com.book.recipe.util.httpRequest
import kotlinx.serialization.Serializable

class SpoonacularService {

    companion object {
        private const val BASE_URL = "https://api.spoonacular.com"
        private const val API_KEY = "f18a7eaa37db42dfac2e0f1bd430d230"
        private const val RESPONSE_LIMIT = 2
    }

    suspend fun search(query: String): List<Ingredient> {
        return findIngredients(query)
            .map { ingredient -> getMetaInfo(ingredient.id) }
            .map { mapToInternalIngredient(it) }
    }

    private suspend fun getMetaInfo(id: Int): SpoonacularIngredient {
        return httpRequest(
            url = "$BASE_URL/food/ingredients/$id/information",
            params = mapOf(
                "apiKey" to API_KEY,
                "amount" to 100,
                "unit" to "grams"
            )
        )
    }

    private suspend fun findIngredients(query: String): List<SpoonacularIngredient> {
        return httpRequest(
            url = "$BASE_URL/food/ingredients/autocomplete",
            params = mapOf(
                "apiKey" to API_KEY,
                "query" to query,
                "number" to RESPONSE_LIMIT,
                "metaInformation" to true
            )
        )
    }

    private fun mapToInternalIngredient(ingredient: SpoonacularIngredient): Ingredient {
        val nutritionFact = NutritionFact().apply {
            ingredient.nutrition?.nutrients?.forEach { nutrient ->
                when (nutrient.name) {
                    "Calories" -> calories = nutrient.amount
                    "Fat" -> fat = nutrient.amount
                    "Carbohydrates" -> carbs = nutrient.amount
                    "Sugar" -> sugar = nutrient.amount
                    "Protein" -> protein = nutrient.amount
                }
            }
        }
        return Ingredient(name = ingredient.name, nutritionFact = nutritionFact)
    }

}

@Serializable
data class SpoonacularIngredient(
    val id: Int,
    val name: String,
    val nutrition: Nutrition? = null
)

@Serializable
data class Nutrition(
    val nutrients: List<Nutrient>
)

@Serializable
data class Nutrient(
    val name: String,
    val amount: Double,
)
