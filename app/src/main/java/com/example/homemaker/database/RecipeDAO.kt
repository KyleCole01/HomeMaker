package com.example.homemaker.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.homemaker.models.Recipe

@Dao
interface RecipeDAO {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun createRecipe(recipe: Recipe)

    @Query("SELECT * FROM recipe")
    fun readAllRecipes(): LiveData<MutableList<Recipe>>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateRecipe(recipe: Recipe)

    @Delete
    fun deleteRecipe(recipe: Recipe)
}