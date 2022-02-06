package com.example.noteapp.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.R
import com.example.noteapp.data.Category
import com.example.noteapp.databinding.CategoryItemBinding
import makeGone
import makeVisible

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
            itemBinding.categoryItemLl.setOnClickListener {
                listener.onItemClick(list[bindingAdapterPosition], bindingAdapterPosition)
            }
            itemBinding.categoryItemLl.setOnLongClickListener {
                listener.onItemLongClick(list[bindingAdapterPosition], bindingAdapterPosition)
                true
            }
        }

        fun bind(cat: Category) {

            itemBinding.categoryItemTitleTv.text = cat.categoryName

            itemBinding.categoryItemLl.setBackgroundColor(Color.parseColor(cat.color))

            if (cat.isSelected) {
                itemBinding.categoryItemLl.setBackgroundResource(R.drawable.background_edit)
            }

            if (cat.isFavoutire) {
                itemBinding.categoryItemFavouriteIv.makeVisible()
            } else {
                itemBinding.categoryItemFavouriteIv.makeGone()
            }

            if (cat.hasPassword) {
                itemBinding.categoryItemLockIv.makeVisible()
            } else {
                itemBinding.categoryItemLockIv.makeGone()
            }
        }
    }
}

interface OnItemCategoryClickListener {
    fun onItemClick(category: Category, position: Int)
    fun onItemLongClick(category: Category, position: Int)
}