package com.example.homemaker.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.homemaker.models.Recipe

@Database(entities = [Recipe::class], version = 1, exportSchema = false)
abstract class RecipeDB : RoomDatabase() {
    abstract fun RecipeDao(): RecipeDAO
}
