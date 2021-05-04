package com.book.recipe.repository

import com.book.recipe.data.Ingredient
import com.book.recipe.data.IngredientEntity
import com.book.recipe.data.Ingredients
import com.book.recipe.data.NutritionFactEntity
import org.jetbrains.exposed.sql.lowerCase
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class IngredientRepository {

    suspend fun search(query: String): List<Ingredient> = newSuspendedTransaction {
        IngredientEntity
            .find { Ingredients.name.lowerCase() like "%${query.toLowerCase()}%" }
            .map { it.toIngredient() }
            .toList()
    }

    suspend fun findAll(): List<Ingredient> = newSuspendedTransaction {
        IngredientEntity.all().map(IngredientEntity::toIngredient)
    }

    suspend fun getOne(id: Int): Ingredient = newSuspendedTransaction {
        IngredientEntity[id].toIngredient()
    }

    suspend fun findById(id: Int): Ingredient? = newSuspendedTransaction {
        IngredientEntity.findById(id)?.toIngredient()
    }

    suspend fun create(ingredient: Ingredient, nutritionFactEntity: NutritionFactEntity) = newSuspendedTransaction {
        IngredientEntity.new(ingredient.id) {
            name = ingredient.name
            nutritionFact = nutritionFactEntity
        }.toIngredient()
    }

    suspend fun update(id: Int, ingredient: Ingredient) = newSuspendedTransaction {
        IngredientEntity[id].fromIngredient(ingredient)
    }

    suspend fun delete(id: Int) = newSuspendedTransaction {
        IngredientEntity[id].delete()
    }

}
