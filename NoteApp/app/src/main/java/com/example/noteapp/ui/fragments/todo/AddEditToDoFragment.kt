package com.example.noteapp.ui.fragments.todo

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
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
import com.example.noteapp.databinding.FragmentAddEditToDoBinding
import com.example.noteapp.ui.fragments.note.RemovePasswordDialogFragment
import com.example.noteapp.viewmodels.ToDoViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.util.*

class AddEditToDoFragment : Fragment(), OnItemTodoClickListener {
    private lateinit var todoViewModel: ToDoViewModel
    private var value:Boolean=false
    private lateinit var itemTodoAdapter: ToDoItemAdapter
    private var quit = 1

    private var _binding:FragmentAddEditToDoBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        todoViewModel = ViewModelProvider(requireActivity())[ToDoViewModel::class.java]

        requireActivity().onBackPressedDispatcher.addCallback(
                this,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {

                            //Po naciśnięciu przycisku wstecz sprawdzamy czy lista nie jest pusta
                            if (binding.titleAddEditFragCategory.text.isNotEmpty()) {
                                val categoryName = binding.titleAddEditFragCategory.text.toString()
                                val date = Calendar.getInstance().timeInMillis
                                val color= todoViewModel.selectedCategotyItemColor

                                if (todoViewModel.getSelectedCategotyItem().value == null) {
                                    val category = Category(categoryName, color, false, date)
                                    todoViewModel.insertCategotyItem(category)
                                    Toast.makeText(
                                            requireContext(),
                                            "Categoty created",
                                            Toast.LENGTH_LONG
                                    ).show()

                                    //Jeśli notatka nie jest pusta, ale jest zaznaczona w MainFragment - Aktualizujemy
                                } else {
                                    val selectedCategory = todoViewModel.getSelectedCategotyItem().value!!
                                    val beforeCategory = todoViewModel.categoryItemBeforeChange

                                    val colorr = todoViewModel.selectedCategotyItemColor
                                    val name = binding.titleAddEditFragCategory.text.toString()
                                    val datee = todoViewModel.categoryDate
                                    val isSelectedd = todoViewModel.categoryIsSelected
                                    val isFavourite = todoViewModel.isFavouriteCategory
                                    val hasPassword = todoViewModel.hasPasswordCategory
                                    val password = todoViewModel.passwordCategory
                                    val id = todoViewModel.categoryId

                                    if (name !=  beforeCategory!!.categoryName || colorr != beforeCategory.color||hasPassword!=beforeCategory.hasPassword
                                            || isFavourite!=beforeCategory.isFavoutire) {
                                        val category = Category(name, colorr, isSelectedd, datee, isFavourite, hasPassword, password, ).apply {
                                            rowIdCategory = id
                                        }

                                        todoViewModel.updateCategoryItem(category)
                                    }
                                }
                            }

                            isEnabled = false
                            closeKeyboard()
                            quit = 2
                        if(todoViewModel.isSearchEdit==1) {
                            findNavController().navigate(R.id.action_addEditToDoFragment_to_mainFramgent)
                        } else{
                            findNavController().navigate(R.id.action_addEditToDoFragment_to_searchCategoryFragment)
                        }
                    }
                })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentAddEditToDoBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gradientDrawable:GradientDrawable = binding.todoViewSubtitleIndicator.background as GradientDrawable
        initMiscellaneous()

        todoViewModel.getSelectedCategotyItem().observe(viewLifecycleOwner, androidx.lifecycle.Observer { it ->
            binding.titleAddEditFragCategory.setText(it?.categoryName)

                if((binding.titleAddEditFragCategory.text.isEmpty())) {
                    todoViewModel.selectedCategotyItemColor = "#333333"
                    gradientDrawable.setColor(Color.parseColor(todoViewModel.selectedCategotyItemColor))

                } else {
                    val cat = todoViewModel.getSelectedCategotyItem().value

                    binding.toolbarTitleAddEditTodo.text = getString(R.string.categoryEditorTextView)
                    gradientDrawable.setColor(Color.parseColor(it!!.color))
                    todoViewModel.selectedCategotyItemColor = it.color
                    todoViewModel.categoryName = it.categoryName
                    todoViewModel.categoryDate = it.date
                    todoViewModel.categoryIsSelected = it.isSelected
                    todoViewModel.isFavouriteCategory = it.isFavoutire
                    todoViewModel.hasPasswordCategory = it.hasPassword
                    todoViewModel.passwordCategory = it.password
                    todoViewModel.categoryId = it.rowIdCategory

                    setImagePassword(todoViewModel.hasPasswordCategory)

                    setFavourite(todoViewModel.isFavouriteCategory)

                    todoViewModel.getAllItemsFromCategory(todoViewModel.getSelectedCategotyItem().value!!.rowIdCategory).observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                        updateItems(it)
                    })

                    when(todoViewModel.getSelectedCategotyItem().value?.color){
                        "#333333"-> {
                            itemSelected1()
                        }

                        "#FDBE3B" -> {
                            itemSelected2()
                        }

                        "#FF4842" -> {
                            itemSelected3()
                        }

                        "#ff0266" ->{
                            itemSelected6()
                        }

                        "#3A52FC" -> {
                            itemSelected4()
                        }

                        "#000000" -> {
                            itemSelected5()
                        }
                    }
                }

        })

        binding.includeMiscellaneousTodoItem.favouriteImageCategory.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                if(todoViewModel.isFavouriteCategory){
                    todoViewModel.isFavouriteCategory = false
                    setFavourite(todoViewModel.isFavouriteCategory)
                } else{
                    todoViewModel.isFavouriteCategory = true
                    setFavourite(todoViewModel.isFavouriteCategory)
                }
            }
        })

        binding.includeMiscellaneousTodoItem.imageViewPasswordCategory.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                if(todoViewModel.hasPasswordCategory){
                    val fm = requireActivity().supportFragmentManager
                    val dialogFragment = RemovePasswordDialogFragment()
                    dialogFragment.show(fm, "ppp")
                } else{
                    findNavController().navigate(R.id.action_addEditToDoFragment_to_passwordNoteFragment)
                }
            }
        })

        binding.addTodo.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                val fm = requireActivity().supportFragmentManager
                val dialogFragment = DialogAddToDoFragment()
                dialogFragment.show(fm, "Abc")

                itemTodoAdapter.notifyDataSetChanged()

            }
        })
    }


    private fun initMiscellaneous() {
        val layoutMiscellaneous = binding.includeMiscellaneousTodoItem
        val bottomSheetBehavior = BottomSheetBehavior.from(binding.includeMiscellaneousTodoItem.todoLayoutMiscellaneous)

        layoutMiscellaneous.todoTextMiscellaneous.setOnClickListener {
            if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            } else {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }

        layoutMiscellaneous.todoImageColor1.setOnClickListener {
            todoViewModel.selectedCategotyItemColor = "#333333"
            itemSelected1()
            setSubtitleIndicator(this)
        }

        layoutMiscellaneous.todoImageColor2.setOnClickListener {
            todoViewModel.selectedCategotyItemColor = "#FDBE3B"
            itemSelected2()
            setSubtitleIndicator(this)
        }

        layoutMiscellaneous.todoImageColor3.setOnClickListener {
            todoViewModel.selectedCategotyItemColor = "#FF4842"
            itemSelected3()
            setSubtitleIndicator(this)
        }

        layoutMiscellaneous.todoImageColor4.setOnClickListener {
            todoViewModel.selectedCategotyItemColor = "#3A52FC"
            itemSelected4()
            setSubtitleIndicator(this)
        }

        layoutMiscellaneous.todoImageColor5.setOnClickListener {
            todoViewModel.selectedCategotyItemColor = "#000000"
            itemSelected5()
            setSubtitleIndicator(this)
        }

        layoutMiscellaneous.todoImageColor6.setOnClickListener {
            todoViewModel.selectedCategotyItemColor = "#ff0266"
            itemSelected6()
            setSubtitleIndicator(this)
        }
    }

    private fun itemSelected1() {
            binding.includeMiscellaneousTodoItem.todoImageColor1.setImageResource(R.drawable.ic_done)
            binding.includeMiscellaneousTodoItem.todoImageColor2.setImageResource(0)
            binding.includeMiscellaneousTodoItem.todoImageColor3.setImageResource(0)
            binding.includeMiscellaneousTodoItem.todoImageColor6.setImageResource(0)
            binding.includeMiscellaneousTodoItem.todoImageColor4.setImageResource(0)
            binding.includeMiscellaneousTodoItem.todoImageColor5.setImageResource(0)
        }

        private fun itemSelected2() {
            binding.includeMiscellaneousTodoItem.todoImageColor1.setImageResource(0)
            binding.includeMiscellaneousTodoItem.todoImageColor2.setImageResource(R.drawable.ic_done)
            binding.includeMiscellaneousTodoItem.todoImageColor3.setImageResource(0)
            binding.includeMiscellaneousTodoItem.todoImageColor6.setImageResource(0)
            binding.includeMiscellaneousTodoItem.todoImageColor4.setImageResource(0)
            binding.includeMiscellaneousTodoItem.todoImageColor5.setImageResource(0)
        }

        private fun itemSelected3() {
            binding.includeMiscellaneousTodoItem.todoImageColor1.setImageResource(0)
            binding.includeMiscellaneousTodoItem.todoImageColor2.setImageResource(0)
            binding.includeMiscellaneousTodoItem.todoImageColor3.setImageResource(R.drawable.ic_done)
            binding.includeMiscellaneousTodoItem.todoImageColor6.setImageResource(0)
            binding.includeMiscellaneousTodoItem.todoImageColor4.setImageResource(0)
            binding.includeMiscellaneousTodoItem.todoImageColor5.setImageResource(0)
        }

        private fun itemSelected6() {
            binding.includeMiscellaneousTodoItem.todoImageColor1.setImageResource(0)
            binding.includeMiscellaneousTodoItem.todoImageColor2.setImageResource(0)
            binding.includeMiscellaneousTodoItem.todoImageColor3.setImageResource(0)
            binding.includeMiscellaneousTodoItem.todoImageColor6.setImageResource(R.drawable.ic_done)
            binding.includeMiscellaneousTodoItem.todoImageColor4.setImageResource(0)
            binding.includeMiscellaneousTodoItem.todoImageColor5.setImageResource(0)
        }


        private fun itemSelected4() {
            binding.includeMiscellaneousTodoItem.todoImageColor1.setImageResource(0)
            binding.includeMiscellaneousTodoItem.todoImageColor2.setImageResource(0)
            binding.includeMiscellaneousTodoItem.todoImageColor3.setImageResource(0)
            binding.includeMiscellaneousTodoItem.todoImageColor6.setImageResource(0)
            binding.includeMiscellaneousTodoItem.todoImageColor4.setImageResource(R.drawable.ic_done)
            binding.includeMiscellaneousTodoItem.todoImageColor5.setImageResource(0)
        }

        private fun itemSelected5() {
            binding.includeMiscellaneousTodoItem.todoImageColor1.setImageResource(0)
            binding.includeMiscellaneousTodoItem.todoImageColor2.setImageResource(0)
            binding.includeMiscellaneousTodoItem.todoImageColor3.setImageResource(0)
            binding.includeMiscellaneousTodoItem.todoImageColor6.setImageResource(0)
            binding.includeMiscellaneousTodoItem.todoImageColor4.setImageResource(0)
            binding.includeMiscellaneousTodoItem.todoImageColor5.setImageResource(R.drawable.ic_done)
        }

        fun closeKeyboard() {
            val view = requireActivity().currentFocus
            if (view != null) {
                val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }

    override fun onItemClick(itemsOfList: ItemOfList, position: Int) {
     if(itemsOfList.isDone){
         itemsOfList.isDone = false
         todoViewModel.updateItem(itemsOfList)
     }   else{
         itemsOfList.isDone = true
         todoViewModel.updateItem(itemsOfList)
        }
    }

    override fun onItemLongClick(itemsOfList: ItemOfList, position: Int) {
        todoViewModel.setSelectedItem(itemsOfList)

        val fm = requireActivity().supportFragmentManager
        val dialogFragment = DialogAddToDoFragment()
        dialogFragment.show(fm, "Abc")

        itemTodoAdapter.notifyDataSetChanged()
    }

    private fun updateItems(list: List<ItemOfList>){
        val lm = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)

        binding.recyclerViewTodo.layoutManager = lm

        itemTodoAdapter = ToDoItemAdapter(list, this)

        binding.recyclerViewTodo.adapter = itemTodoAdapter

        val item = object :SwipeToDelete(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
               todoViewModel.deleteItem(itemTodoAdapter.getItemInPosition(viewHolder.adapterPosition))
            }
        }

        val itemTouchHelper = ItemTouchHelper(item)

        itemTouchHelper.attachToRecyclerView(binding.recyclerViewTodo)

        itemTodoAdapter.notifyDataSetChanged()
    }

    private fun setFavourite(boolean: Boolean){
        if(boolean){
            binding.includeMiscellaneousTodoItem.favouriteImageCategory.setColorFilter(Color.parseColor("#FDBE3B"))
        } else{
            binding.includeMiscellaneousTodoItem.favouriteImageCategory.setColorFilter(Color.WHITE)
        }
    }

    private fun setImagePassword(hasPassword: Boolean) {
        if(hasPassword){
            binding.includeMiscellaneousTodoItem.imageViewPasswordCategory.setColorFilter(Color.parseColor("#FF4343"))
            binding.includeMiscellaneousTodoItem.imageViewPasswordCategory.setImageResource(R.drawable.ic_lock_24)
        } else{
            binding.includeMiscellaneousTodoItem.imageViewPasswordCategory.setColorFilter(Color.WHITE)
            binding.includeMiscellaneousTodoItem.imageViewPasswordCategory.setImageResource(R.drawable.ic_baseline_lock)
        }
    }

    override fun onDestroyView() {

        val color = todoViewModel.selectedCategotyItemColor
        val name = binding.titleAddEditFragCategory.text.toString()
        val date = todoViewModel.categoryDate
        val isSelected = todoViewModel.categoryIsSelected
        val isFavourite = todoViewModel.isFavouriteCategory
        val hasPassword = todoViewModel.hasPassword
        val password = todoViewModel.passwordCategory
        val id = todoViewModel.categoryId


        val category = Category(name, color, isSelected, date, isFavourite, hasPassword, password).apply {
            rowIdCategory = id
        }

        todoViewModel.setSelectedCategotyItem(category)

        if(quit==2){
            setOffAddEdit()
        }

        super.onDestroyView()
    }

    private fun setOffAddEdit(){

        todoViewModel.setSelectedCategotyItem(null)
        todoViewModel.categoryItemBeforeChange = null
        todoViewModel.selectedCategotyItemColor = "#333333"
        todoViewModel.categoryName = ""
        todoViewModel.categoryDate = 0
        todoViewModel.categoryIsSelected = false
        todoViewModel.isFavouriteCategory = false
        todoViewModel.hasPassword =false
        todoViewModel.passwordCategory = 0
        todoViewModel.categoryId = 0
        todoViewModel.isSearchEdit = 1
    }

    companion object {
        private fun setSubtitleIndicator(addEditToDoFragment: AddEditToDoFragment){
            val gradientDrawable: GradientDrawable = addEditToDoFragment.binding.todoViewSubtitleIndicator.background as GradientDrawable
            gradientDrawable.setColor(Color.parseColor(addEditToDoFragment.todoViewModel.selectedCategotyItemColor))
        }
    }
}