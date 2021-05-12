package com.book.recipe.data

fun Recipe.toSummary(): RecipeSummary {
    val sum = recipeIngredients.reduce { acc, recipeIngredient -> acc + recipeIngredient }
    return RecipeSummary(
        recipe = this,
        sumNutritionFact = sum.ingredient.nutritionFact,
        sumWeightInGram = sum.weightInGram
    )
}

operator fun RecipeIngredient.plus(other: RecipeIngredient): RecipeIngredient {
    return RecipeIngredient(
        weightInGram = weightInGram + other.weightInGram,
        ingredient = ingredient + other.ingredient
    )
}

operator fun Ingredient.plus(other: Ingredient): Ingredient {
    return Ingredient(
        nutritionFact = nutritionFact + other.nutritionFact
    )
}

operator fun NutritionFact.plus(other: NutritionFact): NutritionFact {
    return NutritionFact(
        calories = (calories + other.calories).roundTo(),
        fat = (fat + other.fat).roundTo(),
        protein = (protein + other.protein).roundTo(),
        carbs = (carbs + other.carbs).roundTo(),
        sugar = (sugar + other.sugar).roundTo()
    )
}

operator fun NutritionFact.times(times: Float): NutritionFact {
    return NutritionFact(
        calories = (calories * times).roundTo(),
        fat = (fat * times).roundTo(),
        protein = (protein * times).roundTo(),
        carbs = (carbs * times).roundTo(),
        sugar = (sugar * times).roundTo()
    )
}

fun Double.roundTo(n: Int = 2): Double {
    return "%.${n}f".format(this).toDouble()
}
