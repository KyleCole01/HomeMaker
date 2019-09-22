package com.example.homemaker.database

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import javax.security.auth.callback.Callback


 fun storeImage(
    bitmap: Bitmap?,
    recipeId: String, context: Context
) {
    Thread(Runnable {
        val directory = File(context.getFilesDir(), "imageDir")
        if (!directory.exists()) {
            directory.mkdir()
        }
        val myPath = File(directory, "$recipeId.png")
        if(myPath.exists()){
            myPath.delete()
        }
        val fos: FileOutputStream
        try {
            fos = FileOutputStream(myPath)
            bitmap?.compress(Bitmap.CompressFormat.PNG, 100, fos)
            fos.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }).start()
}
 interface UriPathCallback {
    fun onUriPathReceived(path: String)
}