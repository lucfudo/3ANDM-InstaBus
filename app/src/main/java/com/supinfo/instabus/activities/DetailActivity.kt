package com.supinfo.instabus.activities

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.google.android.gms.common.util.IOUtils.toByteArray
import com.supinfo.instabus.R
import com.supinfo.instabus.REQUEST_CODE
import com.supinfo.instabus.fragments.PictureFragment
import com.supinfo.instabus.fragments.StationsDetailFragment
import kotlinx.android.synthetic.main.activity_detail.*
import java.io.File
import java.io.IOException
import java.util.*

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class DetailActivity : AppCompatActivity(), Communicator{
    /**
    The secondary activity of the application,
    it is here that we will find the photos taken in this or that station.
     **/

    @Throws(IOException::class)
    private fun createImageFile(timeStamp: String, actualDirectory: File) : File{
        return File.createTempFile(
                timeStamp, /* prefix */
                ".jpg", /* suffix */
                actualDirectory /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            absolutePath
        }
    }

    fun dispatchTakePictureIntent(timeStamp: String, actualDirectory: File) {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile(timeStamp, actualDirectory)
                } catch (ex: IOException) {
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                            this,
                            "com.supinfo.instabus.fileprovider",
                            it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_CODE)
                }
            }
        }


    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // After having taken a photo,
        // we restart the activity with the adjournment of this last one
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            reloadActivity()
        }
    }

    fun existenceCheck(actualDirectory: File) {
        // Function checking if the folder associated with the station exists,
        // if not creates it and restarts the activity
        if (!actualDirectory.exists()){
            actualDirectory.mkdir()
            finish()
            startActivity(intent)
        }
    }

    fun imageReader(root: File): ArrayList<File> {
        // Function returning the list of all .jpg files in directory
        val filesList: ArrayList<File> = ArrayList()
        if (root.exists()) {
            val actual = root.listFiles()
            if (actual.isNotEmpty()) {
                for (i in actual.indices) {
                    if (actual[i].name.endsWith(".jpg")) {
                        filesList.add(actual[i])
                    }
                }
            }
        }
        return filesList
    }

    fun reloadActivity(){
        finish()
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // Display of the name of the selected station
        val stationName = intent.getStringExtra("stationName")
        detail_stationName.text = stationName
        // Button to return to the main activity
        button_backToMain.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val detailFragment = StationsDetailFragment()
        supportFragmentManager.beginTransaction().replace(R.id.detailFragment, detailFragment).commit()
    }

    override fun passDataCom(position: Int, pictureDate: TextView, view: ImageView, pictureList: ArrayList<File>) {
        // This function is the function of the Communicator interface allows to pass data between
        // "StationsDetailFragment" to "PictureFragment"
        val bundle = Bundle()
        bundle.putString("date", pictureDate.text.toString())
        bundle.putParcelable("view", BitmapFactory.decodeFile(pictureList[position].absolutePath))
        bundle.putString("name", pictureList[position].nameWithoutExtension)

        val transaction = this.supportFragmentManager.beginTransaction()
        val pictureFragment = PictureFragment()
        pictureFragment.arguments = bundle

        transaction.replace(R.id.detailFragment, pictureFragment)
        transaction.commit()
    }

}

interface Communicator{
    // Interface that allow us to communicate between fragments
    fun passDataCom(position: Int, pictureDate: TextView, view: ImageView, pictureList: ArrayList<File>)
}
