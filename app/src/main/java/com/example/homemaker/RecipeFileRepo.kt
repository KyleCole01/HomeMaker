package com.example.homemaker

import android.content.Context
import android.os.Environment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.homemaker.models.Recipe
import org.json.JSONException
import org.json.JSONObject
import java.io.*

class RecipeFileRepo(var context: Context): RecipeRepoInterface {

    override fun updateRecipe(recipe: Recipe) {
        createRecipe(recipe)
    }

    override fun deleteRecipe(recipe: Recipe) {
        val filename = "journalRecipe${recipe.title}.json"
        if (filename.contains(filename)) {
            val deleteFile = File(storageDirectory, filename)
            try {
                deleteFile.delete()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    override fun createRecipe(recipe: Recipe) {
        val entryString = recipe.toJsonObject()
        val filename = "journalRecipe${recipe.title}.json"
        writeToFile(filename, entryString.toString())
    }

    private fun writeToFile(filename: String, entryString: String) {
        val dir = storageDirectory
        val outputFile = File(dir, filename)

        var writer: FileWriter? = null
        try {
            writer = FileWriter(outputFile)
            writer.write(entryString)
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (writer != null) {
                try {
                    writer.close()
                } catch (e2: IOException) {
                    e2.printStackTrace()
                }
            }
        }
    }

    val storageDirectory: File
        get() {
            if (isExternalStorageWriteable) {
                val directory = context.filesDir
                return if (!directory.exists() && !directory.mkdirs()) {
                    context.cacheDir
                } else {
                    directory
                }
            } else {
                return context.cacheDir
            }
        }

    val isExternalStorageWriteable: Boolean
        get() {
            val state = Environment.getExternalStorageState()
            return state == Environment.MEDIA_MOUNTED
        }

    override fun readAllRecipes(): LiveData<List<Recipe>> {
        val entries = ArrayList<Recipe>()

        for (filename in filelist) {
            val json = readFromFile(filename)
            try {
                entries.add(Recipe(JSONObject(json)))
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        val liveData = MutableLiveData<List<Recipe>>()
        liveData.postValue(entries)
        return liveData
    }

    val filelist: ArrayList<String>
        get() {
            val fileNames = arrayListOf<String>()
            val dir = storageDirectory

            val list = dir.list()
            if (list != null) {
                for (name in list) {
                    if (name.contains(".json")) {
                        fileNames.add(name)
                    }
                }
            }
            return fileNames
        }

    private fun readFromFile(filename: String): String {
        val inputFile = File(storageDirectory, filename)
        var readString: String? = null
        var reader: FileReader? = null
        try {
            reader = FileReader(inputFile)
            readString = reader.readText()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (reader != null) {
                try {
                    reader.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return readString ?: ""
    }
}
