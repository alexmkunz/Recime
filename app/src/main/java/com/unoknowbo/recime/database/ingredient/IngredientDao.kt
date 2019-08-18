package com.unoknowbo.recime.database.ingredient

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

/**
 * Defines methods for using the Ingredient class with Room.
 */
@Dao
interface IngredientDao {
    @Insert
    fun insert(ingredient: Ingredient)

    @Update
    fun update(ingredient: Ingredient)

    @Query("SELECT * FROM ingredients WHERE recipeId = :recipeId")
    fun getRecipeIngredients(recipeId: Long): LiveData<List<Ingredient>>

    @Query("DELETE FROM ingredients WHERE recipeId = :recipeId AND description = :description")
    fun deleteIngredient(recipeId: Long, description: String)
}