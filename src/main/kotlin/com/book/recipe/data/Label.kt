package com.book.recipe.data

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object Labels : IntIdTable() {
    val name = enumerationByName("name", 100, Label::class)
}

class LabelEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<LabelEntity>(Labels)

    var name by Labels.name

    fun toLabel() = name
}

enum class Label {
    LOW_CARB, PALEO, SUGAR_FREE, GLUTEN_FREE, SWEET
}
