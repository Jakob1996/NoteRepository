package com.example.noteapp.viewmodels

import android.content.Context
import androidx.appcompat.app.AppCompatActivity

class SharePreferences(context: Context) {

    private val prefs = context.applicationContext.getSharedPreferences(
        SHARED_PREFERENCES_KEY, AppCompatActivity.MODE_PRIVATE
    )

    fun loadSortDescendingData(): Boolean {
        return prefs.getBoolean(SORT_DATA_KEY, false)
    }

    fun saveSortDescendingData(isDescending: Boolean) {
        val editor = prefs.edit()
        editor.putBoolean(SORT_DATA_KEY, isDescending).apply()
    }

    companion object {
        private const val SHARED_PREFERENCES_KEY = "SHARED_PREFERENCES_KEY"
        private const val SORT_DATA_KEY = "SORT_DATA_KEY"
    }
}