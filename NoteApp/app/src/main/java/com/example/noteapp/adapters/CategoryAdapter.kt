package com.example.noteapp.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.R
import com.example.noteapp.data.Category
import kotlinx.android.synthetic.main.category_item.view.*

class ItemsCategoryTodoAdapter(private val list: List<Category>, private val listener: OnItemCategoryClickListener): RecyclerView.Adapter<ItemsCategoryTodoAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater:LayoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.category_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.layout_category_item.setBackgroundColor(Color.parseColor(list[position].color))

        if(list[position].isSelected){
            holder.itemView.layout_category_item.setBackgroundColor(Color.parseColor("#F0F4D7"))
        }

        if(list[position].isFavoutire){
            holder.itemView.favLayCatItem.visibility = View.VISIBLE
        } else{
            holder.itemView.favLayCatItem.visibility = View.GONE
        }

        if(list[position].hasPassword){
            holder.itemView.delLayCatItem.visibility = View.VISIBLE
        } else{
            holder.itemView.delLayCatItem.visibility = View.GONE
        }

        holder.itemView.categoryName_textView.text = list[position].categoryName
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