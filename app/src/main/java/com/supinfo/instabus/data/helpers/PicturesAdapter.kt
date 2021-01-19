package com.supinfo.instabus.data.helpers

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.supinfo.instabus.R
import java.io.File

class PicturesAdapter(
        private val picturesList: ArrayList<File>,
        private val listener: OnItemClickListener):
        RecyclerView.Adapter<PicturesAdapter.ViewHolder>(){
    /**
    Class to incorporate the data into pictures_recyclerView,
    for that our data will take the form defined in the layout "pictures_grid".
    (we will see the photos accompanied by their date of creation).
     **/

    inner class ViewHolder(itemView : View) :RecyclerView.ViewHolder(itemView), View.OnClickListener{
            var view: ImageView = itemView.findViewById(R.id.pictures_imageFrame)
            var date: TextView = itemView.findViewById(R.id.picture_textDate)

            init{
                itemView.setOnClickListener(this)
            }

            override fun onClick(p0: View?) {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(position, date, view, picturesList)
                }
            }

    }

    override fun getItemCount(): Int = picturesList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PicturesAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.pictures_grid, parent,false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val picture = picturesList[position]
        with(holder){

            view.setImageBitmap(BitmapFactory.decodeFile(picture.absolutePath))

            date.let{
                // We reformat the date (we could have chosen another method)
                it.text =
                        picture.name.substring(0, 4) + "-" +
                        picture.name.substring(4, 6) + "-" +
                        picture.name.substring(6, 8)
                it.contentDescription = picture.name
            }
        }
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int, pictureDate: TextView, view: ImageView, picturesList: ArrayList<File>)
    }

}