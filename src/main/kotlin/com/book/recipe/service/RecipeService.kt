package com.book.recipe.service

import com.book.recipe.data.Recipe
import com.book.recipe.repository.RecipeRepository

class RecipeService(
    private val recipeRepository: RecipeRepository,
) {

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

}
