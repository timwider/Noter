package com.example.noter.presentation.view

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.example.noter.R
import com.example.noter.databinding.CreateFolderFragmentBinding
import com.example.noter.presentation.viewmodel.CreateFolderViewModel
import com.example.noter.utils.KeyboardAnimator
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

const val FOLDER_LIST_ARGS = "folder_list_key"

class CreateFolderFragment: BottomSheetDialogFragment() {

    private lateinit var binding: CreateFolderFragmentBinding
    private val createFolderViewModel: CreateFolderViewModel by sharedViewModel()

    override fun onStart() {
        super.onStart()
        dialog?.window?.let { KeyboardAnimator(window = it).start() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AddFolderBottomSheet)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = CreateFolderFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun getFolderNamesFromArgs() {
        arguments?.getStringArrayList(FOLDER_LIST_ARGS)?.let {
            createFolderViewModel.updateFolderNamesList(folderNamesList = it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        getFolderNamesFromArgs()

        binding.btnCreateFolder.setOnClickListener {
            val folderName =  binding.etFolderName.text.toString()
            onCreateFolder(folderName = folderName)
            dialog?.dismiss()
        }

        binding.btnCancel.setOnClickListener {
            dialog?.dismiss()
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun onCreateFolder(folderName: String) {

        if (folderName.isEmpty()) makeSnackbar(messageType = SnackbarMessageType.NO_NAME_PROVIDED)

        val isFolderSaved = createFolderViewModel.checkAndSaveFolder(folderName)

        if (!isFolderSaved) makeSnackbar(SnackbarMessageType.NAME_TAKEN)
    }

    private fun makeSnackbar(messageType: SnackbarMessageType) {

        val message = when (messageType) {
            SnackbarMessageType.NAME_TAKEN -> R.string.folder_name_taken_message
            SnackbarMessageType.NO_NAME_PROVIDED -> R.string.no_folder_name_message
        }
        Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
    }
}

enum class SnackbarMessageType { NAME_TAKEN, NO_NAME_PROVIDED }