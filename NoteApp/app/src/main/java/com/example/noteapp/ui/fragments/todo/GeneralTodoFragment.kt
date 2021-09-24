package com.example.noteapp.ui.fragments.todo

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.noteapp.R
import com.example.noteapp.adapters.OnItemTodoClickListener
import com.example.noteapp.adapters.ToDoItemAdapter
import com.example.noteapp.data.Category
import com.example.noteapp.data.ItemOfList
import com.example.noteapp.databinding.FragmentGeneralTodoBinding
import com.example.noteapp.navigation.Navigation
import com.example.noteapp.ui.fragments.baseFragment.BaseFragment
import com.example.noteapp.ui.fragments.password.RemovePasswordDialogFragment
import com.example.noteapp.viewmodels.TodoViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import fadeIn
import fadeOut
import kotlinx.coroutines.*
import java.util.*

class GeneralTodoFragment : BaseFragment(), OnItemTodoClickListener, Navigation {

    private lateinit var todoViewModel: TodoViewModel

    private var value: Boolean = false

    private lateinit var itemTodoAdapter: ToDoItemAdapter

    private var quit = 1

    private var _binding: FragmentGeneralTodoBinding? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        saveTodoList()
        todoViewModel = ViewModelProvider(requireActivity())[TodoViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGeneralTodoBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMiscellaneous()

        todoViewModel.getSelectedCategotyItem()
            .observe(viewLifecycleOwner, { it ->
                binding.run { fragmentGeneralTodoTitleEt.setText(it?.categoryName) }

                binding.fragmentGeneralTodoToolbarTitleTv.text =
                    getString(R.string.categoryEditorTextView)
                todoViewModel.selectedCategotyItemColor = it!!.color
                todoViewModel.categoryName = it.categoryName
                todoViewModel.categoryDate = it.date
                todoViewModel.categoryIsSelected = it.isSelected
                todoViewModel.isFavouriteCategory = it.isFavoutire
                todoViewModel.hasPasswordCategory = it.hasPassword
                todoViewModel.passwordCategory = it.password
                todoViewModel.categoryId = it.rowIdCategory

                setImagePassword(todoViewModel.hasPasswordCategory)

                binding.fragmentGeneralTodoCastomizer.todoCastomizerFavoutiteCb.isChecked =
                    todoViewModel.isFavouriteCategory
                todoViewModel.getAllItemsFromCategory(todoViewModel.getSelectedCategotyItem().value!!.rowIdCategory)
                    .observe(viewLifecycleOwner, {
                        CoroutineScope(Dispatchers.Main).launch {
                            withContext(Dispatchers.Main) {
                                delay(200)
                                updateItems(it)
                                binding.fragmentGeneralTodoPb.fadeOut()
                                binding.fragmentGeneralTodoRv.fadeIn()
                            }
                        }
                    })
            })

        binding.fragmentGeneralTodoCastomizer.todoCastomizerLockIv.setOnClickListener {
            if (todoViewModel.hasPasswordCategory) {
                val fm = requireActivity().supportFragmentManager
                val dialogFragment = RemovePasswordDialogFragment()
                dialogFragment.show(fm, "ppp")
            } else {
                findNavController().navigate(R.id.action_general_todo_fragment_to_add_password_fragment)
            }
        }

        binding.fragmentGeneralTodoToolbarSaveIv.setOnClickListener {
            val fm = requireActivity().supportFragmentManager
            val dialogFragment = DialogAddToDoFragment()
            dialogFragment.show(fm, "Abc")

            itemTodoAdapter.notifyDataSetChanged()
        }
    }


    private fun initMiscellaneous() {
        val layoutMiscellaneous = binding.fragmentGeneralTodoCastomizer
        val bottomSheetBehavior =
            BottomSheetBehavior.from(binding.fragmentGeneralTodoCastomizer.todoCastomizerLl)

        layoutMiscellaneous.todoCastomizerLl.setOnClickListener {
            if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            } else {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }
    }

    private fun closeKeyboard() {
        val view = requireActivity().currentFocus
        if (view != null) {
            val imm =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    override fun onItemClick(itemsOfList: ItemOfList, position: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            if (itemsOfList.isDone) {
                itemsOfList.isDone = false
                todoViewModel.updateItem(itemsOfList)
            } else {
                itemsOfList.isDone = true
                todoViewModel.updateItem(itemsOfList)
            }
        }
    }

    override fun onItemLongClick(itemsOfList: ItemOfList, position: Int) {
        todoViewModel.setSelectedItem(itemsOfList)

        val fm = requireActivity().supportFragmentManager
        val dialogFragment = DialogAddToDoFragment()
        dialogFragment.show(fm, "Abc")

        itemTodoAdapter.notifyDataSetChanged()
    }

    private fun updateItems(list: List<ItemOfList>) {
        val lm = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)

        binding.fragmentGeneralTodoRv.layoutManager = lm

        itemTodoAdapter = ToDoItemAdapter(list, this)

        binding.fragmentGeneralTodoRv.adapter = itemTodoAdapter

        val item = object : SwipeToDelete(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                todoViewModel.deleteItem(itemTodoAdapter.getItemInPosition(viewHolder.bindingAdapterPosition))
            }
        }

        val itemTouchHelper = ItemTouchHelper(item)

        itemTouchHelper.attachToRecyclerView(binding.fragmentGeneralTodoRv)

        itemTodoAdapter.notifyDataSetChanged()
    }

    private fun setImagePassword(hasPassword: Boolean) {
        if (hasPassword) {
            binding.fragmentGeneralTodoCastomizer.todoCastomizerLockIv.setColorFilter(
                Color.parseColor(
                    "#FF4343"
                )
            )
            binding.fragmentGeneralTodoCastomizer.todoCastomizerLockIv.setImageResource(R.drawable.ic_lock_24)
        } else {
            binding.fragmentGeneralTodoCastomizer.todoCastomizerLockIv.setColorFilter(Color.WHITE)
            binding.fragmentGeneralTodoCastomizer.todoCastomizerLockIv.setImageResource(R.drawable.ic_baseline_lock)
        }
    }

    private fun saveTodoList() {
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {

                    if (quit == 2) {
                        todoViewModel.setDefaultTodoState()
                    }

                    quit()
                    isEnabled = false
                    requireActivity().onBackPressed()
                }
            })
    }

    private fun quit() {

        val beforeCategory = todoViewModel.categoryItemBeforeChange
        val colorr = todoViewModel.selectedCategotyItemColor
        val name = binding.fragmentGeneralTodoTitleEt.text.toString()
        val datee = todoViewModel.categoryDate
        val isSelectedd = todoViewModel.categoryIsSelected
        val isFavourite = binding.fragmentGeneralTodoCastomizer.todoCastomizerFavoutiteCb.isChecked
        val hasPassword = todoViewModel.hasPasswordCategory
        val password = todoViewModel.passwordCategory
        val id = todoViewModel.categoryId

        if (name != beforeCategory!!.categoryName
            || colorr != beforeCategory.color
            || hasPassword != beforeCategory.hasPassword
            || isFavourite != beforeCategory.isFavoutire
        ) {
            val category = Category(
                name,
                colorr,
                isSelectedd,
                datee,
                isFavourite,
                hasPassword,
                password
            ).apply {
                rowIdCategory = id
            }

            todoViewModel.updateCategoryItem(category)
        }

        closeKeyboard()
        quit = 2
    }
}