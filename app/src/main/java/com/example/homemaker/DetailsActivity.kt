package com.example.homemaker

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import com.example.homemaker.database.storeImage
import com.example.homemaker.models.Recipe
import kotlinx.android.synthetic.main.activity_recipe_detail.*
import java.io.File
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso


class DetailsActivity : AppCompatActivity() {

    companion object {
        private const val IMAGE_REQUEST_CODE = 50
    }
    var haspic:Boolean = false
    private var entry: Recipe =
        Recipe(Recipe.INVALID_ID)
    private var entryBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_detail)


        val intent = intent
        entry = intent.getSerializableExtra(Recipe.TAG) as Recipe
        entry_id_label.text = "#${entry.id}"

        recipe_entry_title_et.setText(entry.title)
        recipe_entry_directions.setText(entry.directions)
        recipe_entry_ingredient_list_et.setText(entry.ingredientList)
        if(entry.isfavorite){
            fav_img_view.setImageDrawable(getDrawable(R.drawable.ic_star_black_24dp))
        }else{
            fav_img_view.setImageDrawable(getDrawable(R.drawable.ic_star_border_black_24dp))
        }
            fav_img_view.setOnClickListener {
                entry.isfavorite = !entry.isfavorite
                if(entry.isfavorite){
                    fav_img_view.setImageDrawable(getDrawable(R.drawable.ic_star_black_24dp))
                }else{
                    fav_img_view.setImageDrawable(getDrawable(R.drawable.ic_star_border_black_24dp))
                }
            }

        val directory = File(this@DetailsActivity.filesDir, "imageDir")
        if(directory.exists()){
                val file = File(directory,"${entry.title}.png")
                Picasso.get()
                    .load(file).memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE)
                    .resize(750,500)
                    .into(recipe_image)

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
                entryBitmap = MediaStore.Images.Media.getBitmap(this@DetailsActivity.contentResolver, it)
                entry.setImage(it)
                recipe_image.setImageURI(it)
                haspic = true
                val directory = File(this.filesDir, "imageDir")
                val file = File(directory,"${entry.title}.png")
                if(file.exists()){
                    file.delete()
                }

            }
        }
    }
    override fun onBackPressed() {
        val resultIntent = Intent()
        entry.title = recipe_entry_title_et.text.toString()
        entry.ingredientList = recipe_entry_ingredient_list_et.text.toString()
        entry.directions = recipe_entry_directions.text.toString()

        if(haspic) {
                    storeImage(entryBitmap, entry.title!!, this@DetailsActivity)
                    }
        resultIntent.putExtra(Recipe.TAG, entry)
        this@DetailsActivity.setResult(Activity.RESULT_OK, resultIntent)
        this@DetailsActivity.finish()


        }



    }