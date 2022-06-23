package com.example.noter.presentation.view

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.noter.R
import com.example.noter.adapters.FolderNotesAdapter
import com.example.noter.adapters.NotesAdapter
import com.example.noter.databinding.FolderNotesFragmentBinding
import com.example.noter.domain.model.Note
import com.example.noter.domain.model.NoteFolder
import com.example.noter.presentation.viewmodel.FolderNotesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FolderNotesFragment: Fragment(R.layout.folder_notes_fragment) {

    private lateinit var binding: FolderNotesFragmentBinding
    private val folderNotesViewModel: FolderNotesViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FolderNotesFragmentBinding.bind(view)
        binding.toolbarFolderNotes.setOnMenuItemClickListener {
            if (it.itemId == R.id.action_delete_folder) showDeleteFolderDialog()
            true
        }

        arguments?.getString("args_note_folder_key")?.let {
            binding.tvSelectedFolderName.text = it
        }

        setHomeFragment()

        binding.tvBack.setOnClickListener { activity?.onBackPressed() }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun showDeleteFolderDialog() {
        // todo replace dialog
    }

    private fun setHomeFragment() {
        val fragment = HomeFragment()
        val args = Bundle()
        args.putString(FOLDER_NAME_ARGS_KEY, binding.tvSelectedFolderName.text.toString())
        args.putBoolean(IS_FOLDER_CONTENT_ARGS_KEY, true)
        fragment.arguments = args

        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.folder_notes_container, fragment)
            ?.addToBackStack("folderNotesHomeFragment")
            ?.commit()
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
}