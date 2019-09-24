package com.example.homemaker.viewmodel

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.homemaker.R
import com.example.homemaker.models.Recipe
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recycler_view_layout.view.*
import java.io.File

class ListAdapter(val recipeList: MutableList<Recipe>) : RecyclerView.Adapter<ListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_view_layout, parent, false) as View
        )
    }
    override fun getItemCount() = recipeList.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipe = recipeList[position]
        val context:Context = holder.itemView.context
        holder.bindModel(recipe,context)

        holder.layoutParent.setOnClickListener {
            recipe.isfavorite = !recipe.isfavorite
            notifyItemChanged(position)
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val recipeImageView: ImageView = view.layout_img_view
        val recipeTitleView: TextView = view.layout_title_view
        val layoutParent: ConstraintLayout = view.layout_parent
        val favView = view.layout_fav_view

            fun bindModel(recipe: Recipe,context:Context) {
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