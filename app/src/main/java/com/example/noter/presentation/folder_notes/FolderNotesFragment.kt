package com.example.noter.presentation.folder_notes

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import com.example.noter.R
import com.example.noter.databinding.FolderNotesFragmentBinding
import com.example.noter.presentation.dialogs.delete.DeleteBottomSheetDialog
import com.example.noter.presentation.home.FOLDER_NAME_ARGS_KEY
import com.example.noter.presentation.home.HomeFragment
import com.example.noter.presentation.home.IS_FOLDER_CONTENT_ARGS_KEY
import com.example.noter.utils.DELETE_DIALOG_TYPE_ARGS_KEY
import com.example.noter.utils.DELETE_FOLDER
import org.koin.androidx.viewmodel.ext.android.viewModel

const val DELETE_REQUEST_KEY = "requestKey"
const val SHOULD_DELETE_FOLDER = "shouldDeleteFolder"


class FolderNotesFragment: Fragment(R.layout.folder_notes_fragment) {

    private lateinit var binding: FolderNotesFragmentBinding
    private val folderNotesViewModel: FolderNotesViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FolderNotesFragmentBinding.bind(view)
        binding.toolbarFolderNotes.setOnMenuItemClickListener {
            if (it.itemId == R.id.action_delete_folder) showDeleteFolderDialog()
            true
        }
        val folderName = arguments?.getString(FOLDER_NAME_ARGS_KEY) ?: "Безымянная папка"
        binding.tvSelectedFolderName.text = folderName
        folderNotesViewModel.setFolderName(folderName)
        setHomeFragment()

        setFragmentResultListener(DELETE_REQUEST_KEY) { requestKey, bundle ->
            if (requestKey == DELETE_REQUEST_KEY) {
                val shouldDeleteFolder = bundle.getBoolean(SHOULD_DELETE_FOLDER)
                if (shouldDeleteFolder) {
                    folderNotesViewModel.deleteFolder()
                    activity?.onBackPressed()
                }
            }
        }

        binding.tvBack.setOnClickListener { activity?.onBackPressed() }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun showDeleteFolderDialog() {
        val dialog = DeleteBottomSheetDialog()
        dialog.arguments = bundleOf(DELETE_DIALOG_TYPE_ARGS_KEY to DELETE_FOLDER)
        dialog.show(parentFragmentManager, null)
    }

    private fun setHomeFragment() {
        val fragment = HomeFragment()
        val args = Bundle()
        args.putString(FOLDER_NAME_ARGS_KEY, binding.tvSelectedFolderName.text.toString())
        args.putBoolean(IS_FOLDER_CONTENT_ARGS_KEY, true)
        fragment.arguments = args

        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.folder_notes_container, fragment)
            ?.commit()
    }
}