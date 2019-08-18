/**
 * Based on the implementation provided in Udacity's "Developing Android Apps with Kotlin"
 */
package com.unoknowbo.recime.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.unoknowbo.recime.database.ingredient.Ingredient
import com.unoknowbo.recime.database.ingredient.IngredientDao
import com.unoknowbo.recime.database.instruction.Instruction
import com.unoknowbo.recime.database.instruction.InstructionDao
import com.unoknowbo.recime.database.recipe.Recipe
import com.unoknowbo.recime.database.recipe.RecipeDao

/**
 * A database that stores recipe, ingredient, and instruction information, with a global method to get access to the
 * database.
 */
@Database(
    entities = [Recipe::class, Ingredient::class, Instruction::class], version = 1, exportSchema = false)
abstract class RecimeDatabase : RoomDatabase() {

    /**
     * Connects the database to the DAOs.
     */
    abstract val recipeDao: RecipeDao
    abstract val ingredientDao: IngredientDao
    abstract val instructionDao: InstructionDao

    companion object {
        /**
         * The database instance, used to prevent repeated initialization of the database, is volatile so that it will
         * never be cached, and all writes and reads will be done to and from the main memory. It means that changes
         * made by one thread to shared data are visible to other threads.
         */
        @Volatile
        private var INSTANCE: RecimeDatabase? = null

        /**
         * Threadsafe helper function to get the database.
         *
         * If a database has already been retrieved, the previous database will be returned. Otherwise, create a new
         * database.
         *
         * @param context The application context Singleton, used to get access to the filesystem.
         */
        fun getInstance(context: Context): RecimeDatabase {
            // Multiple threads can ask for the database at the same time, ensure we only initialize
            // it once by using synchronized.
            synchronized(this) {
                // Copy the current value of INSTANCE to a local variable so Kotlin can smart cast.
                var instance = INSTANCE
                // If instance is `null` make a new database instance.
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        RecimeDatabase::class.java,
                        "recipe_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    // Assign INSTANCE to the newly created database.
                    INSTANCE = instance
                }
                // Return instance, which has been smart-casted to be non-null.
                return instance
            }
        }
    }
}