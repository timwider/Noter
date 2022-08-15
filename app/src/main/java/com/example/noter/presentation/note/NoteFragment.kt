package com.example.noter.presentation.note

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.view.children
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import com.example.noter.R
import com.example.noter.databinding.NoteFragmentBinding
import com.example.noter.domain.model.Note
import com.example.noter.presentation.dialogs.create_notification.CreateNotificationFragment
import com.example.noter.presentation.dialogs.delete.DeleteBottomSheetDialog
import com.example.noter.presentation.folder_notes.DELETE_REQUEST_KEY
import com.example.noter.presentation.folder_notes.SHOULD_DELETE_FOLDER
import com.example.noter.presentation.home.FOLDER_NAME_ARGS_KEY
import com.example.noter.utils.*
import org.koin.androidx.viewmodel.ext.android.viewModel

const val CLICKED_NOTE_ARGS_KEY = "clickedNote"
const val IS_NEW_NOTE_ARGS_KEY = "isNewNote"

class NoteFragment: Fragment(R.layout.note_fragment) {

    private val noteViewModel: NoteViewModel by viewModel()
    private val textFormatter: TextFormatter by lazy { initTextFormatter() }
    private lateinit var binding: NoteFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding = NoteFragmentBinding.bind(view)

        noteViewModel.setIsNewNote(value = arguments?.getBoolean(IS_NEW_NOTE_ARGS_KEY) ?: true)

        if (!noteViewModel.isNewNote()) setDataForExistingNote() else setDataForNewNote()

        showKeyboard()

        arguments?.getString(FOLDER_NAME_ARGS_KEY)?.let { noteViewModel.setFolderName(name = it) }

        setClickListeners()

        toggleMenuVisibilityOnTextChanged()

        setFragmentResultListener(DELETE_REQUEST_KEY) { _, bundle ->
            if (bundle.getBoolean(SHOULD_DELETE_FOLDER)) deleteNoteAndExit()
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun showKeyboard() {
        binding.etNoteContent.requestFocus()
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.etNoteContent, 0)
    }

    private fun setDataForExistingNote() {

        noteViewModel.setNote(note = arguments?.getSerializable(CLICKED_NOTE_ARGS_KEY) as Note)

        binding.tvNoteDate.text = noteViewModel.getNoteDateCreated()

        val spannedText = textFormatter.setSpansFromContainers(
            containers = noteViewModel.getNoteSpans(),
            text = noteViewModel.getNote().content
        )
        binding.etNoteContent.setText(spannedText, TextView.BufferType.SPANNABLE)
    }

    private fun setDataForNewNote() {
        val currentDate = Calendar().getCurrentDate(resources)
        noteViewModel.setNoteDateCreated(currentDate)
        binding.tvNoteDate.text = currentDate
    }

    private fun setClickListeners() {

        binding.toolbar.setOnMenuItemClickListener { toolbarMenuItemClickListener(it); true }

        binding.backButton.setOnClickListener { parentFragmentManager.popBackStack() }

        for (child in binding.noteMenuParent.children) {
            child.setOnClickListener { launchTextFormatter(textFormatter.setTypeFromId(it.id)) }
        }
    }

    private fun toggleMenuVisibilityOnTextChanged() {
        val actionsMenu = binding.toolbar.menu.findItem(R.id.menu_dropdown)
        binding.etNoteContent.addTextChangedListener {
            it?.let { actionsMenu.isVisible = it.isNotBlank() }
        }
    }

    private fun toolbarMenuItemClickListener(item: MenuItem?) {
        when(item?.itemId) {
            R.id.action_share_note -> shareNote(binding.etNoteContent.text.toString())
            R.id.action_delete_note -> showDeleteBottomSheet()
            R.id.action_create_notification -> showNotificationFragment()
        }
    }

    private fun showNotificationFragment() {
        CreateNotificationFragment().show(parentFragmentManager, null)
    }

    private fun showDeleteBottomSheet() {
        val dialog = DeleteBottomSheetDialog()
        val args = Bundle()
        args.putString(DELETE_DIALOG_TYPE_ARGS_KEY, DELETE_NOTE)
        dialog.arguments = args
        dialog.show(parentFragmentManager, null)
    }

    //todo i shouldn't pass all note content, just selected text
    private fun launchTextFormatter(styleSpan: TextFormatterType) {
        if (binding.etNoteContent.hasSelection()) {
            val text = binding.etNoteContent.text as Spannable
            val formattedText = textFormatter.formatText(
                text, styleSpan, binding.etNoteContent.selectionStart, binding.etNoteContent.selectionEnd)
            binding.etNoteContent.setText(formattedText, TextView.BufferType.SPANNABLE)
        }
    }

    private fun shareNote(noteContent: String)  {
        val intent = IntentHelper().createShareNoteIntent(noteContent)
        startActivity(intent)
    }

    private fun deleteNoteAndExit() {
        if (!noteViewModel.isNewNote()) noteViewModel.deleteOldNote() else binding.etNoteContent.text.clear()
        parentFragmentManager.popBackStack()
    }

    override fun onStop() {
        val noteContent = binding.etNoteContent.text.toString()
        val spannableText = binding.etNoteContent.text as Spannable
        noteViewModel.resolveNoteOnStop(noteContent, textFormatter.grabAllSpans(spannableText))
        super.onStop()
    }

    private fun initTextFormatter(): TextFormatter {
        return TextFormatter(noteViewModel.getNoteSpans())
    }
}