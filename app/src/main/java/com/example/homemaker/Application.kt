package com.example.homemaker

import android.app.Application
import com.example.homemaker.database.RecipeDBRepo

val repo: RecipeRepoInterface by lazy {
    App.repo!!
}



class App : Application() {

    companion object {
        var repo: RecipeRepoInterface? = null
    }

    override fun onCreate() {
        super.onCreate()

        //repo = Prefs(applicationContext)
        //repo = JournalFileRepo(applicationContext)

        // TODO 18: Instantiate the DB repo here instead
        repo = RecipeDBRepo(applicationContext)

    }
}