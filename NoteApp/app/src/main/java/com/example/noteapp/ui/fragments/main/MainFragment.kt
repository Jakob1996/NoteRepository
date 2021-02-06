package com.example.noteapp.ui.fragments.main

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.example.noteapp.R
import com.example.noteapp.adapters.ViewPagerAdapter
import com.example.noteapp.ui.fragments.note.NoteFragment
import com.example.noteapp.ui.fragments.sort.SortDialogFragment
import com.example.noteapp.ui.fragments.todo.CategoryFragment
import com.example.noteapp.ui.fragments.todo.DialogAddCategoryItem
import com.example.noteapp.viewmodels.ProfilViewModel
import com.example.noteapp.viewmodels.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.menu_drawer2.*
import kotlinx.android.synthetic.main.note_edit_layout_miscellaneous.*

class MainFragment() : Fragment(){
    private lateinit var viewModel:ViewModel
    private lateinit var profileViewModel: ProfilViewModel
    private val request_code = 123
    private val fbAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("abcd", "MainFragment onCreate")
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[ViewModel::class.java]

        requireActivity().onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                Log.d("abccc", "${viewModel.getMultiSelectMode().value}")
                if(viewModel.getMultiSelectMode().value == true){
                    viewModel.setMutliSelectMode(false)
                    viewModel.setNotifyDataNote(true)
                    viewModel.setNotifyDataCategory(true)
                } else{
                    isEnabled = false
                    requireActivity().onBackPressed()
                }
            }
        })
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

        viewModel.getFabButtonMode().observe(viewLifecycleOwner, Observer {
            if(it==true){
                floatingActionMenu.visibility = View.GONE
                fav_FB.setColorFilter(Color.parseColor("#FDBE3B"))
            } else{
                floatingActionMenu.visibility = View.VISIBLE
                fav_FB.setColorFilter(Color.WHITE)
            }
        })

        fav_FB.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                if(viewModel.getFabButtonMode().value==true){
                    viewModel.setFabButtonMode(false)
                } else{
                    viewModel.setFabButtonMode(true)
                }
            }
        })

        loginMenuButton.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                if(fbAuth.currentUser!=null){
                    viewModel.setMutliSelectMode(false)
                    findNavController().navigate(R.id.action_mainFramgent_to_profileFragment)
                } else{
                    findNavController().navigate(R.id.action_mainFramgent_to_loginFragment)
                }
            }
        })

        homeLayout.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                draw_lay.close()
            }
        })


        menuButton.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                if(viewModel.getMultiSelectMode().value==true){
                    viewModel.setMutliSelectMode(false)
                    viewModel.setNotifyDataNote(true)
                    viewModel.setNotifyDataCategory(true)
                } else{
                    draw_lay.open()
                }
            }
        })

        setUpTabs()

        /*
        viewModel.getMultiSelectNote().observe(viewLifecycleOwner, Observer {
            if(viewPager.currentItem==0&&it==false){
            } else if(viewPager.currentItem==0 && it == true){
                toolbar_Title.text = "Delete"
                floatingActionMenu.visibility = View.GONE
                menuButton.visibility = View.GONE
                searchIcon.setImageResource(R.drawable.ic_baseline_delete)
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
         */

        viewModel.getMultiSelectMode().observe(viewLifecycleOwner, Observer {
            if(it==true){
                onMultiSelectMode()
            } else {
                exitMultiSelectMode()
            }
        })

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                if (position == 0) {
                    addNote_FB.labelText = "Add Note"
                } else {
                    addNote_FB.labelText = "Add Category"
                }
        }

            override fun onPageScrollStateChanged(state: Int) {}
        })

            searchIcon.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    if(viewModel.getMultiSelectMode().value==true){

                        //DeleteNotes in firebase
                        if(fbAuth.currentUser!=null) {
                            profileViewModel = ViewModelProvider(requireActivity())[ProfilViewModel::class.java]
                            profileViewModel.deleteDataFromFirebase(viewModel.selectedNotes)
                        }
                        //DeleteNotes
                        viewModel.deleteNotes(viewModel.selectedNotes)

                        //Delete Category
                        viewModel.deleteCategotyItems(viewModel.selectedCategoryItems)

                        viewModel.selectedCategoryItems.forEach { it.isSelected = false }
                        viewModel.selectedCategoryItems.clear()

                        viewModel.selectedNotes.forEach{it.isSelected = false}
                        viewModel.selectedNotes.clear()

                        viewModel.setMutliSelectMode(false)
                    } else{
                        findNavController().navigate(R.id.action_mainFramgent_to_searchFragment)
                    }
                }
            })

        addNote_FB.setOnClickListener(View.OnClickListener {
            if(addNote_FB.labelText == "Add Note"){
                viewModel.newNote = true
                findNavController().navigate(R.id.action_mainFramgent_to_addNoteFragment)
            }else if(addNote_FB.labelText == "Add Category"){
                val fm = requireActivity().supportFragmentManager
                val dialogFragment = DialogAddCategoryItem()
                dialogFragment.show(fm, "Abc")
            }
        })
    }

    private fun exitMultiSelectMode() {
        searchIcon.setImageResource(R.drawable.ic_search)
        if(viewModel.getFabButtonMode().value==false){
            floatingActionMenu.visibility = View.VISIBLE
        } else{
            floatingActionMenu.visibility = View.GONE
        }
        menuButton.visibility = View.VISIBLE
        toolbar_Title.text = "Explore"
        menuButton.setImageResource(R.drawable.ic_menu2)
        fav_FB.visibility = View.VISIBLE
    }

    private fun setUpTabs(){
        val adapter = ViewPagerAdapter(childFragmentManager)
        adapter.addFragment(NoteFragment(), "Notes")
        adapter.addFragment(CategoryFragment(), "ToDo List")
        viewPager.adapter = adapter
        tabsLayout.setupWithViewPager(viewPager)
    }

    private fun onMultiSelectMode(){
        toolbar_Title.text = "Delete"
        floatingActionMenu.visibility = View.GONE
        //menuButton.visibility = View.GONE
        searchIcon.setImageResource(R.drawable.ic_round_delete_outline)
        menuButton.setImageResource(R.drawable.ic_round_arrow_back_ios)
        fav_FB.visibility = View.GONE
    }
}