package com.example.noteapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.noteapp.R
import com.example.noteapp.adapters.ViewPagerAdapter
import com.example.noteapp.viewmodels.NotesViewModel
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment() {

    private lateinit var viewModel:NotesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("addda", "MainFragment onCreate")
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[NotesViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setUpTabs()

        searchIcon.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                findNavController().navigate(R.id.action_mainFramgent_to_searchFragment)
            }
        })
    }

    private fun setUpTabs(){
        Log.d("abcd", "isCreate")
        val adapter = ViewPagerAdapter(childFragmentManager)
        adapter.addFragment(NoteFragment(), "Notes")
        adapter.addFragment(TodoListFragment(), "ToDo List")

        viewPager.adapter = adapter
        tabsLayout.setupWithViewPager(viewPager)
    }
}