package com.example.noteapp.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.data.Category
import com.example.noteapp.databinding.CategoryItemBinding

class ItemsCategoryTodoAdapter(
    private val list: List<Category>, private val listener: OnItemCategoryClickListener
) : RecyclerView.Adapter<ItemsCategoryTodoAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemBinding =
            CategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val cat: Category = list[position]
        holder.bind(cat)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(private val itemBinding: CategoryItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        init {
            itemBinding.layoutCategoryItem.setOnClickListener {
                listener.onItemClick(list[bindingAdapterPosition], bindingAdapterPosition)
            }
            itemBinding.layoutCategoryItem.setOnLongClickListener {
                listener.onItemLongClick(list[bindingAdapterPosition], bindingAdapterPosition)
                true
            }
        }

        fun bind(cat: Category) {

            itemBinding.categoryNameTextView.text = cat.categoryName

            itemBinding.layoutCategoryItem.setBackgroundColor(Color.parseColor(cat.color))

            if (cat.isSelected) {
                itemBinding.layoutCategoryItem.setBackgroundColor(Color.parseColor("#F0F4D7"))
            }

            if (cat.isFavoutire) {
                itemBinding.favLayCatItem.visibility = View.VISIBLE
            } else {
                itemBinding.favLayCatItem.visibility = View.GONE
            }

            if (cat.hasPassword) {
                itemBinding.delLayCatItem.visibility = View.VISIBLE
            } else {
                itemBinding.delLayCatItem.visibility = View.GONE
            }
        }
    }
}

interface OnItemCategoryClickListener {
    fun onItemClick(category: Category, position: Int)
    fun onItemLongClick(category: Category, position: Int)
}