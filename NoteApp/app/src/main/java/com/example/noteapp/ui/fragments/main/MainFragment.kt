package com.example.noteapp.ui.fragments.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.example.noteapp.R
import com.example.noteapp.adapters.ViewPagerAdapter
import com.example.noteapp.ui.fragments.note.NoteFragment
import com.example.noteapp.ui.fragments.todo.CategoryFragment
import com.example.noteapp.ui.fragments.todo.DialogAddCategoryItem
import com.example.noteapp.ui.fragments.todo.DialogAddToDoFragment
import com.example.noteapp.viewmodels.ViewModel
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment() : Fragment(){
    private lateinit var viewModel:ViewModel
    private val request_code = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("abcd", "MainFragment onCreate")
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[ViewModel::class.java]

        /*
        if(viewModel.multiSelectMode){
            searchIcon.setImageIcon(Icon.createWithResource(requireContext(), R.drawable.ic_baseline_delete))
        }
        */
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("abcd", "MainFragment onCreateView")
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("abcd", "MainFragment onViewCreated")

        /*
        addNote_FB.setOnClickListener {
            if(viewModel.multiSelectMode) {
                viewModel.delete(viewModel.selectedNotes.toList())
                Toast.makeText(requireContext(), "Notes deleted", Toast.LENGTH_LONG).show()
            } else{
                viewModel.setSelectedNote(null)
                findNavController().navigate(R.id.action_mainFramgent_to_addEditNoteFragment)
            }
        }

        sortDate_FB.setOnClickListener {
            val sortDialogFragment = SortDialogFragment()
            sortDialogFragment.setTargetFragment(this, request_code)
            sortDialogFragment.show(parentFragmentManager, "SortDialogFragment")
        }
        */
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d("abcd", "MainFragment onActivityCreated")

        setUpTabs()

        if(viewPager.currentItem == 0){
            viewModel.getMultiSelectNote().observe(viewLifecycleOwner, Observer {
                if(it == false){
                    addNote_FB.labelText = "Add Note"
                } else {
                    addNote_FB.labelText = "Delete Notes"
                }
            })
        } else {
            viewModel.getMultiSelectCategoryMode().observe(viewLifecycleOwner, Observer {
                if(it == false){
                    addNote_FB.labelText = "Add Category"
                } else{
                    addNote_FB.labelText = "Delete Category"
                }
            })
        }


        viewModel.getMultiSelectNote().observe(viewLifecycleOwner, Observer {
            if(viewPager.currentItem==0&&it==false){
                addNote_FB.labelText = "Add Note"
            } else if(viewPager.currentItem==0&& it == true){
                addNote_FB.labelText = "Delete Notes"
            }
        })

        viewModel.getMultiSelectCategoryMode().observe(viewLifecycleOwner, Observer {
            if(viewPager.currentItem ==1 && it == false){
                addNote_FB.labelText = "Add Category"
            } else if(viewPager.currentItem == 1 && it == true){
                addNote_FB.labelText = "Delete Category"
            }
        })

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                if(position==0){
                    if(viewModel.getMultiSelectNote().value==true) {
                        addNote_FB.labelText = "Delete Notes"
                    }else {
                        addNote_FB.labelText = "Add Note"
                    }
                } else if (position==1){
                    if(viewModel.getMultiSelectCategoryMode().value == true){
                        addNote_FB.labelText = "Delete Category"
                    } else {
                        addNote_FB.labelText = "Add Category"
                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })

            searchIcon.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    findNavController().navigate(R.id.action_mainFramgent_to_searchFragment)
                }
            })

        addNote_FB.setOnClickListener(View.OnClickListener {
            if(addNote_FB.labelText == "Add Note"){
                viewModel.newNote = true
                findNavController().navigate(R.id.action_mainFramgent_to_addEditNoteFragment)
            }else if(addNote_FB.labelText == "Add Category"){
                val fm = requireActivity().supportFragmentManager
                val dialogFragment = DialogAddCategoryItem()
                dialogFragment.show(fm, "Abc")

            } else if(addNote_FB.labelText == "Delete Notes"){
                viewModel.deleteNotes(viewModel.selectedNotes)
                viewModel.setMutliSelectNote(false)
                addNote_FB.labelText="Add Note"
            } else if(addNote_FB.labelText == "Delete Category"){
                viewModel.selectedCategoryItems.forEach { viewModel.deleteItems(it.rowIdCategory) }
                viewModel.deleteCategotyItems(viewModel.selectedCategoryItems)
                viewModel.setMutliSelectCategoryMode(false)
            }
        })
    }

    private fun setUpTabs(){
        val adapter = ViewPagerAdapter(childFragmentManager)
        adapter.addFragment(NoteFragment(), "Notes")
        adapter.addFragment(CategoryFragment(), "ToDo List")

        viewPager.adapter = adapter
        tabsLayout.setupWithViewPager(viewPager)
    }
}