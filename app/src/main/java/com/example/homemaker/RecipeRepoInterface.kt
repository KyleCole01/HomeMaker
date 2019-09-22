package com.example.homemaker

import androidx.lifecycle.LiveData
import com.example.homemaker.models.Recipe

interface RecipeRepoInterface {
    fun createRecipe(recipe: Recipe)
    fun readAllRecipes(): LiveData<List<Recipe>>
    fun updateRecipe(recipe: Recipe)
    fun deleteRecipe(recipe: Recipe)
}