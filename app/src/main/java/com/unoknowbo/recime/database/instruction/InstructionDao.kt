package com.unoknowbo.recime.database.instruction

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

/**
 * Defines methods for using the Instruction class with Room.
 */
@Dao
interface InstructionDao {
    @Insert
    fun insert(instruction: Instruction)

    @Update
    fun update(instruction: Instruction)

    @Query("SELECT * FROM instructions WHERE recipeId = :recipeId ORDER BY orderOfInstruction ASC")
    fun getRecipeInstructions(recipeId: Long): LiveData<List<Instruction>>

    @Query("DELETE FROM instructions WHERE recipeId = :recipeId AND description = :description")
    fun deleteInstruction(recipeId: Long, description: String)
}