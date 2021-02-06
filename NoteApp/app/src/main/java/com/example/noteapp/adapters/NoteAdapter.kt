package com.example.noteapp.adapters

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.icu.number.NumberFormatter.with
import android.opengl.Visibility
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Glide.with
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.noteapp.R
import com.example.noteapp.data.Note
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.note_item.view.*

class NoteAdapter(private val noteList:List<Note>, private val listener: OnItemClickListener): RecyclerView.Adapter <NoteAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater:LayoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.note_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val imageNote = holder.itemView.findViewById<ImageView>(R.id.imageNote)
        holder.itemView.linear.setBackgroundColor(Color.parseColor(noteList[position].color))


            //Log.d("pps", "${noteList[position].isSelected}")

        if(noteList[position].isSelected){
            holder.itemView.linear.setBackgroundColor(Color.parseColor("#F0F4D7"))
        }

        /*
        else {
            holder.itemView.delLayout.visibility = View.GONE
        }

         */

        if(noteList[position].isFavourite){
            holder.itemView.favLayItem.visibility = View.VISIBLE
            holder.itemView.itemsLayout.visibility = View.VISIBLE
        } else{
            holder.itemView.favLayItem.visibility = View.GONE
        }

        if (noteList[position].hasPassword){
            holder.itemView.delLayItem.visibility = View.VISIBLE
            holder.itemView.itemsLayout.visibility = View.VISIBLE
        } else{
            holder.itemView.delLayItem.visibility = View.GONE
        }

        if(!noteList[position].hasPassword&&!noteList[position].isFavourite){
            holder.itemView.itemsLayout.visibility = View.GONE
        }

        holder.itemView.note_title.text = noteList[position].title
        holder.itemView.note_message.text = noteList[position].message

        /*
        if(!noteList[position].imagePaths.isEmpty()){

                Glide
                        .with(context).load(noteList[position].imagePaths.get(noteList[position].imagePaths.size-1)).placeholder(R.drawable.background_note)
                        .override(1000, 1000).fitCenter().centerCrop().into(imageNote)
                holder.itemView.imageNote.visibility = View.VISIBLE

        } else{
            imageNote.visibility = View.GONE
        }
        */

    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    inner class MyViewHolder(view:View):RecyclerView.ViewHolder(view) {
        init {
            view.setOnClickListener{
                listener.onItemClick(noteList[adapterPosition], adapterPosition)
            }
            view.setOnLongClickListener{
                listener.onItemLongClick(noteList[adapterPosition], adapterPosition)
                true
            }
        }
    }
}

interface OnItemClickListener{
    fun onItemClick(note: Note, position:Int)
    fun onItemLongClick(note: Note, position: Int)
}
