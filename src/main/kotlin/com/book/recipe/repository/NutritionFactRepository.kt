package com.book.recipe.repository

import com.book.recipe.data.NutritionFact
import com.book.recipe.data.NutritionFactEntity
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class NutritionFactRepository {

    suspend fun create(nutritionFact: NutritionFact): NutritionFactEntity = newSuspendedTransaction {
        NutritionFactEntity.new(nutritionFact.id) {
            calories = nutritionFact.calories
            fat = nutritionFact.fat
            protein = nutritionFact.protein
            carbs = nutritionFact.carbs
            sugar = nutritionFact.sugar
        }
    }

    suspend fun update(id: Int, nutritionFact: NutritionFact) = newSuspendedTransaction {
        val entity = NutritionFactEntity.findById(id)
        entity?.fromNutritionFact(nutritionFact) ?: create(nutritionFact.copy(id = id))
    }

    suspend fun delete(id: Int) = newSuspendedTransaction {
        NutritionFactEntity[id].delete()
    }

}
