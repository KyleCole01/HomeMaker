package com.example.homemaker

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.homemaker.entity.RecipeBook.Companion.createRecipe
import com.example.homemaker.models.Recipe
import com.example.homemaker.viewmodel.RecipeViewModel
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_recipe_list.*
import kotlinx.android.synthetic.main.content_recipe_list.*
import kotlinx.android.synthetic.main.recycler_view_layout.view.*
import java.io.File
import java.lang.ref.WeakReference

class RecipeListActivity : AppCompatActivity() {
    lateinit var  rAdapter:RecipeListAdapter
    var type:String = ""
    var list = mutableListOf<Recipe>()
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
        rAdapter = RecipeListAdapter(list)
        //adding recyclerview
        rv_view.apply {
            layoutManager = LinearLayoutManager(this@RecipeListActivity)
            setHasFixedSize(true)
            adapter = rAdapter

        }
        viewModel.readAllRecipes().observe(this, Observer {
            list.clear()


            list.addAll(it)
            rAdapter.notifyDataSetChanged()

        })
        //button.onclicklistener {




        fab.setOnClickListener {
            val intent = Intent(this@RecipeListActivity, DetailsActivity::class.java)
            val entry = createRecipe()
            intent.putExtra(Recipe.TAG, entry)
            /*startActivityForResult(
                intent,
                NEW_ENTRY_REQUEST
            )*/
            viewModel.entries.observe(this, Observer {
                list.clear()
                list.addAll(it)
                rAdapter.notifyDataSetChanged()

            })
        }
    }

/*
    private fun updateForRecipes(recipes: List<Recipe>) {
        listLayout.removeAllViews()
        recipes.forEach { entry ->
            listLayout.addView(createEntryView(entry))
        }
    }
*/

/*
    private fun createEntryView(recipe: Recipe): LinearLayout {
        val linearLayout = LinearLayout(this)
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
            val favView = ImageView(this)
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
*/

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

 inner class RecipeListAdapter(val recipeList: MutableList<Recipe>) : RecyclerView.Adapter<RecipeListAdapter.ViewHolder>() {
     override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
         return ViewHolder(
             LayoutInflater.from(parent.context)
                 .inflate(R.layout.recycler_view_layout, parent, false) as View
         )
     }
     override fun getItemCount() = recipeList.size


     override fun onBindViewHolder(holder: ViewHolder, position: Int) {
         val recipe = recipeList[position]
         holder.bindModel(recipe,this@RecipeListActivity)

         holder.layoutParent.setOnClickListener {
             val intent = Intent(this@RecipeListActivity, DetailsActivity::class.java)
             intent.putExtra(Recipe.TAG,recipe)
             this@RecipeListActivity.startActivityForResult(intent, EDIT_ENTRY_REQUEST)
         }
         holder.layoutParent.setOnLongClickListener{
            DeleteAsyncTask(viewModel).execute(recipe)
             true
         }

     }

      inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
         val recipeImageView: ImageView = view.layout_img_view
         val recipeTitleView: TextView = view.layout_title_view
         val layoutParent: ConstraintLayout = view.layout_parent
         val favView = view.layout_fav_view

         fun bindModel(recipe: Recipe,context: Context) {
             val directory = File(context.filesDir, "imageDir")
             val file = File(directory,"${recipe.title}.png")

             Picasso.get().load(file).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).resize(100,100).into(recipeImageView)
             recipeTitleView.text = recipe.title
             if (recipe.isfavorite)
                 favView.setImageDrawable(ContextCompat.getDrawable(itemView.context,R.drawable.ic_star_black_24dp))
             else
                 favView.setImageDrawable(ContextCompat.getDrawable(itemView.context,R.drawable.ic_star_border_black_24dp))

         }
     }

 }



}