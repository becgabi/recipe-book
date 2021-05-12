package com.book.recipe.repository

import com.book.recipe.data.*
import org.jetbrains.exposed.sql.SizedCollection
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class RecipeRepository {

    suspend fun findAll(): List<Recipe> = newSuspendedTransaction {
        RecipeEntity.all().map(RecipeEntity::toRecipe)
    }

    suspend fun getOne(id: Int): Recipe = newSuspendedTransaction {
        RecipeEntity[id].toRecipe()
    }

    suspend fun findById(id: Int): Recipe? = newSuspendedTransaction {
        RecipeEntity.findById(id)?.toRecipe()
    }

    suspend fun create(recipe: Recipe) = newSuspendedTransaction {
        RecipeEntity.new(recipe.id) {
            name = recipe.name
            description = recipe.description
            portion = recipe.portion
        }.also {
            it.recipeIngredients = createRecipeIngredients(recipe)
            it.labels = getLabels(recipe)
        }.toRecipe()
    }

    suspend fun update(id: Int, recipe: Recipe) = newSuspendedTransaction {
        val oldIngredientIds = getOne(id).recipeIngredients.map { requireNotNull(it.id) }
        RecipeEntity[id].fromRecipe(
            recipe,
            getLabels(recipe),
            createRecipeIngredients(recipe)
        )
        oldIngredientIds.forEach { RecipeIngredientEntity[it].delete() }
    }

    suspend fun delete(id: Int) = newSuspendedTransaction {
        val current = getOne(id)
        update(id, current.copy(labels = emptyList(), recipeIngredients = emptyList()))
        RecipeEntity[id].delete()
    }

    private fun getLabels(recipe: Recipe): SizedCollection<LabelEntity> {
        return SizedCollection(recipe.labels.flatMap { LabelEntity.find { Labels.name eq it } })
    }

    private fun createRecipeIngredients(recipe: Recipe): SizedCollection<RecipeIngredientEntity> {
        return SizedCollection(recipe.recipeIngredients.map { recipeIngredient ->
            RecipeIngredientEntity.new(recipeIngredient.id) {
                amountInGram = recipeIngredient.amountInGram
                ingredientId = recipeIngredient.ingredientId
            }
        })
    }

}
