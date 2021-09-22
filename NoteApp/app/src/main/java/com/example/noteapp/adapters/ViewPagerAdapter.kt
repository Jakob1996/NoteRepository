package com.example.noteapp.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.noteapp.ui.fragments.note.NoteListFragment
import com.example.noteapp.ui.fragments.todo.CategoryFragment

class ViewPagerAdapter(supportFragmentManager:FragmentManager) : FragmentPagerAdapter(supportFragmentManager ) {

    private val mFragmentList = ArrayList<Fragment>()
    private val mFragmentTitleList = ArrayList<String>()

    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {
        when(position){
            0->{
                return NoteListFragment()
            }

            1-> {
                return CategoryFragment()
            }

            else -> { return NoteListFragment()}
        }
    }

    fun addFragment(fragment:Fragment, title:String){
        mFragmentList.add(fragment)
        mFragmentTitleList.add(title)
    }


    override fun getPageTitle(position: Int): CharSequence? {
        when(position){
            0 -> {return "Notes"}
            1 -> {return "ToDo List"}
        }
        return super.getPageTitle(position)
    }
}