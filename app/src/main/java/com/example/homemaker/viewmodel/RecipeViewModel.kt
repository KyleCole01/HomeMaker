package com.example.homemaker.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.homemaker.models.Recipe
import com.example.homemaker.repo

class RecipeViewModel : ViewModel() {

    val entries: LiveData<MutableList<Recipe>> by lazy {
        readAllRecipes()
    }

    fun readAllRecipes() : LiveData<MutableList<Recipe>> {
        return repo.readAllRecipes()
    }
    fun createRecipe(recipe: Recipe) {
        repo.createRecipe(recipe)
    }
    fun updateRecipe(recipe: Recipe) {
        repo.updateRecipe(recipe)
    }
    fun deleteRecipe(recipe:Recipe){
        repo.deleteRecipe(recipe)
    }
}