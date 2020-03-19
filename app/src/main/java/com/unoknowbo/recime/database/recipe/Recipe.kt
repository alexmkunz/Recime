package com.unoknowbo.recime.database.recipe

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class Recipe(
    var name: String,
    var cookTimeEstimateInMinutes: Int,
    var prepTimeEstimateInMinutes: Int,
    var description: String,
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0 // Optional parameter because it is auto-generated.
)