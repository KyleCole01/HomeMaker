package com.example.homemaker.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.homemaker.models.Recipe

@Dao
interface RecipeDAO {

    // TODO 10: Insert with onConflict = REPLACE
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun createRecipe(recipe: Recipe)

    // TODO 11: Query for all entities
    @Query("SELECT * FROM recipe")
    fun readAllRecipes(): LiveData<List<Recipe>> // TODO 27: Return LiveData for VM

    // TODO 12: Update with onConflict = REPLACE
    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateRecipe(recipe: Recipe)

    // TODO 13: DELETE
    @Delete
    fun deleteRecipe(recipe: Recipe)
}
