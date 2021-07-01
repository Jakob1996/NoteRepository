package com.example.noteapp.ui.fragments.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.example.noteapp.R
import com.example.noteapp.adapters.ViewPagerAdapter
import com.example.noteapp.databinding.FragmentMainBinding
import com.example.noteapp.navigation.Navigation
import com.example.noteapp.ui.fragments.login.LoginFragment
import com.example.noteapp.ui.fragments.note.AddNoteFragment
import com.example.noteapp.ui.fragments.search.SearchCategoryFragment
import com.example.noteapp.ui.fragments.search.SearchNoteFragment
import com.example.noteapp.ui.fragments.sort.SortDialogFragment
import com.example.noteapp.ui.fragments.todo.DialogAddCategoryItem
import com.example.noteapp.ui.fragments.profile.ProfileFragment
import com.example.noteapp.viewmodels.ProfilViewModel
import com.example.noteapp.viewmodels.NoteViewModel
import com.example.noteapp.viewmodels.ToDoViewModel
import com.google.firebase.auth.FirebaseAuth

class MainFragment: Fragment(), SortDialogFragment.OnItemClickDialogListener, Navigation {

    private var _binding:FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var noteViewModel:NoteViewModel
    private lateinit var todoViewModel:ToDoViewModel
    private lateinit var profileViewModel: ProfilViewModel
    private val fbAuth = FirebaseAuth.getInstance()
    private val request_code = 123

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater , container , false)

        Log.d("ahc", "onViewCreated MainF")
        noteViewModel = ViewModelProvider(requireActivity())[NoteViewModel::class.java]
        todoViewModel = ViewModelProvider(requireActivity())[ToDoViewModel::class.java]

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object: OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                if(noteViewModel.getMultiSelectMode().value == true){
                    noteViewModel.setMutliSelectMode(false)
                    noteViewModel.setNotifyDataNote(true)
                    noteViewModel.setNotifyDataCategory(true)
                } else{
                    isEnabled = false
                    requireActivity().onBackPressed()
                }
            }
        })

        noteViewModel.getNoteFabButtonMode().observe(viewLifecycleOwner, Observer {
            if(it==true){
                binding.floatingActionMenu.visibility = View.GONE
            } else{
                binding.floatingActionMenu.visibility = View.VISIBLE
            }
        })

        binding.favFB.setOnClickListener {
            if (noteViewModel.getNoteFabButtonMode().value == true) {
                noteViewModel.run {
                    setNoteFabButtonMode(false)
                    setCategoryFabButtonMode(false)
                }
            } else {
                noteViewModel.setNoteFabButtonMode(true)
                noteViewModel.setCategoryFabButtonMode(true)
            }
        }

        binding.includeMenu.loginMenuButton.setOnClickListener {
            if (fbAuth.currentUser != null) {
                navigateToFragment(ProfileFragment(), "main", requireActivity().supportFragmentManager)
            } else {
                navigateToFragment(LoginFragment(), "main", requireActivity().supportFragmentManager)
            }
            binding.drawLay.close()
        }

        binding.includeMenu.homeLayout.setOnClickListener { binding.drawLay.close() }

        binding.menuButton.setOnClickListener {
            if (noteViewModel.getMultiSelectMode().value == true) {
                noteViewModel.setMutliSelectMode(false)
                noteViewModel.setNotifyDataNote(true)
                noteViewModel.setNotifyDataCategory(true)
            } else {
                binding.drawLay.open()
            }
        }

        noteViewModel.getMultiSelectMode().observe(viewLifecycleOwner,  {
            if(it==true){
                onMultiSelectMode()
            } else {
                exitMultiSelectMode()
            }
        })

        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                if (position == 0) {
                    binding.addNoteFB.labelText = "Add Note"
                } else {
                    binding.addNoteFB.labelText = "Add Category"
                }
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })

        binding.searchIcon.setOnClickListener {
            if (noteViewModel.getMultiSelectMode().value == true) {

                //DeleteNotes in firebase
                if (fbAuth.currentUser != null) {
                    profileViewModel =
                        ViewModelProvider(requireActivity())[ProfilViewModel::class.java]
                    profileViewModel.deleteDataFromFirebase(noteViewModel.selectedNotes)
                }
                //DeleteNotes
                noteViewModel.deleteNotes(noteViewModel.selectedNotes)

                noteViewModel.selectedNotes.forEach { it.isSelected = false }
                noteViewModel.selectedNotes.clear()

                //Delete Category
                todoViewModel.deleteCategotyItems(todoViewModel.selectedCategoryItems)

                todoViewModel.selectedCategoryItems.forEach { it.isSelected = false }
                todoViewModel.selectedCategoryItems.clear()

                noteViewModel.setMutliSelectMode(false)

            } else {
                if (binding.viewPager.currentItem == 0) {
                    navigateToFragment(SearchNoteFragment(), "SF", requireActivity().supportFragmentManager)
                } else {
                    navigateToFragment(SearchCategoryFragment(), "SCF", parentFragmentManager)
                }
            }
        }

        binding.addNoteFB.setOnClickListener{
            if(binding.addNoteFB.labelText == "Add Note"){
                noteViewModel.newNote = true
                binding.floatingActionMenu.close(true)
                navigateToFragment(AddNoteFragment(), "main", requireActivity().supportFragmentManager)
            }else if(binding.addNoteFB.labelText == "Add Category"){
                val fm = requireActivity().supportFragmentManager
                val dialogFragment = DialogAddCategoryItem()
                dialogFragment.show(fm, "Abc")
            }
        }

        binding.sortFB.setOnClickListener {
            val sortDialogFragment = SortDialogFragment()
            showDialog(sortDialogFragment, "SortDialogFragment", parentFragmentManager, request_code)
        }

        setUpTabs()

        return binding.root
    }

    override fun onItemClickDialog(sortDesc: Boolean) {
        noteViewModel.setSortDescNote(sortDesc)
        noteViewModel.setSortDescCategory(sortDesc)
    }

    private fun exitMultiSelectMode() {
        binding.searchIcon.setImageResource(R.drawable.ic_search)
        when (noteViewModel.getNoteFabButtonMode().value) {
            false -> {
                binding.floatingActionMenu.visibility = View.VISIBLE
            }
            true -> {
                binding.floatingActionMenu.visibility = View.GONE
            }
            else -> {
                binding.floatingActionMenu.visibility = View.VISIBLE
            }
        }
        binding.menuButton.visibility = View.VISIBLE
        binding.menuButton.setImageResource(R.drawable.ic_menu2)
        binding.toolbarTitle.text = getString(R.string.Explore)
        binding.favFB.visibility = View.VISIBLE
    }

    private fun setUpTabs(){
        val adapter = ViewPagerAdapter(requireActivity().supportFragmentManager)
        binding.viewPager.adapter = adapter
        binding.viewPager.offscreenPageLimit = 1
        binding.tabsLayout.setupWithViewPager(binding.viewPager)
    }

    private fun onMultiSelectMode(){
        binding.toolbarTitle.text = getString(R.string.deleteToolbar)
        binding.floatingActionMenu.visibility = View.GONE
        binding.searchIcon.setImageResource(R.drawable.ic_round_delete_outline)
        binding.menuButton.setImageResource(R.drawable.ic_round_arrow_back_ios)
        binding.favFB.visibility = View.GONE
    }
}