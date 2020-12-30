package com.example.noteapp.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.R
import com.example.noteapp.data.Category
import kotlinx.android.synthetic.main.note_item.view.*
import kotlinx.android.synthetic.main.todo_item.view.*

class ItemsCategoryTodoAdapter(private val list: List<Category>, private val listener: OnItemCategoryClickListener): RecyclerView.Adapter<ItemsCategoryTodoAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater:LayoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.todo_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.layout_todo_item.setBackgroundColor(Color.parseColor(list[position].color))

        if(list[position].isSelected){
            holder.itemView.layout_todo_item.setBackgroundColor(Color.parseColor("#F0F4D7"))
        }

        holder.itemView.todo_categoryName.text = list[position].categoryName
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(view:View):RecyclerView.ViewHolder(view){
        init {
            view.setOnClickListener{
                listener.onItemClick(list[adapterPosition], adapterPosition)
            }
            view.setOnLongClickListener{
                listener.onItemLongClick(list[adapterPosition], adapterPosition)
                true
            }
        }
    }
}

interface OnItemCategoryClickListener{
    fun onItemClick(category: Category, position:Int)
    fun onItemLongClick(category: Category, position: Int)
}