package com.example.homemaker

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.homemaker.entity.RecipeBook.Companion.createRecipe
import com.example.homemaker.models.Recipe
import com.example.homemaker.viewmodel.RecipeViewModel

import kotlinx.android.synthetic.main.activity_recipe_list.*
import kotlinx.android.synthetic.main.content_recipe_list.*
import java.lang.ref.WeakReference

class RecipeListActivity : AppCompatActivity() {

    companion object {
        const val NEW_ENTRY_REQUEST = 2
        const val EDIT_ENTRY_REQUEST = 1
    }

    lateinit var viewModel: RecipeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_list)
        setSupportActionBar(toolbar)

        viewModel = ViewModelProviders.of(this).get(RecipeViewModel::class.java)

        fab.setOnClickListener { _ ->
            val intent = Intent(this@RecipeListActivity, DetailsActivity::class.java)
            val entry = createRecipe()
            intent.putExtra(Recipe.TAG, entry)
            startActivityForResult(
                intent,
                NEW_ENTRY_REQUEST
            )
        }



        // TODO 17: Replace the call here by observing LiveData from the ViewModel
        //entryList = repo.readAllEntries()
        ReadAllAsyncTask(this).execute()
    }


    // TODO 22: Extract update functionality
    private fun updateForRecipes(recipes: List<Recipe>) {
        listLayout.removeAllViews()
        recipes.forEach { entry ->
            listLayout.addView(createEntryView(entry))
        }
    }




    private fun createEntryView(recipe: Recipe): TextView {
        val view = TextView(this@RecipeListActivity)

        view.text = getString(R.string.entry_label, recipe.id, recipe.title)

        view.setPadding(15, 15, 15, 15)
        view.textSize = 22f

        view.setOnClickListener {
            val viewDetailIntent = Intent(this@RecipeListActivity, DetailsActivity::class.java)
            viewDetailIntent.putExtra(Recipe.TAG, recipe)
            startActivityForResult(
                viewDetailIntent,
                EDIT_ENTRY_REQUEST
            )
        }
        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            if (requestCode == NEW_ENTRY_REQUEST) {
                if (data != null) {
                    val entry = data.getSerializableExtra(Recipe.TAG) as Recipe
                    //entryList.add(entry)
                    CreateAsyncTask(viewModel).execute(entry)
                    //repo.createEntry(entry) // TODO 16a: Notice the call here
                }
            } else if (requestCode == EDIT_ENTRY_REQUEST) {
                if (data != null) {
                    val entry = data.getSerializableExtra(Recipe.TAG) as Recipe
                    //entryList[entry.id] = entry
                    UpdateAsyncTask(viewModel).execute(entry)
                    //repo.updateEntry(entry) // TODO 16b. Notice the call here
                }
            }
        }
    }

    // TODO 19: Create AsyncTasks
    class CreateAsyncTask(viewModel: RecipeViewModel) : AsyncTask<Recipe, Void, Unit>() {
        private val viewModel = WeakReference(viewModel)
        override fun doInBackground(vararg entries: Recipe?) {
            if (entries.isNotEmpty()) {
                entries[0]?.let {
                    viewModel.get()?.createRecipe(it)
                }
            }
        }
    }

    // TODO 20: Create AsyncTasks
    class UpdateAsyncTask(viewModel: RecipeViewModel) : AsyncTask<Recipe, Void, Unit>() {
        private val viewModel = WeakReference(viewModel)
        override fun doInBackground(vararg entries: Recipe?) {
            if (entries.isNotEmpty()) {
                entries[0]?.let {
                    viewModel.get()?.updateRecipe(it)
                }
            }
        }
    }

    // TODO 21: Create AsyncTasks
    class ReadAllAsyncTask(activity: RecipeListActivity) : AsyncTask<Void, Void, LiveData<List<Recipe>>?>() {

        private val activity = WeakReference(activity)

        override fun doInBackground(vararg entries: Void?): LiveData<List<Recipe>>? {
            return activity.get()?.viewModel?.entries
        }

        override fun onPostExecute(result: LiveData<List<Recipe>>?) {
            activity.get()?.let { act ->
                result?.let { entries ->
                    // TODO 27: Observe LiveData here
                    entries.observe(act,
                        Observer<List<Recipe>> { t ->
                            t?.let {
                                act.updateForRecipes(t)
                            }
                        }
                    )
                }
            }
        }
    }

}
