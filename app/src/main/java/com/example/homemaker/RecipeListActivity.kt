package com.example.homemaker

import android.content.Intent
import android.media.Image
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.homemaker.entity.RecipeBook.Companion.createRecipe
import com.example.homemaker.models.Recipe
import com.example.homemaker.viewmodel.RecipeViewModel
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso

import kotlinx.android.synthetic.main.activity_recipe_list.*
import kotlinx.android.synthetic.main.content_recipe_list.*
import java.io.File
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

        ReadAllAsyncTask(this).execute()
    }

    private fun updateForRecipes(recipes: List<Recipe>) {
        listLayout.removeAllViews()
        recipes.forEach { entry ->
            listLayout.addView(createEntryView(entry))
        }
    }

    private fun createEntryView(recipe: Recipe): LinearLayout {
        val linearLayout:LinearLayout = LinearLayout(this)
        linearLayout.orientation = LinearLayout.HORIZONTAL

        val view = TextView(this@RecipeListActivity)
        view.text = getString(R.string.entry_label, recipe.updatedId, recipe.title)
        view.setPadding(15, 15, 15, 15)
        view.textSize = 22f




        val imageView= ImageView(this)
        val directory = File(this.filesDir, "imageDir")
        val file = File(directory,"${recipe.title}.png")

        Picasso.get().load(file).memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE).resize(100,100).into(imageView)
        linearLayout.addView(imageView)
        linearLayout.addView(view)
        if(recipe.isfavorite){
            val favView:ImageView = ImageView(this)
            favView.setImageDrawable(getDrawable(R.drawable.ic_star_black_24dp))
            linearLayout.addView(favView)
        }
        linearLayout.setOnClickListener {
            val viewDetailIntent = Intent(this@RecipeListActivity, DetailsActivity::class.java)
            viewDetailIntent.putExtra(Recipe.TAG, recipe)
            startActivityForResult(
                viewDetailIntent,
                EDIT_ENTRY_REQUEST
            )
        }
        linearLayout.setOnLongClickListener {
            DeleteAsyncTask(viewModel).execute(recipe)
            val directory = File(this.filesDir, "imageDir")
            val file = File(directory,"${recipe.title}.png")
            if(file.exists()){
                file.delete()
            }
            true

        }

        return linearLayout
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == NEW_ENTRY_REQUEST) {
                if (data != null) {
                    val entry = data.getSerializableExtra(Recipe.TAG) as Recipe
                    CreateAsyncTask(viewModel).execute(entry)
                }
            } else if (requestCode == EDIT_ENTRY_REQUEST) {
                if (data != null) {
                    val entry = data.getSerializableExtra(Recipe.TAG) as Recipe
                    UpdateAsyncTask(viewModel).execute(entry)
                }
            }
        }
    }

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
    class DeleteAsyncTask(viewModel: RecipeViewModel) : AsyncTask<Recipe, Void, Unit>() {
        private val viewModel = WeakReference(viewModel)
        override fun doInBackground(vararg entries: Recipe?) {
            if (entries.isNotEmpty()) {
                entries[0]?.let {
                    viewModel.get()?.deleteRecipe(it)
                }
            }
        }
    }

    class ReadAllAsyncTask(activity: RecipeListActivity) : AsyncTask<Void, Void, LiveData<List<Recipe>>?>() {
        private val activity = WeakReference(activity)
        override fun doInBackground(vararg entries: Void?): LiveData<List<Recipe>>? {
            return activity.get()?.viewModel?.entries
        }
        override fun onPostExecute(result: LiveData<List<Recipe>>?) {
            activity.get()?.let { act ->
                result?.let { entries ->
                    entries.observe(act,
                        Observer<List<Recipe>> { t ->
                            var i = 1
                            t.forEach {
                                it.updatedId = i++
                            }
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
