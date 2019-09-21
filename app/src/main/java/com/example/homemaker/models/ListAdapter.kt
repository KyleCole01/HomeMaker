package com.example.homemaker.models

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

class ListAdapter(var entryData: ArrayList<Recipe>) :
    RecyclerView.Adapter<ListAdapter.SampleViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListAdapter.SampleViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemCount() = entryData.size

    override fun onBindViewHolder(holder: ListAdapter.SampleViewHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    inner class SampleViewHolder//bind the data members of our viewHolder to the items in the layout
        ( itemView: View) : RecyclerView.ViewHolder(itemView) {


        init {

        }
    }


}

