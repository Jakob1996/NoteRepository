package com.example.noteapp.ui.fragments.main

import android.content.res.Configuration
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
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.viewpager.widget.ViewPager
import com.example.noteapp.R
import com.example.noteapp.adapters.NoteAdapter
import com.example.noteapp.adapters.OnItemClickListener
import com.example.noteapp.adapters.ViewPagerAdapter
import com.example.noteapp.data.Note
import com.example.noteapp.databinding.FragmentMainBinding
import com.example.noteapp.ui.fragments.note.NoteFragment
import com.example.noteapp.ui.fragments.sort.SortDialogFragment
import com.example.noteapp.ui.fragments.todo.CategoryFragment
import com.example.noteapp.ui.fragments.todo.DialogAddCategoryItem
import com.example.noteapp.viewmodels.ProfilViewModel
import com.example.noteapp.viewmodels.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.menu_drawer2.view.*


class MainFragment() : Fragment(),  SortDialogFragment.OnItemClickDialogListener, OnItemClickListener {

    //private var _binding:FragmentMainBinding? = null
    //private val binding get() = _binding!!
    private lateinit var viewModel:ViewModel
    private lateinit var profileViewModel: ProfilViewModel
    private val fbAuth = FirebaseAuth.getInstance()
    private val request_code = 123
    private lateinit var adapter:NoteAdapter

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
        Log.d("Abccc", "onDestroy")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[ViewModel::class.java]

        Log.d("Abccc", "OnCreate")

        requireActivity().onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
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
        _binding = FragmentMainBinding.inflate(inflater , container , false)

        Log.d("Abccc", "OnCreateView")


        viewModel.getNoteFabButtonMode().observe(viewLifecycleOwner, Observer {
            Log.d("Abccc", "getNoteFabButton")
            if(it==true){
                binding.floatingActionMenu.visibility = View.GONE
                binding.favFB.setColorFilter(Color.parseColor("#FDBE3B"))
            } else{
                binding.floatingActionMenu.visibility = View.VISIBLE
                binding.favFB.setColorFilter(Color.WHITE)
            }
        })

        binding.favFB.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                if(viewModel.getNoteFabButtonMode().value==true){
                    viewModel.setNoteFabButtonMode(false)
                    viewModel.setCategoryFabButtonMode(false)
                } else{
                    viewModel.setNoteFabButtonMode(true)
                    viewModel.setCategoryFabButtonMode(true)
                }
            }
        })


        binding.drawLay.loginMenuButton.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                if(fbAuth.currentUser!=null){
                    viewModel.setMutliSelectMode(false)
                    findNavController().navigate(R.id.action_mainFramgent_to_profileFragment)
                } else{
                    findNavController().navigate(R.id.action_mainFramgent_to_loginFragment)
                }
            }
        })

        binding.drawLay.homeLayout.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                binding.drawLay.close()
            }
        })

        binding.menuButton.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                if(viewModel.getMultiSelectMode().value==true){
                    viewModel.setMutliSelectMode(false)
                    viewModel.setNotifyDataNote(true)
                    viewModel.setNotifyDataCategory(true)
                } else{
                    binding.drawLay.open()
                }
            }
        })

        viewModel.getMultiSelectMode().observe(viewLifecycleOwner, Observer {
            Log.d("Abccc", "getMutliSelectMode")
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



        binding.searchIcon.setOnClickListener(object : View.OnClickListener {
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
                    if(binding.viewPager.currentItem==0){
                        findNavController().navigate(R.id.action_mainFramgent_to_searchFragment)
                    } else{
                        findNavController().navigate(R.id.action_mainFramgent_to_searchCategoryFragment)
                    }



                }
            }
        })

        binding.addNoteFB.setOnClickListener(View.OnClickListener {
            if(binding.addNoteFB.labelText == "Add Note"){
                viewModel.newNote = true
                findNavController().navigate(R.id.action_mainFramgent_to_addNoteFragment)
            }else if(binding.addNoteFB.labelText == "Add Category"){
                val fm = requireActivity().supportFragmentManager
                val dialogFragment = DialogAddCategoryItem()
                dialogFragment.show(fm, "Abc")
            }
        })


        binding.sortFB.setOnClickListener {
            val sortDialogFragment = SortDialogFragment()
            sortDialogFragment.setTargetFragment(this, request_code)
            sortDialogFragment.show(parentFragmentManager, "SortDialogFragment")
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("Abccc", "onViewCreated")
        setUpTabs()
    }

    override fun onItemClickDialog(sortDesc: Boolean) {
        viewModel.setSortDescNote(sortDesc)
        viewModel.setSortDescCategory(sortDesc)
    }

    private fun exitMultiSelectMode() {
        binding.searchIcon.setImageResource(R.drawable.ic_search)
        if(viewModel.getNoteFabButtonMode().value == false){
            binding.floatingActionMenu.visibility = View.VISIBLE
        } else if(viewModel.getNoteFabButtonMode().value == true){
            binding.floatingActionMenu.visibility = View.GONE
        } else{
            binding.floatingActionMenu.visibility = View.VISIBLE
        }
        binding.menuButton.visibility = View.VISIBLE
        binding.menuButton.setImageResource(R.drawable.ic_menu2)
        binding.toolbarTitle.text = "Explore"
        binding.favFB.visibility = View.VISIBLE
    }

    private fun setUpTabs(){
        val adapter = ViewPagerAdapter(childFragmentManager)

        adapter.addFragment(NoteFragment(), "Notes")
        adapter.addFragment(CategoryFragment(), "ToDo List")
        binding.viewPager.adapter = adapter
        binding.viewPager.offscreenPageLimit =1
        binding.tabsLayout.setupWithViewPager(binding.viewPager)

    }

    private fun onMultiSelectMode(){
        binding.toolbarTitle.text = "Delete"
        binding.floatingActionMenu.visibility = View.GONE
        binding.searchIcon.setImageResource(R.drawable.ic_round_delete_outline)
        binding.menuButton.setImageResource(R.drawable.ic_round_arrow_back_ios)
        binding.favFB.visibility = View.GONE
    }

    override fun onItemClick(note: Note, position: Int) {
      findNavController().navigate(R.id.action_mainFramgent_to_BeforeAddEditNoteFragment)
    }

    override fun onItemLongClick(note: Note, position: Int) {

    }
}