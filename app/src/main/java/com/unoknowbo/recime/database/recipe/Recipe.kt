package com.unoknowbo.recime.database.recipe

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class Recipe(
    var name: String,
    var cookTimeInMinutes: Int,
    var prepTimeInMinutes: Int,
    var notes: String,
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0 // Optional parameter because it is auto-generated.
)