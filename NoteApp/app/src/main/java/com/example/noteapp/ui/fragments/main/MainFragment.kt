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
import com.example.noteapp.ui.activities.MainActivity
import com.example.noteapp.ui.fragments.baseFragment.BaseFragment
import com.example.noteapp.ui.fragments.sort.SortDialogFragment
import com.example.noteapp.ui.fragments.todo.DialogAddCategoryItem
import com.example.noteapp.viewmodels.ProfilViewModel
import com.example.noteapp.viewmodels.NoteViewModel
import com.example.noteapp.viewmodels.TodoViewModel
import fadeIn
import fadeOut

class MainFragment : BaseFragment() {

    companion object {
        const val REQUEST_CODE_KEY = 123
    }

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var todoViewModel: TodoViewModel
    private lateinit var profileViewModel: ProfilViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    override fun onResume() {
        super.onResume()

        val mainActivity = this.activity as MainActivity

        mainActivity.setupToolbar(
            "Main", true,
            backBtnVisible = false,
            favouriteBtnVisible = true,
            multiBtnVisible = true
        )
    }

    private fun setupView() {
        binding.fragmentMainDl.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

        setupFavouriteButtonObserverState()
        setupOnLoginBtnListener()
        setupOnHomeDrawerBtnListener()
        setupOnHomeBtnListener()
        setupMultiSelectModeObserverState()
        setupViewPagerChangeListener()
        setupMainBtnListener()
        setupSortBtnListener()
        val mainActivity = this.activity as MainActivity

        mainActivity.onFavouriteBtnPressed {
            if (noteViewModel.fabNoteButtonMode.value == true) {
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

        mainActivity.onSearchBtnPressed {
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

                    setMultiSelectMode(false)

                } else {
                    if (binding.fragmentMainVp.currentItem == 0) {
                        noteViewModel.setIsFromMainFragmentNavigation(false)
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
                            setMultiSelectMode(false)
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
        noteViewModel.fabNoteButtonMode.observe(viewLifecycleOwner) {
            if (it == true) {
                binding.fragmentMainFam.fadeOut()
            } else {
                binding.fragmentMainFam.fadeIn()
            }
        }
    }

    private fun setupViewModel() {
        noteViewModel = ViewModelProvider(requireActivity())[NoteViewModel::class.java]
        todoViewModel = ViewModelProvider(requireActivity())[TodoViewModel::class.java]
    }

    private fun setupOnLoginBtnListener() {
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

    private fun setupOnHomeDrawerBtnListener() {
        binding.fragmentMainMenu.menuDrawerHomeLl.setOnClickListener { binding.fragmentMainDl.close() }
    }

    private fun setupOnHomeBtnListener() {
//        binding.fragmentMainToolbarMenuIb.setOnClickListener {
//            noteViewModel.run {
//                if (getMultiSelectMode().value == true) {
//                    setMutliSelectMode(false)
//                    setNotifyDataNote(true)
//                    setNotifyDataCategory(true)
//                } else {
//                    //TODO {FEATURE IMPLEMENTATION}
////                binding.fragmentMainDl.open()
//                    navigateToFragment(findNavController(), R.id.action_to_login_fragment)
//                }
//            }
//        }
    }

    private fun setupMultiSelectModeObserverState() {
        noteViewModel.getMultiSelectMode().observe(viewLifecycleOwner) {
            if (it == true) {
                enableMultiSelectMode()
            } else {
                disableMultiSelectMode()
            }
        }
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
                    binding.fragmentMainAddNoteFab.labelText = getString(R.string.add_note)
                } else {
                    binding.fragmentMainAddNoteFab.labelText = getString(R.string.add_category)
                }
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    private fun setupMainBtnListener() {
        binding.fragmentMainAddNoteFab.setOnClickListener {
            if (binding.fragmentMainAddNoteFab.labelText == getString(R.string.add_note)) {
                binding.fragmentMainFam.close(true)
                noteViewModel.newNote = true
                navigateToFragment(
                    findNavController(), R.id.action_to_add_note_fragment
                )
            } else if (binding.fragmentMainAddNoteFab.labelText == getString(R.string.add_category)) {
                val fm = requireActivity().supportFragmentManager
                val dialogFragment = DialogAddCategoryItem()
                dialogFragment.show(fm, "Abc")
            }
        }
    }

    private fun setupSortBtnListener() {
        binding.fragmentMainSortNoteFab.setOnClickListener {
            val fm = requireActivity().supportFragmentManager
            val dialogFragment = SortDialogFragment()
            dialogFragment.show(fm, "SortDialogFragment")
        }
    }


    private fun disableMultiSelectMode() {
        val mainActivity = this.activity as MainActivity

        mainActivity.disableMultiSelectMode()
        when (noteViewModel.fabNoteButtonMode.value) {
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
    }

    private fun setupViewPager() {
        val adapter =
            ViewPagerAdapter(requireActivity().supportFragmentManager, this.requireContext())
        binding.run {
            fragmentMainVp.adapter = adapter
            fragmentMainVp.offscreenPageLimit = 1
            fragmentMainTl.setupWithViewPager(binding.fragmentMainVp)
        }
    }

    private fun enableMultiSelectMode() {
        val mainActivity = this.activity as MainActivity

        mainActivity.enableMultiSelectMode()
        binding.fragmentMainFam.fadeOut()
    }
}