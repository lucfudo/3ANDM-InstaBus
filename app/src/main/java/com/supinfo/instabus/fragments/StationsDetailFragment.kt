package com.supinfo.instabus.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.supinfo.instabus.R
import com.supinfo.instabus.REQUEST_CODE
import com.supinfo.instabus.activities.Communicator
import com.supinfo.instabus.activities.DetailActivity
import com.supinfo.instabus.data.helpers.PicturesAdapter
import com.supinfo.instabus.data.helpers.SwipeToDelete
import kotlinx.android.synthetic.main.fragment_stations_detail.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class StationsDetailFragment : Fragment() , PicturesAdapter.OnItemClickListener{
    /**
    Fragment for the details of stations,
    it allows us to take and display photos.
     **/
    private lateinit var communicator: Communicator

    @SuppressLint("SimpleDateFormat")
    val timeStamp: String = SimpleDateFormat("yyyyMMdd").format(Date())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // We retrieve the name of the selected station from the main activity,
        // and create a folder in the external files directory
        val stationName = requireActivity().intent.getStringExtra("stationName")
        val folder : File? = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val actualDirectory = File(folder, stationName.toString())
        (activity as DetailActivity).existenceCheck(actualDirectory)

        // Pressing the "+" button verifies that the application has the rights to use the camera
        // and calls up the function that creates the intent to take pictures
        button_takePicture.setOnClickListener {
            if(context?.let { it1 -> ActivityCompat.checkSelfPermission(
                            it1,
                            Manifest.permission.CAMERA
                    ) }
                    == PackageManager.PERMISSION_DENIED ||
                    context?.let { it1 -> checkSelfPermission(
                            it1,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) }
                    == PackageManager.PERMISSION_DENIED){
                val permission = arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                requestPermissions(permission, REQUEST_CODE)
            }else{
                (activity as DetailActivity).dispatchTakePictureIntent(timeStamp, actualDirectory)
            }
        }

        // Value including the list of all images in the current folder
        val picturesList = (activity as DetailActivity).imageReader(actualDirectory)
        // We call on our RecyclerView
        pictures_recyclerView.adapter = PicturesAdapter(picturesList, this)
        pictures_recyclerView.layoutManager = StaggeredGridLayoutManager(
                2,
                StaggeredGridLayoutManager.VERTICAL
        )
        pictures_recyclerView.setHasFixedSize(true)
        // Finally, we have the possibility to delete a photo by doing a swipe to delete which
        // will refresh the page
        val swipeToDeleteCallback = object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val pos = viewHolder.adapterPosition
                if(picturesList[pos].absoluteFile.exists())
                    picturesList[pos].absoluteFile.delete()
                picturesList.removeAt(pos)
                (activity as DetailActivity).reloadActivity()
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(pictures_recyclerView)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_stations_detail, container, false)
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        // The function to check/manage the authorization to access the camera
        val stationName = activity?.intent?.getStringExtra("stationName")
        val folder : File? = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val actualDirectory = File(folder, stationName.toString())

        when(requestCode){
            REQUEST_CODE ->
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    (activity as DetailActivity).dispatchTakePictureIntent(
                            timeStamp,
                            actualDirectory
                    )
                } else {
                    Toast.makeText(activity, "Permission denied", Toast.LENGTH_LONG).show()
                }
        }
    }

    override fun onItemClick(position: Int, pictureDate: TextView, view: ImageView, picturesList: ArrayList<File>) {
        communicator = activity as Communicator
        communicator.passDataCom(position, pictureDate, view, picturesList)
    }
}