package com.example.noteapp.ui.fragments.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.example.noteapp.R
import com.example.noteapp.adapters.ViewPagerAdapter
import com.example.noteapp.databinding.FragmentMainBinding
import com.example.noteapp.ui.interfaces.OnItemClickDialogListener
import com.example.noteapp.ui.fragments.baseFragment.BaseFragment
import com.example.noteapp.ui.fragments.todo.DialogAddCategoryItem
import com.example.noteapp.viewmodels.ProfilViewModel
import com.example.noteapp.viewmodels.NoteViewModel
import com.example.noteapp.viewmodels.ToDoViewModel

class MainFragment : BaseFragment(), OnItemClickDialogListener {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var todoViewModel: ToDoViewModel
    private lateinit var profileViewModel: ProfilViewModel
    private val request_code = 123

    override fun onDestroyView() {
        noteViewModel.view = _binding
        _binding = null
        super.onDestroyView()
    }

    override fun onBackPress() {
        if (noteViewModel.getMultiSelectMode().value == true) {
            noteViewModel.setMutliSelectMode(false)
            noteViewModel.setNotifyDataNote(true)
            noteViewModel.setNotifyDataCategory(true)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        noteViewModel = ViewModelProvider(requireActivity())[NoteViewModel::class.java]
        todoViewModel = ViewModelProvider(requireActivity())[ToDoViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = noteViewModel.view

        if (_binding == null) {
            _binding = FragmentMainBinding.inflate(inflater, container, false)
            setUpTabs()
        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        noteViewModel.getNoteFabButtonMode().observe(viewLifecycleOwner, {
            if (it == true) {
                binding.fragmentMainFam.visibility = View.GONE
            } else {
                binding.fragmentMainFam.visibility = View.VISIBLE
            }
        })

        binding.fragmentMainToolbarFavouriteCb.setOnClickListener {
            if (noteViewModel.getNoteFabButtonMode().value == true) {
                noteViewModel.run {
                    setNoteFabButtonMode(false)
                    setCategoryFabButtonMode(false)
                }
            } else {
                noteViewModel.setNoteFabButtonMode(true)
                noteViewModel.setCategoryFabButtonMode(true)
                findNavController()
            }
        }

        binding.fragmentMainMenu.menuDrawerLoginLl.setOnClickListener {
            if (fbAuth.currentUser != null) {
                navigateToFragment(
                    findNavController(), R.id.userProfileFragment

                )
            } else {
                navigateToFragment(
                    findNavController(), R.id.action_to_login_fragment
                )
            }
            binding.fragmentMainDl.close()
        }

        binding.fragmentMainMenu.menuDrawerHomeLl.setOnClickListener { binding.fragmentMainDl.close() }

        binding.fragmentMainToolbarMenuIb.setOnClickListener {
            if (noteViewModel.getMultiSelectMode().value == true) {
                noteViewModel.setMutliSelectMode(false)
                noteViewModel.setNotifyDataNote(true)
                noteViewModel.setNotifyDataCategory(true)
            } else {
                binding.fragmentMainDl.open()
            }
        }

        noteViewModel.getMultiSelectMode().observe(viewLifecycleOwner, {
            if (it == true) {
                onMultiSelectMode()
            } else {
                exitMultiSelectMode()
            }
        })

        binding.fragmentMainVp.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                if (position == 0) {
                    binding.fragmentMainAddNoteFab.labelText = "Add Note"
                } else {
                    binding.fragmentMainAddNoteFab.labelText = "Add Category"
                }
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })

        binding.fragmentMainToolbarSearchIb.setOnClickListener {
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
                if (binding.fragmentMainVp.currentItem == 0) {
                    navigateToFragment(
                        findNavController(), R.id.searchNoteFragment
                    )
                } else {
                    navigateToFragment(findNavController(), R.id.searchCategoryFragment)
                }
            }
        }

        binding.fragmentMainAddNoteFab.setOnClickListener {
            if (binding.fragmentMainAddNoteFab.labelText == "Add Note") {
                noteViewModel.newNote = true
                binding.fragmentMainFam.close(true)
                navigateToFragment(
                    findNavController(), R.id.addNoteFragment
                )
            } else if (binding.fragmentMainAddNoteFab.labelText == "Add Category") {
                val fm = requireActivity().supportFragmentManager
                val dialogFragment = DialogAddCategoryItem()
                dialogFragment.show(fm, "Abc")
            }
        }

        binding.fragmentMainSortNoteFab.setOnClickListener {
            showDialog(
                this,
                "SortDialogFragment",
                parentFragmentManager,
                request_code
            )
        }
    }

    override fun onItemClickDialog(sortDesc: Boolean) {
        noteViewModel.setSortDescNote(sortDesc)
        noteViewModel.setSortDescCategory(sortDesc)
    }

    private fun exitMultiSelectMode() {
        binding.fragmentMainToolbarSearchIb.setImageResource(R.drawable.ic_search)
        when (noteViewModel.getNoteFabButtonMode().value) {
            false -> {
                binding.fragmentMainFam.visibility = View.VISIBLE
            }
            true -> {
                binding.fragmentMainFam.visibility = View.GONE
            }
            else -> {
                binding.fragmentMainFam.visibility = View.VISIBLE
            }
        }
        binding.fragmentMainToolbarMenuIb.visibility = View.VISIBLE
        binding.fragmentMainToolbarMenuIb.setImageResource(R.drawable.ic_menu2)
        binding.fragmentSearchCategoryToolbarTitleTv.text = getString(R.string.Explore)
        binding.fragmentMainToolbarFavouriteCb.visibility = View.VISIBLE
    }

    private fun setUpTabs() {
        val adapter = ViewPagerAdapter(requireActivity().supportFragmentManager)
        binding.fragmentMainVp.adapter = adapter
        binding.fragmentMainVp.offscreenPageLimit = 1
        binding.fragmentMainTl.setupWithViewPager(binding.fragmentMainVp)
    }

    private fun onMultiSelectMode() {
        binding.fragmentSearchCategoryToolbarTitleTv.text = getString(R.string.deleteToolbar)
        binding.fragmentMainFam.visibility = View.GONE
        binding.fragmentMainToolbarSearchIb.setImageResource(R.drawable.ic_round_delete_outline)
        binding.fragmentMainToolbarMenuIb.setImageResource(R.drawable.ic_round_arrow_back_ios)
        binding.fragmentMainToolbarFavouriteCb.visibility = View.GONE
    }
}