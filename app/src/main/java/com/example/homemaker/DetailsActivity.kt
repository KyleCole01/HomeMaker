package com.example.homemaker

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log.i
import android.view.Menu
import android.view.MenuItem
import android.widget.SeekBar
import com.example.homemaker.models.Recipe
import kotlinx.android.synthetic.main.activity_recipe_detail.*

class DetailsActivity : AppCompatActivity() {

    companion object {
        private const val IMAGE_REQUEST_CODE = 50
    }

    private var entry: Recipe =
        Recipe(Recipe.INVALID_ID)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_detail)


        val intent = intent
        entry = intent.getSerializableExtra(Recipe.TAG) as Recipe
        entry_id_label.text = "#${entry.id}"

        recipe_entry_title_et.setText(entry.title)
        recipe_entry_directions.setText(entry.directions)
        recipe_entry_ingredient_list_et.setText(entry.ingredientList)




        val imageUri = entry.getImage()
        if (imageUri != null) {
            journal_entry_image?.setImageURI(imageUri)
        }

        add_image_button.setOnClickListener {
            val imageIntent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            imageIntent.apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "image/*"
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            }
            startActivityForResult(
                imageIntent,
                IMAGE_REQUEST_CODE
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_REQUEST_CODE && data != null) {
            data.data?.let {
                entry.setImage(it)
                journal_entry_image.setImageURI(it)
            }
        }
    }

    override fun onBackPressed() {
        val resultIntent = Intent()
        entry.title = recipe_entry_title_et.text.toString()
        entry.ingredientList = recipe_entry_ingredient_list_et.text.toString()
        entry.directions = recipe_entry_directions.text.toString()
        resultIntent.putExtra(Recipe.TAG, entry)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
        super.onBackPressed()
    }
}