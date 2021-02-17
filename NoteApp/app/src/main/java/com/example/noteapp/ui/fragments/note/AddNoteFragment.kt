package com.example.noteapp.ui.fragments.note

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.noteapp.R
import com.example.noteapp.data.Note
import com.example.noteapp.viewmodels.ProfilViewModel
import com.example.noteapp.viewmodels.ViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_add_note.*
import kotlinx.android.synthetic.main.note_edit_layout_miscellaneous.*
import java.util.*

class AddNoteFragment : Fragment() {

    private lateinit var viewModel: ViewModel

    private lateinit var profileViewModel:ProfilViewModel

    private val fbAuth = FirebaseAuth.getInstance()

    private var value: Boolean = false

    private var selectedImagePath: String = ""

    private var quit = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[ViewModel::class.java]

        requireActivity().onBackPressedDispatcher.addCallback(
                this,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        val title = title_addNoteFrag.text.toString()
                        val message = mess_addNoteFrag.text.toString()
                        val date = Calendar.getInstance().timeInMillis
                        val color = viewModel.selectedNoteColor
                        val fontSize = viewModel.selectedFontSize
                        val fontColor = viewModel.selectedFontNote
                        val favourite = viewModel.isFavourite
                        val hasPassword = viewModel.hasPassword
                        val password = viewModel.password

                        if (title.isNotEmpty()||message.isNotEmpty()) {

                            val note = Note(title, message, date, isSelected = false, color,
                                     fontColor, fontSize, favourite, hasPassword, password).apply {
                                rowId = title.hashCode()+message.hashCode()+date.hashCode()*color.hashCode()
                            }

                            viewModel.insertNote(note)

                            if(fbAuth.currentUser!=null) {
                                profileViewModel = ViewModelProvider(requireActivity())[ProfilViewModel::class.java]
                                val noteAsList = listOf(note)
                                profileViewModel.addNotesToCloud(noteAsList)
                            }
                            viewModel.noteState = null
                        }

                        quit = 2
                        isEnabled = false
                        closeKeyboard()
                        findNavController().navigate(R.id.action_addNoteFragment_to_mainFramgent)
                    }
                })
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_note, container, false)
    }

    @SuppressLint("Range")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gradientDrawable: GradientDrawable =
            addNote_viewSubtitleIndicator.background as GradientDrawable

        initMiscellaneous()

        fontSizeL.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                ++viewModel.selectedFontSize
                if (viewModel.selectedFontSize > 5) {
                    viewModel.selectedFontSize = 1
                }
                setFontSize(viewModel.selectedFontSize)
                viewModel.selectedFontSize + 1

                if (viewModel.selectedFontSize > 5) {
                    viewModel.selectedFontSize = 1
                }
            }
        })

        colorFontL.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                ++viewModel.selectedFontNote

                if (viewModel.selectedFontNote > 3) {
                    viewModel.selectedFontNote = 1
                }

                setFontColor(viewModel.selectedFontNote)
                viewModel.selectedFontNote + 1

                if (viewModel.selectedFontNote > 3) {
                    viewModel.selectedFontNote = 1
                }
            }
        })

        back_from_addNote.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                requireActivity().onBackPressed()
            }
        })

        favouriteNoteButton.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                if(viewModel.isFavourite){
                    viewModel.isFavourite = false
                    setFavourite(viewModel.isFavourite)
                } else{
                    viewModel.isFavourite = true
                    setFavourite(viewModel.isFavourite)
                }
            }
        })

        layoutPasswordImage.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                if(viewModel.hasPassword){
                    val fm = requireActivity().supportFragmentManager
                    val dialogFragment = RemovePasswordDialogFragment()
                    dialogFragment.show(fm, "abcc")
                } else{
                    findNavController().navigate(R.id.action_addNoteFragment_to_passwordNoteFragment)
                }
            }
        })


        viewModel.getSelectedNote().observe(viewLifecycleOwner, androidx.lifecycle.Observer {

            if(viewModel.getSelectedNote().value==null) {
                viewModel.selectedNoteColor = "#333333"
                gradientDrawable.setColor(Color.parseColor(viewModel.selectedNoteColor))
                setFavourite(viewModel.isFavourite)

            } else{
                viewModel.noteTitle = it!!.title
                viewModel.noteMessage = it.message
                viewModel.noteDate = it.date
                viewModel.selectedNoteColor = it.color
                viewModel.selectedFontSize = it.fontSize
                viewModel.selectedFontNote = it.fontColor
                viewModel.idNote = it.rowId
                viewModel.hasPassword = it.hasPassword
                viewModel.password = it.password

                setImagePassword(viewModel.hasPassword)
                title_addNoteFrag.setText(viewModel.noteTitle)
                mess_addNoteFrag.setText(viewModel.noteMessage)
                setFontColor(viewModel.selectedFontNote)
                setFontSize(viewModel.selectedFontSize)

                val selectedColor = viewModel.getSelectedNote().value?.color
                gradientDrawable.setColor(Color.parseColor(selectedColor))
                viewModel.selectedNoteColor = viewModel.getSelectedNote().value!!.color

                when (viewModel.getSelectedNote().value?.color) {
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
    }

    fun closeKeyboard() {
        val view = requireActivity().currentFocus
        if (view != null) {
            val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun setSubtitleIndicator() {
        val gradientDrawable: GradientDrawable = addNote_viewSubtitleIndicator.background as GradientDrawable
        gradientDrawable.setColor(Color.parseColor(viewModel.selectedNoteColor))
    }

    fun initMiscellaneous() {
        val layoutMiscellaneous = requireActivity().findViewById<LinearLayout>(R.id.layoutMiscellaneous)
        val bottomSheetBehavior = BottomSheetBehavior.from(requireActivity().findViewById(R.id.layoutMiscellaneous))

        layoutMiscellaneous.findViewById<TextView>(R.id.textMiscellaneous).setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                } else {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                }
            }
        })

        layoutMiscellaneous.findViewById<ImageView>(R.id.imageColor1).setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                viewModel.selectedNoteColor = "#333333"
                itemSelected1()
                setSubtitleIndicator()
            }
        })

        layoutMiscellaneous.findViewById<ImageView>(R.id.imageColor2).setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                viewModel.selectedNoteColor = "#FDBE3B"
                itemSelected2()
                setSubtitleIndicator()
            }
        })

        layoutMiscellaneous.findViewById<ImageView>(R.id.imageColor3).setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                viewModel.selectedNoteColor = "#FF4842"
                itemSelected3()
                setSubtitleIndicator()
            }
        })

        layoutMiscellaneous.findViewById<ImageView>(R.id.imageColor4).setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                viewModel.selectedNoteColor = "#3A52FC"
                itemSelected4()
                setSubtitleIndicator()
            }
        })

        layoutMiscellaneous.findViewById<ImageView>(R.id.imageColor5).setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                viewModel.selectedNoteColor = "#000000"
                itemSelected5()
                setSubtitleIndicator()
            }
        })

        layoutMiscellaneous.findViewById<ImageView>(R.id.imageColor6).setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                viewModel.selectedNoteColor = "#ff0266"
                itemSelected6()
                setSubtitleIndicator()
            }
        })

    }

    fun itemSelected1() {
        imageColor1.setImageResource(R.drawable.ic_done)
        imageColor2.setImageResource(0)
        imageColor3.setImageResource(0)
        imageColor6.setImageResource(0)
        imageColor4.setImageResource(0)
        imageColor5.setImageResource(0)
    }

    fun itemSelected2() {
        imageColor1.setImageResource(0)
        imageColor2.setImageResource(R.drawable.ic_done)
        imageColor3.setImageResource(0)
        imageColor6.setImageResource(0)
        imageColor4.setImageResource(0)
        imageColor5.setImageResource(0)
    }

    fun itemSelected3() {
        imageColor1.setImageResource(0)
        imageColor2.setImageResource(0)
        imageColor3.setImageResource(R.drawable.ic_done)
        imageColor6.setImageResource(0)
        imageColor4.setImageResource(0)
        imageColor5.setImageResource(0)
    }

    fun itemSelected6() {
        imageColor1.setImageResource(0)
        imageColor2.setImageResource(0)
        imageColor3.setImageResource(0)
        imageColor6.setImageResource(R.drawable.ic_done)
        imageColor4.setImageResource(0)
        imageColor5.setImageResource(0)
    }

    fun itemSelected4() {
        imageColor1.setImageResource(0)
        imageColor2.setImageResource(0)
        imageColor3.setImageResource(0)
        imageColor6.setImageResource(0)
        imageColor4.setImageResource(R.drawable.ic_done)
        imageColor5.setImageResource(0)
    }

    fun itemSelected5() {
        imageColor1.setImageResource(0)
        imageColor2.setImageResource(0)
        imageColor3.setImageResource(0)
        imageColor6.setImageResource(0)
        imageColor4.setImageResource(0)
        imageColor5.setImageResource(R.drawable.ic_done)
    }

    /*
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode){
            FishBun.FISHBUN_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK){
                    imageList = data?.getParcelableArrayListExtra(FishBun.INTENT_PATH)!!

                    imageList.forEach {
                        viewModel.pathImage.add(it.toString())
                    }
                    initAdapter(viewModel.pathImage)
                }
            }
        }
    }
     */

    fun setFontColor(colorPath: Int?) {
        when (colorPath) {
            1 -> {
                mess_addNoteFrag.setTextColor(Color.parseColor("#FFFFFF"))
            }

            2 -> {
                mess_addNoteFrag.setTextColor(Color.parseColor("#959595"))
            }

            3 -> {
                mess_addNoteFrag.setTextColor(Color.parseColor("#666666"))
            }
        }
    }

    fun setFontSize(colorSize: Int?) {
        when (colorSize) {
            1 -> {
                mess_addNoteFrag.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F)
            }

            2 -> {
                mess_addNoteFrag.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20F)
            }

            3 -> {
                mess_addNoteFrag.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25F)
            }

            4 -> {
                mess_addNoteFrag.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30F)
            }

            5 -> {
                mess_addNoteFrag.setTextSize(TypedValue.COMPLEX_UNIT_SP, 35F)
            }
        }
    }

    override fun onDestroyView() {
        Log.d("onDepp", "OnDestroy")

        if(quit==2){
            setOffAddEdit()
        }

        val title = title_addNoteFrag.text
        val message = mess_addNoteFrag.text
        val fontSize = viewModel.selectedFontSize
        val fontColor = viewModel.selectedFontNote
        val date = viewModel.noteDate
        val color = viewModel.selectedNoteColor
        val id = viewModel.idNote
        val isFavourite = viewModel.isFavourite
        val hasPassword = viewModel.hasPassword
        val password = viewModel.password

        val note = Note(title.toString(), message.toString(), date, false, color, fontColor, fontSize, isFavourite, hasPassword, password)
        note.rowId = id

        viewModel.setSelectedNote(note)
        super.onDestroyView()
    }

    fun setOffAddEdit(){
        selectedImagePath = ""
        title_addNoteFrag.setText("")
        mess_addNoteFrag.setText("")
        viewModel.setSelectedNote(null)
        viewModel.noteBeforeChange = null
        viewModel.noteTitle = ""
        viewModel.noteMessage=""
        viewModel.noteDate = 1
        viewModel.pathImage = arrayListOf()
        viewModel.selectedFontSize = 3
        viewModel.selectedFontNote = 1
        viewModel.selectedNoteColor = "#333333"
        viewModel.idNote = -1
        viewModel.isFavourite = false
        viewModel.hasPassword = false
        viewModel.password = 0
    }

    fun setFavourite(boolean: Boolean){
        if(boolean){
            favouriteImage.setColorFilter(Color.parseColor("#FDBE3B"))
        } else{
            favouriteImage.setColorFilter(Color.WHITE)
        }
    }

    private fun setImagePassword(hasPassword: Boolean) {
        if(hasPassword){
            imageViewPassword.setColorFilter(Color.parseColor("#FF4343"))
            imageViewPassword.setImageResource(R.drawable.ic_lock_24)
        } else{
            imageViewPassword.setColorFilter(Color.WHITE)
            imageViewPassword.setImageResource(R.drawable.ic_baseline_lock)
        }
    }

    /*
    private fun openImagePicker() {
        val count = 10 - viewModel.pathImage.size
        FishBun.with(this).setImageAdapter(GlideAdapter())
            .setMinCount(1)
            .setMaxCount(count)
            .setAllViewTitle("All")
            .setActionBarTitle("Selected Images")
            .setIsUseDetailView(false)
            .startAlbum()
    }

     */
}