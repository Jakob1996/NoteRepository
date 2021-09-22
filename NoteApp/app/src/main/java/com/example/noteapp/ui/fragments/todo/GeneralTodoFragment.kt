package com.example.noteapp.ui.fragments.todo

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
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
import com.example.noteapp.viewmodels.ToDoViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.auth.FirebaseAuth
import fadeIn
import fadeOut
import kotlinx.coroutines.*
import java.util.*

class GeneralTodoFragment : BaseFragment(), OnItemTodoClickListener, Navigation {

    private lateinit var todoViewModel: ToDoViewModel
    private var value: Boolean = false
    private lateinit var itemTodoAdapter: ToDoItemAdapter
    private var quit = 1
    private val auth = FirebaseAuth.getInstance()

    private var _binding: FragmentGeneralTodoBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        todoViewModel = ViewModelProvider(requireActivity())[ToDoViewModel::class.java]
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

        val gradientDrawable =
            binding.fragmentGeneralTodoSubtitleIndicatorV.background as GradientDrawable
        initMiscellaneous()

        todoViewModel.getSelectedCategotyItem()
            .observe(viewLifecycleOwner, { it ->
                binding.fragmentGeneralTodoTitleEt.setText(it?.categoryName)

                if ((binding.fragmentGeneralTodoTitleEt.text.isEmpty())) {
                    todoViewModel.selectedCategotyItemColor = "#333333"
                    gradientDrawable.setColor(Color.parseColor(todoViewModel.selectedCategotyItemColor))

                } else {
                    binding.fragmentGeneralTodoToolbarTitleTv.text =
                        getString(R.string.categoryEditorTextView)
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


                    when (todoViewModel.getSelectedCategotyItem().value?.color) {
                        "#333333" -> {
                            itemSelected1()
                        }

                        "#FDBE3B" -> {
                            itemSelected2()
                        }

                        "#FF4842" -> {
                            itemSelected3()
                        }

                        "#ff0266" -> {
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

        binding.fragmentGeneralTodoCastomizer.favouriteImageCategory.setOnClickListener {
            if (todoViewModel.isFavouriteCategory) {
                todoViewModel.isFavouriteCategory = false
                setFavourite(todoViewModel.isFavouriteCategory)
            } else {
                todoViewModel.isFavouriteCategory = true
                setFavourite(todoViewModel.isFavouriteCategory)
            }
        }

        binding.fragmentGeneralTodoCastomizer.imageViewPasswordCategory.setOnClickListener {
            if (todoViewModel.hasPasswordCategory) {
                val fm = requireActivity().supportFragmentManager
                val dialogFragment = RemovePasswordDialogFragment()
                dialogFragment.show(fm, "ppp")
            } else {
                findNavController().navigate(R.id.action_addEditToDoFragment_to_passwordNoteFragment)
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
            BottomSheetBehavior.from(binding.fragmentGeneralTodoCastomizer.todoLayoutMiscellaneous)

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
        binding.fragmentGeneralTodoCastomizer.run {
            todoImageColor1.setImageResource(R.drawable.ic_done)
            todoImageColor2.setImageResource(0)
            todoImageColor3.setImageResource(0)
            todoImageColor6.setImageResource(0)
            todoImageColor4.setImageResource(0)
            todoImageColor5.setImageResource(0)
        }
    }

    private fun itemSelected2() {

        binding.fragmentGeneralTodoCastomizer.run {
            todoImageColor1.setImageResource(0)
            todoImageColor2.setImageResource(R.drawable.ic_done)
            todoImageColor3.setImageResource(0)
            todoImageColor6.setImageResource(0)
            todoImageColor4.setImageResource(0)
            todoImageColor5.setImageResource(0)
        }
    }

    private fun itemSelected3() {
        binding.fragmentGeneralTodoCastomizer.run {
            todoImageColor1.setImageResource(0)
            todoImageColor2.setImageResource(0)
            todoImageColor3.setImageResource(R.drawable.ic_done)
            todoImageColor6.setImageResource(0)
            todoImageColor4.setImageResource(0)
            todoImageColor5.setImageResource(0)
        }
    }

    private fun itemSelected6() {
        binding.fragmentGeneralTodoCastomizer.run {
            todoImageColor1.setImageResource(0)
            todoImageColor2.setImageResource(0)
            todoImageColor3.setImageResource(0)
            todoImageColor6.setImageResource(R.drawable.ic_done)
            todoImageColor4.setImageResource(0)
            todoImageColor5.setImageResource(0)
        }
    }


    private fun itemSelected4() {
        binding.fragmentGeneralTodoCastomizer.run {
            todoImageColor1.setImageResource(0)
            todoImageColor2.setImageResource(0)
            todoImageColor3.setImageResource(0)
            todoImageColor6.setImageResource(0)
            todoImageColor4.setImageResource(R.drawable.ic_done)
            todoImageColor5.setImageResource(0)
        }
    }

    private fun itemSelected5() {
        binding.fragmentGeneralTodoCastomizer.run {
            todoImageColor1.setImageResource(0)
            todoImageColor2.setImageResource(0)
            todoImageColor3.setImageResource(0)
            todoImageColor6.setImageResource(0)
            todoImageColor4.setImageResource(0)
            todoImageColor5.setImageResource(R.drawable.ic_done)
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
                todoViewModel.deleteItem(itemTodoAdapter.getItemInPosition(viewHolder.adapterPosition))
            }
        }

        val itemTouchHelper = ItemTouchHelper(item)

        itemTouchHelper.attachToRecyclerView(binding.fragmentGeneralTodoRv)

        itemTodoAdapter.notifyDataSetChanged()
    }

    private fun setFavourite(boolean: Boolean) {
        if (boolean) {
            binding.fragmentGeneralTodoCastomizer.favouriteImageCategory.setColorFilter(
                Color.parseColor(
                    "#FDBE3B"
                )
            )
        } else {
            binding.fragmentGeneralTodoCastomizer.favouriteImageCategory.setColorFilter(Color.WHITE)
        }
    }

    private fun setImagePassword(hasPassword: Boolean) {
        if (hasPassword) {
            binding.fragmentGeneralTodoCastomizer.imageViewPasswordCategory.setColorFilter(
                Color.parseColor(
                    "#FF4343"
                )
            )
            binding.fragmentGeneralTodoCastomizer.imageViewPasswordCategory.setImageResource(R.drawable.ic_lock_24)
        } else {
            binding.fragmentGeneralTodoCastomizer.imageViewPasswordCategory.setColorFilter(Color.WHITE)
            binding.fragmentGeneralTodoCastomizer.imageViewPasswordCategory.setImageResource(R.drawable.ic_baseline_lock)
        }
    }

    override fun onBackPress() {
        val color = todoViewModel.selectedCategotyItemColor
        val name = binding.fragmentGeneralTodoTitleEt.text.toString()
        val date = todoViewModel.categoryDate
        val isSelected = todoViewModel.categoryIsSelected
        val isFavourite = todoViewModel.isFavouriteCategory
        val hasPassword = todoViewModel.hasPassword
        val password = todoViewModel.passwordCategory
        val id = todoViewModel.categoryId


        val category =
            Category(name, color, isSelected, date, isFavourite, hasPassword, password).apply {
                rowIdCategory = id
            }

        todoViewModel.setSelectedCategotyItem(category)

        if (quit == 2) {
            setOffAddEdit()
        }

        quit()
    }

    private fun setOffAddEdit() {
        todoViewModel.run {
            setSelectedCategotyItem(null)
            categoryItemBeforeChange = null
            selectedCategotyItemColor = "#333333"
            categoryName = ""
            categoryDate = 0
            categoryIsSelected = false
            isFavouriteCategory = false
            hasPassword = false
            passwordCategory = 0
            categoryId = 0
            isSearchEdit = 1
        }
    }

    companion object {
        private fun setSubtitleIndicator(addEditToDoFragment: GeneralTodoFragment) {
            val gradientDrawable: GradientDrawable =
                addEditToDoFragment.binding.fragmentGeneralTodoSubtitleIndicatorV.background as GradientDrawable
            gradientDrawable.setColor(Color.parseColor(addEditToDoFragment.todoViewModel.selectedCategotyItemColor))
        }
    }

    private fun backTransaction() {
        popBackStack("CatFrag", requireActivity().supportFragmentManager, false)
    }

    private fun quit() {
        //Po naciśnięciu przycisku wstecz sprawdzamy czy lista nie jest pusta
        if (binding.fragmentGeneralTodoTitleEt.text.isNotEmpty()) {
            val categoryName = binding.fragmentGeneralTodoTitleEt.text.toString()
            val date = Calendar.getInstance().timeInMillis
            val color = todoViewModel.selectedCategotyItemColor

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
                val name = binding.fragmentGeneralTodoTitleEt.text.toString()
                val datee = todoViewModel.categoryDate
                val isSelectedd = todoViewModel.categoryIsSelected
                val isFavourite = todoViewModel.isFavouriteCategory
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
            }
        }

        abc {
            print(it)
        }

        closeKeyboard()
        quit = 2
        if (todoViewModel.isSearchEdit == 1) {
            backTransaction()
        } else {
            findNavController().navigate(R.id.action_addEditToDoFragment_to_searchCategoryFragment)
        }
    }

    private fun abc(action: (Boolean) -> Unit) {
        action(false)
    }
}