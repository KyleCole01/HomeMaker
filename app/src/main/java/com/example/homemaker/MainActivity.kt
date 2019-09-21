package com.example.homemaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        launch_recipe_list_button.setOnClickListener {
            startActivity(Intent(this,RecipeListActivity::class.java))
        }
    }
}
