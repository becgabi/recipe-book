package com.book.recipe.service

import com.book.recipe.data.Recipe
import com.book.recipe.data.SearchRequest
import com.book.recipe.repository.RecipeRepository

class RecipeService(
    private val recipeRepository: RecipeRepository,
) {

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

    suspend fun getPortion(id: Int, portionNumber: Float): Recipe {
        val recipe = recipeRepository.getOne(id)
        val ingredients = recipe.recipeIngredients.map { recipeIngredient ->
            val newAmountInGram = (recipeIngredient.amountInGram * portionNumber / recipe.portion).toInt()
            recipeIngredient.copy(amountInGram = newAmountInGram)
        }
        return recipe.copy(portion = portionNumber, recipeIngredients = ingredients)
    }

}
