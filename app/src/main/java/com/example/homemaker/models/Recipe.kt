package com.example.homemaker.models

import android.net.Uri
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import org.json.JSONException
import org.json.JSONObject
import java.io.Serializable
import java.util.*

@Entity
class Recipe : Serializable {

    companion object {
        const val TAG = "JournalEntry"
        // TODO 8: This must be 0 if we want autoGenerate to work
        const val INVALID_ID = -1
    }

    var title: String? = null
    @Ignore
    var updatedId: Int = 0
    var ingredientList: String? = null
    var image: String? = null
    var directions: String? = null
    var filePath:String? = null
    var isfavorite:Boolean = false

    // TODO 7: Let's make id the primary key
    @PrimaryKey(autoGenerate = true) @NonNull
    var id: Int = 0

    constructor(id: Int) {
        this.id = id
        this.title = ""
        this.ingredientList = ""
        this.directions = ""
        this.image = ""

    }

    constructor(jsonObject: JSONObject) {
        try {
            this.title = jsonObject.getString("title")
        } catch (e: JSONException) {
            this.title = (Date().time / 1000).toString()
        }
        try {
            this.ingredientList = jsonObject.getString("ingredientList")
        } catch (e: JSONException) {
            this.ingredientList = ""
        }
        try {
            this.image = jsonObject.getString("image")
        } catch (e: JSONException) {
            this.image = ""
        }
        try {
            this.directions = jsonObject.getString("directions")
        } catch (e: JSONException) {
            this.directions = ""
        }
        try {
            this.id = jsonObject.getInt("id")
        } catch (e: JSONException) {
            this.id = -1
        }
    }

    fun toJsonObject(): JSONObject? {
        try {
            return JSONObject().apply {
                put("title", title)
                put("ingredientList", ingredientList)
                put("image", image)
                put("directions", directions)
                put("id", id)
            }
        } catch (e1: JSONException) {
            return try {
                JSONObject("{\"title\" : \"$title\", \"ingredientList\" : \"$ingredientList\", \"image\": \"$image\", \"directions\": $directions, \"id\": $id}")
            } catch (e2: JSONException) {
                e2.printStackTrace()
                return null
            }
        }

    }

    constructor(csvString: String) {
        val values = csvString.split(",")
        // check to see if we have the right string
        if (values.size == 5) {
            // handle missing numbers or strings in the number position
            try {
                this.id = Integer.parseInt(values[0])
            } catch (e: NumberFormatException) {
                e.printStackTrace()
            }

            this.title = values[1]
            try {
                this.directions = (values[2])
            } catch (e: NumberFormatException) {
                e.printStackTrace()
            }

            this.ingredientList = values[3].replace("~@", ",")
            this.image = if (values[4] == "unused") "" else values[4]
        }
    }

    internal fun toCsvString(): String {
        return String.format(
            "%d,%s,%d,%s,%s",
            id,
            title,
            directions,
            ingredientList?.replace(",", "~@"),
            if (image === "") "unused" else image
        )
    }

    fun getImage(): Uri? {
        return if (image != "") {
            Uri.parse(image)
        } else {
            null
        }
    }

    fun setImage(imageUri: Uri) {
        this.image = imageUri.toString()
    }

    override fun toString(): String {
        return "JournalEntry(title=$title, ingredientList=$ingredientList, image=$image, directions=$directions, id=$id)"
    }
}