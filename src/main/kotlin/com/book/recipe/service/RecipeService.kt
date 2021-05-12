package com.book.recipe.service

import com.book.recipe.data.*
import com.book.recipe.repository.RecipeRepository

class RecipeService(
    private val recipeRepository: RecipeRepository,
) {

    companion object {
        private const val NUTRIENT_VALUE_PER_GRAM = 100F
    }

    suspend fun search(request: SearchRequest): List<Recipe> {
        return recipeRepository.search(request).sortedBy { it.name }
    }

    suspend fun findAll(): List<Recipe> {
        return recipeRepository.findAll()
    }

    suspend fun getOne(id: Int): Recipe {
        return recipeRepository.getOne(id)
    }

    suspend fun create(recipe: Recipe): Recipe {
        return recipeRepository.create(recipe)
    }

    suspend fun delete(id: Int) {
        recipeRepository.delete(id)
    }

    suspend fun update(id: Int, recipe: Recipe) {
        val original = recipeRepository.findById(id)

        if (original != null) {
            recipeRepository.update(id, recipe)
        } else {
            create(recipe.copy(id = id))
        }
    }

    suspend fun getPortion(id: Int, portionNumber: Float): RecipeSummary {
        val recipe = recipeRepository.getOne(id)
        val ingredients = recipe.recipeIngredients.map { recipeIngredient ->
            calculateNutrition(recipeIngredient, portionNumber, recipe)
        }
        return recipe.copy(portion = portionNumber, recipeIngredients = ingredients).toSummary()
    }

    private fun calculateNutrition(
        recipeIngredient: RecipeIngredient,
        portionNumber: Float,
        recipe: Recipe
    ): RecipeIngredient {
        val newWeightInGram = (recipeIngredient.weightInGram * portionNumber / recipe.portion).toInt()
        val newNutritionFact = recipeIngredient.ingredient.nutritionFact * (newWeightInGram / NUTRIENT_VALUE_PER_GRAM)
        return recipeIngredient.copy(
            weightInGram = newWeightInGram,
            ingredient = recipeIngredient.ingredient.copy(nutritionFact = newNutritionFact)
        )
    }

}
