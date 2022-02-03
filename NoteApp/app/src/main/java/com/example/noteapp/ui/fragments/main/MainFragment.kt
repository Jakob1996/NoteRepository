package com.example.noteapp.ui.fragments.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.drawerlayout.widget.DrawerLayout
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
import com.example.noteapp.viewmodels.TodoViewModel
import fadeIn
import fadeOut

class MainFragment : BaseFragment(), OnItemClickDialogListener {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var todoViewModel: TodoViewModel
    private lateinit var profileViewModel: ProfilViewModel
    private val request_code = 123

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun setupView() {
        binding.fragmentMainDl.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

        setupFavouriteButtonObserverState()
        setOnFavouriteButtonClickListener()
        setOnLoginBtnListener()
        setOnHomeDrawerBtnListener()
        setOnHomeBtnListener()
        setupMultiSelectModeObserverState()
        setupViewPagerChangeListener()
        setupMultiBtnClickListener()
        setupMainBtnListener()
        setupSortBtnListener()
    }

    override fun onDestroyView() {
        noteViewModel.view = _binding
        _binding = null
        super.onDestroyView()
    }

    private fun onBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    noteViewModel.run {
                        if (getMultiSelectMode().value == true) {
                            setMutliSelectMode(false)
                            setNotifyDataNote(true)
                            setNotifyDataCategory(true)
                        }
                    }

                    isEnabled = false
                    requireActivity().onBackPressed()
                }
            })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        onBackPressed()
        setupViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = noteViewModel.view

        if (_binding == null) {
            _binding = FragmentMainBinding.inflate(inflater, container, false)
            setupViewPager()
        }

        return binding.root
    }

    private fun setupFavouriteButtonObserverState() {
        noteViewModel.getNoteFabButtonMode().observe(viewLifecycleOwner, {
            if (it == true) {
                binding.fragmentMainFam.fadeOut()
            } else {
                binding.fragmentMainFam.fadeIn()
            }
        })
    }

    private fun setupViewModel() {
        noteViewModel = ViewModelProvider(requireActivity())[NoteViewModel::class.java]
        todoViewModel = ViewModelProvider(requireActivity())[TodoViewModel::class.java]
    }

    private fun setOnFavouriteButtonClickListener() {
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
    }

    private fun setOnLoginBtnListener() {
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
    }

    private fun setOnHomeDrawerBtnListener() {
        binding.fragmentMainMenu.menuDrawerHomeLl.setOnClickListener { binding.fragmentMainDl.close() }
    }

    private fun setOnHomeBtnListener() {
        binding.fragmentMainToolbarMenuIb.setOnClickListener {
            noteViewModel.run {
                if (getMultiSelectMode().value == true) {
                    setMutliSelectMode(false)
                    setNotifyDataNote(true)
                    setNotifyDataCategory(true)
                } else {
//                binding.fragmentMainDl.open()
                    navigateToFragment(findNavController(), R.id.action_to_login_fragment)
                }
            }
        }
    }

    private fun setupMultiSelectModeObserverState() {
        noteViewModel.getMultiSelectMode().observe(viewLifecycleOwner, {
            if (it == true) {
                enableMultiSelectMode()
            } else {
                disableMultiSelectMode()
            }
        })
    }

    private fun setupViewPagerChangeListener() {
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
    }

    private fun setupMultiBtnClickListener() {
        binding.fragmentMainToolbarMultibuttonIb.setOnClickListener {
            noteViewModel.run {
                if (noteViewModel.getMultiSelectMode().value == true) {

                    //DeleteNotes in firebase
                    if (fbAuth.currentUser != null) {
                        profileViewModel =
                            ViewModelProvider(requireActivity())[ProfilViewModel::class.java]
                        profileViewModel.deleteDataFromFirebase(noteViewModel.selectedNotes)
                        profileViewModel.deleteCategoriesFromFirebase(todoViewModel.selectedCategoryItems)
                    }
                    //DeleteNotes
                    deleteNotes(noteViewModel.selectedNotes)
                    selectedNotes.forEach { it.isSelected = false }
                    selectedNotes.clear()

                    //Delete Category
                    todoViewModel.deleteCategotyItems(todoViewModel.selectedCategoryItems)

                    todoViewModel.selectedCategoryItems.forEach { it.isSelected = false }
                    todoViewModel.selectedCategoryItems.clear()

                    setMutliSelectMode(false)

                } else {
                    noteViewModel.setIsFromMainFragmentNavigation(false)
                    if (binding.fragmentMainVp.currentItem == 0) {
                        navigateToFragment(
                            findNavController(), R.id.action_to_search_note_fragment
                        )
                    } else {
                        todoViewModel.setIsFromMainFragmentNavigation(false)
                        navigateToFragment(
                            findNavController(),
                            R.id.action_to_search_category_fragment
                        )
                    }
                }
            }
        }
    }

    private fun setupMainBtnListener() {
        binding.fragmentMainAddNoteFab.setOnClickListener {
            if (binding.fragmentMainAddNoteFab.labelText == "Add Note") {
                binding.fragmentMainFam.close(true)
                noteViewModel.newNote = true
                navigateToFragment(
                    findNavController(), R.id.addNoteFragment
                )
            } else if (binding.fragmentMainAddNoteFab.labelText == "Add Category") {
                val fm = requireActivity().supportFragmentManager
                val dialogFragment = DialogAddCategoryItem()
                dialogFragment.show(fm, "Abc")
            }
        }
    }

    private fun setupSortBtnListener() {
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
        noteViewModel.run {
            setSortDescNote(sortDesc)
            setSortDescCategory(sortDesc)
        }
    }

    private fun disableMultiSelectMode() {
        binding.fragmentMainToolbarMultibuttonIb.setImageResource(R.drawable.ic_search)
        when (noteViewModel.getNoteFabButtonMode().value) {
            false -> {
                binding.fragmentMainFam.fadeIn()
            }
            true -> {
                binding.fragmentMainFam.fadeOut()
            }
            else -> {
                binding.fragmentMainFam.fadeIn()
            }
        }
        binding.run {
            fragmentMainToolbarMenuIb.fadeIn()
            fragmentMainToolbarMenuIb.setImageResource(R.drawable.ic_menu2)
            fragmentSearchCategoryToolbarTitleTv.text = getString(R.string.Explore)
            fragmentMainToolbarFavouriteCb.fadeIn()
        }
    }

    private fun setupViewPager() {
        val adapter = ViewPagerAdapter(requireActivity().supportFragmentManager)
        binding.run {
            fragmentMainVp.adapter = adapter
            fragmentMainVp.offscreenPageLimit = 1
            fragmentMainTl.setupWithViewPager(binding.fragmentMainVp)
        }
    }

    private fun enableMultiSelectMode() {
        binding.run {
            fragmentSearchCategoryToolbarTitleTv.text = getString(R.string.deleteToolbar)
            fragmentMainFam.fadeOut()
            fragmentMainToolbarMultibuttonIb.setImageResource(R.drawable.ic_round_delete_outline)
            fragmentMainToolbarMenuIb.setImageResource(R.drawable.ic_round_arrow_back_ios)
            fragmentMainToolbarFavouriteCb.fadeOut()
        }
    }
}