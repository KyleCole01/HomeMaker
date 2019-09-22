package com.example.homemaker

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.homemaker.models.Recipe

class Prefs(context: Context): RecipeRepoInterface {
    companion object {
        private const val JOURNAL_PREFERENCES = "JournalPreferences"

        private const val ID_LIST_KEY = "id_list"
        private const val ENTRY_ITEM_KEY_PREFIX = "entry_"
        private const val NEXT_ID_KEY = "next_id"

        private const val BACKGROUND_COLOR = "background_color"  // and example
    }

    val sharedPrefs: SharedPreferences =
        context.getSharedPreferences(JOURNAL_PREFERENCES, Context.MODE_PRIVATE)

    // create a new entry
    override fun createRecipe(recipe: Recipe) {
        // read list of entry ids
        val ids = getListOfIds()

        if (recipe.id == Recipe.INVALID_ID && !ids.contains(recipe.id.toString())) {
            // new entry
            val editor = sharedPrefs.edit()

            var nextId = sharedPrefs.getInt(NEXT_ID_KEY, 0)
            recipe.id = nextId
            // store updated next id
            editor.putInt(NEXT_ID_KEY, ++nextId)

            // add id to list of ids

            ids.add(recipe.id.toString())
            // store updated id list
            val newIdList = StringBuilder()
            for (id in ids) {
                newIdList.append(id).append(",")
            }

            editor.putString(ID_LIST_KEY, newIdList.toString())

            // store new entry
            editor.putString(ENTRY_ITEM_KEY_PREFIX + recipe.id, recipe.toCsvString())
            editor.apply()
        } else {
            updateRecipe(recipe)
        }
    }

    private fun getListOfIds(): java.util.ArrayList<String> {
        val idList = sharedPrefs.getString(ID_LIST_KEY, "")
        val oldList = idList!!.split(",")

        val ids = ArrayList<String>(oldList.size)
        if (idList.isNotBlank()) {
            ids.addAll(oldList)
        }
        return ids
    }

    // read an existing entry
    private fun readRecipe(id: Int): Recipe? {
        val entryCsv = sharedPrefs.getString(ENTRY_ITEM_KEY_PREFIX + id, "invalid")!!
        return if (entryCsv != "invalid") {
            Recipe(entryCsv)
        } else {
            null
        }
    }

    // read all entries
    override fun readAllRecipes(): LiveData<List<Recipe>> {
        // read list of entry ids
        val listOfIds = getListOfIds()

        // step through that list and read each entry
        val entryList = java.util.ArrayList<Recipe>()
        for (id in listOfIds) {
            if (id.isNotBlank()) {
                readRecipe(id.toInt())?.let {
                    entryList.add(it)
                }
            }
        }

        val liveData = MutableLiveData<List<Recipe>>()
        liveData.postValue(entryList)
        return liveData
    }


    // In Activity, can simply use: repo.bgColor (to get and set)
    var bgColor: Int
        get() = sharedPrefs.getInt(BACKGROUND_COLOR, Color.BLACK)
        set(value) = sharedPrefs.edit().putInt(BACKGROUND_COLOR, value).apply()


    // edit an existing entry
    override fun updateRecipe(recipe: Recipe) {
        val editor = sharedPrefs.edit()
        editor.putString(ENTRY_ITEM_KEY_PREFIX + recipe.id, recipe.toCsvString())
        editor.apply()
    }

    override fun deleteRecipe(recipe: Recipe) {
        val editor = sharedPrefs.edit()
        editor.remove(ENTRY_ITEM_KEY_PREFIX + recipe.id)
        editor.apply()
    }
}