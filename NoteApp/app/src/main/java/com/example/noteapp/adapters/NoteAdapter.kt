package com.example.noteapp.adapters

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.icu.number.NumberFormatter.with
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

class NoteAdapter(private val noteList:List<Note>, private val listener: OnItemClickListener, private val context:Context): RecyclerView.Adapter <NoteAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater:LayoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.note_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val imageNote = holder.itemView.findViewById<ImageView>(R.id.imageNote)
        holder.itemView.linear.setBackgroundColor(Color.parseColor(noteList[position].color))

        if(noteList[position].isSelected){
            holder.itemView.delLayout.visibility = View.VISIBLE
        } else{
            holder.itemView.delLayout.visibility = View.GONE
        }

        holder.itemView.note_title.text = noteList[position].title
        holder.itemView.note_message.text = noteList[position].message
/*
        if(!noteList[position].imagePaths.isEmpty()){

                Glide
                        .with(context).load(noteList[position].imagePaths.get(0)).placeholder(R.drawable.background_note).override(1000, 1000).fitCenter().centerCrop().into(imageNote)

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
