package com.book.recipe.service

import com.book.recipe.data.Ingredient
import com.book.recipe.repository.IngredientRepository
import com.book.recipe.repository.NutritionFactRepository

class IngredientService(
    private val ingredientRepository: IngredientRepository,
    private val nutritionFactRepository: NutritionFactRepository,
    private val spoonacularService: SpoonacularService
) {

    suspend fun search(query: String): List<Ingredient> {
        val externalIngredients = spoonacularService.search(query)
        val internalIngredients = ingredientRepository.search(query)
        return (externalIngredients + internalIngredients).sortedBy { it.name }
    }

    suspend fun findAll(): List<Ingredient> {
        return ingredientRepository.findAll()
    }

    suspend fun getOne(id: Int): Ingredient {
        return ingredientRepository.getOne(id)
    }

    suspend fun create(ingredient: Ingredient): Ingredient {
        val nutritionFactEntity = nutritionFactRepository.create(ingredient.nutritionFact)
        return ingredientRepository.create(ingredient, nutritionFactEntity)
    }

    suspend fun delete(id: Int) {
        val nutritionFactId = requireNotNull(getOne(id).nutritionFact.id)
        ingredientRepository.delete(id)
        nutritionFactRepository.delete(nutritionFactId)
    }

    suspend fun update(id: Int, ingredient: Ingredient) {
        val original = ingredientRepository.findById(id)

        if (original != null) {
            ingredientRepository.update(id, ingredient)
            nutritionFactRepository.update(original.nutritionFact.id!!, ingredient.nutritionFact)
        } else {
            create(ingredient.copy(id = id))
        }
    }

}
