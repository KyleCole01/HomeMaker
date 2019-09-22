package com.example.homemaker.entity

import com.example.homemaker.models.Recipe

class RecipeBook {

    companion object {

        fun createRecipe(): Recipe {
            return Recipe(0)
        }

        fun createRecipe(text: String): Recipe {
            val entry = createRecipe()
            entry.title = text
            return entry
        }
    }
}

