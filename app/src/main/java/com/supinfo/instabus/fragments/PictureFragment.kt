package com.supinfo.instabus.fragments

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.Fragment
import com.supinfo.instabus.R
import com.supinfo.instabus.activities.DetailActivity
import kotlinx.android.synthetic.main.fragment_picture.*
import kotlinx.android.synthetic.main.fragment_picture.view.*
import java.io.File


class PictureFragment : Fragment() {
    /**
    Fragment for the details of picture,
    it allows us to change the name of this one.
     **/
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val stationName = requireActivity().intent.getStringExtra("stationName")
        val folder : File? = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val actualDirectory = File(folder, stationName.toString())
        (activity as DetailActivity).existenceCheck(actualDirectory)

        // We retrieve the name of the photo and we make subparts of it
        val name = arguments?.getString("name")
        val fullName = name.toString()
        val timePart = name.toString().substring(0, 8)
        val namePart = name.toString().substring(8, name.toString().length)

        // We check if the photo has a name, if so we display it, if not we display one by default
        if(namePart.isDigitsOnly()){
            view.pictureName.text = "My Picture"
        }else{
            view.pictureName.text = namePart
        }


        view.buttonValidate.setOnClickListener{
            // When the button to change the name of the image is activated,
            // the file is renamed with a combination of the associated timeStamp
            // and the user's choice
            val oldFile = File(actualDirectory, "$fullName.jpg")
            val newName = timePart + changePictureName.text.toString() + ".jpg"
            val newFile = File(actualDirectory, newName)
            oldFile.renameTo(newFile)

            view.pictureName.text = newName.substring(8, newName.length - 4)
        }

        // The associated date is displayed that one recovers from the PicturesAdapter
        view.pictureDate.text = arguments?.getString("date")

        // We display the image which will then be clickable to return to the previous page
        val image = arguments?.getParcelable<Bitmap>("view")
        view.imageView.setImageBitmap(image)
        view.imageView.setOnClickListener{
            val activity = requireView().context as AppCompatActivity
            val detailFragment = StationsDetailFragment()
            activity.supportFragmentManager.beginTransaction()
                    .replace(R.id.detailFragment, detailFragment)
                    .addToBackStack(null).commit()
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_picture, container, false)
    }
}
