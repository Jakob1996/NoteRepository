package com.example.noteapp.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.data.Note
import com.example.noteapp.databinding.NoteItemBinding
import com.example.noteapp.tools.OnItemClickListener
import fadeIn
import fadeOut
import makeGone
import makeVisible

class NoteAdapter(private val noteList: List<Note>, private val listener: OnItemClickListener) :
    RecyclerView.Adapter<NoteAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val viewBinding =
            NoteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = noteList[position]

        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    inner class MyViewHolder(private val viewBinding: NoteItemBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        init {
            viewBinding.linear.setOnClickListener {
                listener.onItemClick(noteList[bindingAdapterPosition], bindingAdapterPosition)
            }
            viewBinding.linear.setOnLongClickListener {
                listener.onItemLongClick(noteList[bindingAdapterPosition], bindingAdapterPosition)
                true
            }
        }

        @SuppressLint("Range")
        fun bind(note: Note) {
            viewBinding.linear.setBackgroundColor(Color.parseColor(note.color))

            if (note.isSelected) {
                viewBinding.linear.setBackgroundColor(Color.parseColor("#F0F4D7"))
            }

            if (note.isFavourite) {
                viewBinding.favLayItem.makeVisible()
                viewBinding.itemsLayout.makeVisible()
            } else {
                viewBinding.favLayItem.makeGone()
            }

            if (note.hasPassword) {
                viewBinding.delLayItem.makeVisible()
                viewBinding.itemsLayout.makeVisible()
            } else {
                viewBinding.delLayItem.makeGone()
            }

            if (!note.hasPassword && !note.isFavourite) {
                viewBinding.itemsLayout.makeGone()
            }

            viewBinding.noteTitle.text = note.title
            viewBinding.noteMessage.text = note.message
        }
    }
}
