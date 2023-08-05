package com.example.noteapp.ui.fragments.note

import android.content.Context
import android.view.LayoutInflater
import android.view.RoundedCorner
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.noteapp.R
import com.example.noteapp.databinding.ImageItemBinding

class CustomImageAdapter(var images: List<String>, var context: Context, val onImageClickListener: ImageClickListener) :
    RecyclerView.Adapter<CustomImageAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val viewBinding =
            ImageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ImageViewHolder(viewBinding)
    }

    override fun getItemCount(): Int {
        return images.count()
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(images[position], )
    }


    inner class ImageViewHolder(private val viewBinding: ImageItemBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {

        fun bind(item: String) {
            Glide.with(context)
                .load(item)
                .transform(CenterCrop(), RoundedCorners(24))
                .placeholder(R.drawable.image_placeholder)
                .into(viewBinding.imageItemIv)

            viewBinding.imageItemDeleteIv.setOnClickListener {
                onImageClickListener.onImageClick(item, bindingAdapterPosition)
            }
        }
    }
}

interface ImageClickListener {
    fun onImageClick(item: String, imagePosition: Int)
}