package com.example.homemaker.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.homemaker.models.Recipe
import com.example.homemaker.repo

class RecipeViewModel : ViewModel() {

    // TODO 25: Create a LiveData object for the entries
    val entries: LiveData<List<Recipe>> by lazy {
        readAllRecipes()
    }

    // TODO 26: Recreate the repo calls to as functions here.
    fun readAllRecipes() : LiveData<List<Recipe>> {
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