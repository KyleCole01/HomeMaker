package com.example.homemaker.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.example.homemaker.RecipeRepoInterface
import com.example.homemaker.models.Recipe

class RecipeDBRepo(val context: Context) : RecipeRepoInterface {

    override fun createRecipe(recipe: Recipe) {
        database.RecipeDao().createRecipe(recipe)
    }

    override fun readAllRecipes(): LiveData<MutableList<Recipe>> {
        return database.RecipeDao().readAllRecipes()
    }

    override fun updateRecipe(recipe: Recipe) {
        database.RecipeDao().updateRecipe(recipe)
    }

    override fun deleteRecipe(recipe: Recipe) {
        database.RecipeDao().deleteRecipe(recipe)
    }

    private val database/**/ by lazy {
        Room.databaseBuilder(
            context.applicationContext,
            RecipeDB::class.java, "entry_database"
        ).fallbackToDestructiveMigration().build()
    }
}