package com.example.homemaker

import androidx.lifecycle.LiveData
import com.example.homemaker.models.Recipe

interface RecipeRepoInterface {
    fun createRecipe(recipe: Recipe)
    fun readAllRecipes(): LiveData<MutableList<Recipe>>
    fun updateRecipe(recipe: Recipe)
    fun deleteRecipe(recipe: Recipe)
}