package com.example.noteapp.ui.fragments.todo

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.noteapp.R
import com.example.noteapp.data.Category
import com.example.noteapp.data.Note
import com.example.noteapp.viewmodels.ViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.fragment_add_edit_note.*
import kotlinx.android.synthetic.main.fragment_add_edit_to_do.*
import kotlinx.android.synthetic.main.note_layout_miscellaneous.*
import kotlinx.android.synthetic.main.todo_item.*
import kotlinx.android.synthetic.main.todo_layout_miscellaneous.*
import java.util.*

class AddEditToDoFragment : Fragment() {
    private lateinit var viewModel: ViewModel
    private var value:Boolean=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[ViewModel::class.java]

        requireActivity().onBackPressedDispatcher.addCallback(
                this,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        if (value==true) {
                            isEnabled=false
                            requireActivity().onBackPressed()
                        } else{
                            //Po naciśnięciu przycisku wstecz sprawdzamy czy lista nie jest pusta
                            if (title_addEditFragCategory.text.isNotEmpty()) {
                                val categoryName = title_addEditFragCategory.text.toString()
                                val date = Calendar.getInstance().timeInMillis
                                val color= viewModel.selectedCategotyItemColor

                                if (viewModel.getSelectedCategotyItem().value == null) {
                                    val category = Category(categoryName, color, false, date)
                                    viewModel.insertCategotyItem(category)
                                    Toast.makeText(
                                            requireContext(),
                                            "Categoty created",
                                            Toast.LENGTH_LONG
                                    ).show()

                                    //Jeśli notatka nie jest pusta, ale jest zaznaczona w MainFragment - Aktualizujemy
                                } else {
                                    val selectedCategory = viewModel.getSelectedCategotyItem().value!!
                                    if (selectedCategory.categoryName != categoryName || selectedCategory.color != color) {
                                        val category = Category(categoryName, color, false, date).apply {
                                            rowIdCategory = viewModel.getSelectedCategotyItem().value!!.rowIdCategory
                                        }
                                        Toast.makeText(
                                                requireContext(),
                                                "Category updated",
                                                Toast.LENGTH_LONG
                                        ).show()
                                        viewModel.updateCategoryItem(category)
                                    }
                                }
                            } else if (viewModel.getSelectedCategotyItem().value != null) {

                                Toast.makeText(
                                        requireContext(),
                                        "Note deleted",
                                        Toast.LENGTH_LONG
                                )
                                        .show()
                            }

                            /*else {
                        Log.d("adds", "ff")
                        viewModel.deleteOneNote(viewModel.getSelectedNote().value)
                    }
                     */
                            viewModel.setSelectedCategotyItem(null)
                            isEnabled = false
                            closeKeyboard()
                            requireActivity().onBackPressed()
                        }
                    }
                })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_edit_to_do, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gradientDrawable:GradientDrawable = todo_viewSubtitleIndicator.background as GradientDrawable
        initMiscellaneous()

        viewModel.getSelectedCategotyItem().observe(viewLifecycleOwner, androidx.lifecycle.Observer { category ->
            category.let {
                title_addEditFragCategory.setText(it?.categoryName)


                if((title_addEditFragCategory.text.isEmpty())) {
                    viewModel.selectedCategotyItemColor = "#333333"
                    gradientDrawable.setColor(Color.parseColor(viewModel.selectedCategotyItemColor))

                } else {
                    toolbar_Title_AddEditTodo.text = "Category editor"
                    gradientDrawable.setColor(Color.parseColor(viewModel.getSelectedCategotyItem().value?.color))
                    viewModel.selectedCategotyItemColor = viewModel.getSelectedCategotyItem().value!!.color

                    when(viewModel.getSelectedCategotyItem().value?.color){
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
            }
        })
    }

    fun initMiscellaneous() {
        val layoutMiscellaneous = requireActivity().findViewById<LinearLayout>(R.id.todoLayoutMiscellaneous)
        val bottomSheetBehavior = BottomSheetBehavior.from(requireActivity().findViewById(R.id.todoLayoutMiscellaneous))

        layoutMiscellaneous.findViewById<TextView>(R.id.todo_textMiscellaneous).setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                } else {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                }
            }
        })

        layoutMiscellaneous.findViewById<ImageView>(R.id.todo_imageColor1).setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                viewModel.selectedCategotyItemColor = "#333333"
                itemSelected1()
                setSubtitleIndicator()
            }
        })

        layoutMiscellaneous.findViewById<ImageView>(R.id.todo_imageColor2).setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                viewModel.selectedCategotyItemColor = "#FDBE3B"
                itemSelected2()
                setSubtitleIndicator()
            }
        })

        layoutMiscellaneous.findViewById<ImageView>(R.id.todo_imageColor3).setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                viewModel.selectedCategotyItemColor = "#FF4842"
                itemSelected3()
                setSubtitleIndicator()
            }
        })

        layoutMiscellaneous.findViewById<ImageView>(R.id.todo_imageColor4).setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                viewModel.selectedCategotyItemColor = "#3A52FC"
                itemSelected4()
                setSubtitleIndicator()
            }
        })

        layoutMiscellaneous.findViewById<ImageView>(R.id.todo_imageColor5).setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                viewModel.selectedCategotyItemColor = "#000000"
                itemSelected5()
                setSubtitleIndicator()
            }
        })

        layoutMiscellaneous.findViewById<ImageView>(R.id.todo_imageColor6).setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                viewModel.selectedCategotyItemColor = "#ff0266"
                itemSelected6()
                setSubtitleIndicator()
            }
        })
    }

    fun setSubtitleIndicator(){
        val gradientDrawable: GradientDrawable = todo_viewSubtitleIndicator.background as GradientDrawable
        gradientDrawable.setColor(Color.parseColor(viewModel.selectedCategotyItemColor))
    }

        fun itemSelected1() {
            todo_imageColor1.setImageResource(R.drawable.ic_done)
            todo_imageColor2.setImageResource(0)
            todo_imageColor3.setImageResource(0)
            todo_imageColor6.setImageResource(0)
            todo_imageColor4.setImageResource(0)
            todo_imageColor5.setImageResource(0)
        }

        fun itemSelected2() {
            todo_imageColor1.setImageResource(0)
            todo_imageColor2.setImageResource(R.drawable.ic_done)
            todo_imageColor3.setImageResource(0)
            todo_imageColor6.setImageResource(0)
            todo_imageColor4.setImageResource(0)
            todo_imageColor5.setImageResource(0)
        }

        fun itemSelected3() {
            todo_imageColor1.setImageResource(0)
            todo_imageColor2.setImageResource(0)
            todo_imageColor3.setImageResource(R.drawable.ic_done)
            todo_imageColor6.setImageResource(0)
            todo_imageColor4.setImageResource(0)
            todo_imageColor5.setImageResource(0)
        }

        fun itemSelected6() {
            todo_imageColor1.setImageResource(0)
            todo_imageColor2.setImageResource(0)
            todo_imageColor3.setImageResource(0)
            todo_imageColor6.setImageResource(R.drawable.ic_done)
            todo_imageColor4.setImageResource(0)
            todo_imageColor5.setImageResource(0)
        }


        fun itemSelected4() {
            todo_imageColor1.setImageResource(0)
            todo_imageColor2.setImageResource(0)
            todo_imageColor3.setImageResource(0)
            todo_imageColor6.setImageResource(0)
            todo_imageColor4.setImageResource(R.drawable.ic_done)
            todo_imageColor5.setImageResource(0)
        }

        fun itemSelected5() {
            todo_imageColor1.setImageResource(0)
            todo_imageColor2.setImageResource(0)
            todo_imageColor3.setImageResource(0)
            todo_imageColor6.setImageResource(0)
            todo_imageColor4.setImageResource(0)
            todo_imageColor5.setImageResource(R.drawable.ic_done)
        }


        fun closeKeyboard() {
            val view = requireActivity().currentFocus
            if (view != null) {
                val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }


}