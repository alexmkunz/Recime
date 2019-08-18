package com.unoknowbo.recime.database.recipe

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

/**
 * Defines methods for using the Recipe class with Room.
 */
@Dao
interface RecipeDao {
    @Insert
    fun insert(recipe: Recipe)

    @Update
    fun update(recipe: Recipe)

    @Query("SELECT * FROM recipes ORDER BY id ASC")
    fun getRecipes(): LiveData<List<Recipe>>

    @Query("DELETE FROM recipes WHERE id = :id")
    fun deleteRecipe(id: Long)

    @Query("DELETE FROM recipes")
    fun clear()
}