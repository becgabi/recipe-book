package com.book.recipe.data

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.SizedCollection
import org.jetbrains.exposed.sql.Table

object Recipes : IntIdTable() {
    val name = varchar("name", 100)
    val description = varchar("description", 1000)
    val portion = integer("portion")
}

class RecipeEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<RecipeEntity>(Recipes)

    var name by Recipes.name
    var description by Recipes.description
    var portion by Recipes.portion
    var labels by LabelEntity via RecipeLabels
    var recipeIngredients by RecipeIngredientEntity via RecipeRecipeIngredients

    fun toRecipe() = Recipe(
        id = id.value,
        name = name,
        description = description,
        portion = portion,
        labels = labels.map { it.toLabel() },
        recipeIngredients = recipeIngredients.map { it.toRecipeIngredient() }
    )

    fun fromRecipe(
        recipe: Recipe,
        labelEntities: SizedCollection<LabelEntity>,
        recipeIngredientEntities: SizedCollection<RecipeIngredientEntity>
    ) {
        name = recipe.name
        name = recipe.name
        description = recipe.description
        portion = recipe.portion
        labels = labelEntities
        recipeIngredients = recipeIngredientEntities
    }
}

@Serializable
data class Recipe(
    val id: Int? = null,
    val name: String,
    val description: String,
    val portion: Int,
    val labels: List<Label>,
    val recipeIngredients: List<RecipeIngredient>
)

object RecipeLabels : Table() {
    val recipe = reference("recipe", Recipes)
    val label = reference("label", Labels)
    override val primaryKey = PrimaryKey(recipe, label)
}

object RecipeRecipeIngredients : Table() {
    val recipe = reference("recipe", Recipes)
    val recipeIngredient = reference("recipe_ingredient", RecipeIngredients)
    override val primaryKey = PrimaryKey(recipe, recipeIngredient)
}

@Serializable
class SearchRequest(
    val query: String = "",
    val labels: List<Label> = emptyList()
)
