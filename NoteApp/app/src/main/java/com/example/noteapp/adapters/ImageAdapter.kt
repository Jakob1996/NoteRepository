package com.example.noteapp.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.noteapp.R

class ImageAdapter(private val imageList: ArrayList<String>, val context: Context):RecyclerView.Adapter<ImageAdapter.MyViewImage>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewImage {
        val layoutInflater:LayoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.image_item, parent, false)
        return MyViewImage(view)
    }

    override fun onBindViewHolder(holder: MyViewImage, position: Int) {
        val imageNote = holder.itemView.findViewById<ImageView>(R.id.imageN)

       Glide.with(context).load(imageList[position]).override(500, 500).fitCenter().centerCrop().into(imageNote)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    inner class MyViewImage(view:View):RecyclerView.ViewHolder(view){

    }
}