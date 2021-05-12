package com.book.recipe.config

import com.book.recipe.repository.IngredientRepository
import com.book.recipe.repository.NutritionFactRepository
import com.book.recipe.repository.RecipeRepository
import com.book.recipe.service.IngredientService
import com.book.recipe.service.RecipeService
import com.book.recipe.service.SpoonacularService
import org.koin.dsl.module

val recipeBookModule = module {
    single { NutritionFactRepository() }
    single { IngredientRepository() }
    single { RecipeRepository() }
    single { SpoonacularService() }
    single { RecipeService(get()) }
    single { IngredientService(get(), get(), get()) }
}
