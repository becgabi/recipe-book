package com.book.recipe.data

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object NutritionFacts : IntIdTable() {
    val calories = double("calories")
    val fat = double("fat")
    val protein = double("protein")
    val carbs = double("carbs")
    val sugar = double("sugar")
}

class NutritionFactEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<NutritionFactEntity>(NutritionFacts)

    var calories by NutritionFacts.calories
    var fat by NutritionFacts.fat
    var protein by NutritionFacts.protein
    var carbs by NutritionFacts.carbs
    var sugar by NutritionFacts.sugar

    fun toNutritionFact() = NutritionFact(id.value, calories, fat, protein, carbs, sugar)

    fun fromNutritionFact(nutritionFact: NutritionFact) {
        calories = nutritionFact.calories
        fat = nutritionFact.fat
        protein = nutritionFact.protein
        carbs = nutritionFact.carbs
        sugar = nutritionFact.sugar
    }
}

@Serializable
data class NutritionFact(
    var id: Int? = null,
    var calories: Double = 0.0,
    var fat: Double = 0.0,
    var protein: Double = 0.0,
    var carbs: Double = 0.0,
    var sugar: Double = 0.0
)
