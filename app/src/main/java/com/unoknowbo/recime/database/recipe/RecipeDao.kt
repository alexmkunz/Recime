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
    fun insert(recipe: Recipe): Long // Returns the id of the newly inserted Recipe

    @Update
    fun update(recipe: Recipe)

    @Query("UPDATE recipes SET name=:name WHERE id = :id")
    fun updateName(name: String, id: Long)

    @Query("UPDATE recipes SET prepTimeEstimateInMinutes=:prepTimeEstimateInMinutes WHERE id = :id")
    fun updatePrepTime(prepTimeEstimateInMinutes: Int, id: Long)

    @Query("UPDATE recipes SET cookTimeEstimateInMinutes=:cookTimeEstimateInMinutes WHERE id = :id")
    fun updateCookTime(cookTimeEstimateInMinutes: Int, id: Long)

    @Query("UPDATE recipes SET servings=:servings WHERE id = :id")
    fun updateServings(servings: Int, id: Long)

    @Query("UPDATE recipes SET calories=:calories WHERE id = :id")
    fun updateCalories(calories: Int, id: Long)

    @Query("UPDATE recipes SET description=:description WHERE id = :id")
    fun updateDescription(description: String, id: Long)

    @Query("SELECT * FROM recipes WHERE name = :name")
    fun getRecipeByName(name: String): Recipe?

    @Query("SELECT * FROM recipes WHERE id = :id")
    fun getRecipe(id: Long): LiveData<Recipe>

    @Query("SELECT * FROM recipes ORDER BY id ASC")
    fun getRecipes(): LiveData<List<Recipe>>

    @Query("DELETE FROM recipes WHERE id = :id")
    fun deleteRecipe(id: Long)

    @Query("DELETE FROM recipes")
    fun clear()
}