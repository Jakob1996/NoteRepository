package com.example.noteapp.ui.interfaces

interface ToolbarAction {

    fun setupToolbar(
        backToTitle: String = "",
        titleVisible: Boolean,
        backBtnVisible: Boolean,
        favouriteBtnVisible: Boolean = false,
        multiBtnVisible: Boolean = false,
        editBtnVisible: Boolean = false,
        doneBtnVisible: Boolean = false
    )

    fun onBackBtnPressed(action: () -> Unit)
    fun onFavouriteBtnPressed(action: () -> Unit)
    fun onSearchBtnPressed(action: () -> Unit)
    fun onDoneBtnPressed(action: () -> Unit)
    fun onEditBtnPressed(action: () -> Unit)

    fun setToolbarTitle(title: String)

    fun changeToolbarMultiSelectIcon(icon: Int)

    fun disableMultiSelectMode()
    fun enableMultiSelectMode()
}