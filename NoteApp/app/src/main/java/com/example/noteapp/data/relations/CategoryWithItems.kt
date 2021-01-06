package com.example.noteapp.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.noteapp.data.Category
import com.example.noteapp.data.ItemOfList

data class CategoryWithItems(
        @Embedded val category: Category,
        @Relation(
                parentColumn = "rowIdCategory",
                entityColumn = "categoryName"
        )
        val items:List<ItemOfList>
        )
