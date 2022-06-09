package com.example.noter.presentation.view

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.noter.R
import com.example.noter.adapters.FolderNotesAdapter
import com.example.noter.databinding.FolderNotesFragmentBinding
import com.example.noter.domain.model.Note
import com.example.noter.domain.model.NoteFolder
import com.example.noter.presentation.view.dialogs.AlertDialogType
import com.example.noter.presentation.view.dialogs.CustomAlertDialog
import com.example.noter.presentation.viewmodel.FolderNotesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FolderNotesFragment: Fragment(R.layout.folder_notes_fragment) {

    private lateinit var binding: FolderNotesFragmentBinding
    private val folderNotesViewModel: FolderNotesViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding = FolderNotesFragmentBinding.bind(view)
        val folderNotesAdapter = FolderNotesAdapter { onNoteClick(noteItem = it) }

        binding.rvFolderNotes.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = folderNotesAdapter
        }

        binding.toolbarFolderNotes.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.action_delete_folder -> showDeleteFolderDialog()
            }
            true
        }

        binding.tvBack.setOnClickListener { activity?.onBackPressed() }

        getArgsData()

        binding.btnAddNote.setOnClickListener { onFabClick(folderName = binding.tvSelectedFolderName.text.toString()) }

        folderNotesViewModel.folderNotes.observe(viewLifecycleOwner) {
            folderNotesAdapter.submitList(it)
        }

        super.onViewCreated(view, savedInstanceState)
    }

    private fun showDeleteFolderDialog() {
        val alertDialog = CustomAlertDialog(
            context = requireContext(),
            positiveButtonClickHandler = (::alertDialogPositiveButtonClickHandler),
            dialogType = AlertDialogType.DELETE_FOLDER
        )

        alertDialog.showDialog()
    }

    private fun alertDialogPositiveButtonClickHandler() {
        // first, we delete all notes associated with this folder,
        // then we delete the folder itself and removing current fragment
        binding.tvSelectedFolderName.text.toString().apply {
            folderNotesViewModel.deleteFolderNotes(folderName = this)
            folderNotesViewModel.deleteFolder(noteFolderTitle = this)
        }
        activity?.onBackPressed()
    }

    private fun onNoteClick(noteItem: Note) {

        val bundle = Bundle()
        bundle.putSerializable(ARGS_CLICKED_NOTE_KEY, noteItem)
        val noteFragment = NoteFragment()
        noteFragment.arguments = bundle
        navigateToNote(noteFragment = noteFragment)
    }

    private fun onFabClick(folderName: String) {
        val bundle = Bundle()
        bundle.putString(FOLDER_NAME_ARGS_KEY, folderName)
        val noteFragment = NoteFragment()
        noteFragment.arguments = bundle
        navigateToNote(noteFragment = noteFragment)
    }

    private fun navigateToNote(noteFragment: NoteFragment) {
        activity?.supportFragmentManager?.let {
            it.beginTransaction()
                .replace(R.id.main_fragment_container, noteFragment)
                .addToBackStack(NOTE_FRAGMENT_TAG)
                .commit()
        }
    }

    private fun getArgsData() {
        val noteFolder = arguments?.getSerializable("args_note_folder_key") as NoteFolder
        val folderName = noteFolder.title
        folderNotesViewModel.getFolderNotes(folderName = folderName)
        setFolderNameFromArgs(folderName = folderName)
    }

    private fun setFolderNameFromArgs(folderName: String) {
        binding.tvSelectedFolderName.text = folderName
    }
}