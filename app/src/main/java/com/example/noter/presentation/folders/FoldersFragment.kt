package com.example.noter.presentation.folders

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.noter.R
import com.example.noter.adapters.FoldersAdapter
import com.example.noter.databinding.FoldersFragmentBinding
import com.example.noter.presentation.dialogs.create_folder.CreateFolderViewModel
import com.example.noter.presentation.folder_notes.FolderNotesFragment
import com.example.noter.presentation.holder.CREATE_FOLDER_FRAGMENT_TAG
import com.example.noter.presentation.holder.HolderViewModel
import com.example.noter.presentation.home.FOLDER_NAME_ARGS_KEY
import com.example.noter.presentation.dialogs.create_folder.CreateFolderFragment
import com.example.noter.presentation.dialogs.create_folder.FOLDER_LIST_ARGS
import com.example.noter.utils.FabAction
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FoldersFragment: Fragment(R.layout.folders_fragment) {

    private lateinit var binding: FoldersFragmentBinding
    private val createFolderViewModel: CreateFolderViewModel by sharedViewModel()
    private val folderViewModel: FolderViewModel by viewModel()
    private val holderViewModel: HolderViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding = FoldersFragmentBinding.bind(view)

        val foldersAdapter = FoldersAdapter { onFolderClick(folderName = it.title) }

        binding.rvNoteFolders.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = foldersAdapter
        }

        folderViewModel.getFolders()

        setupFabActionObserver()

        folderViewModel.noteFolders.observe(viewLifecycleOwner) {
            val reversedList = it.reversed()
            foldersAdapter.submitList(reversedList)
        }

        createFolderViewModel.createdFolder.observe(viewLifecycleOwner) {
            folderViewModel.addNewFolder(newFolder = it)
        }

        super.onViewCreated(view, savedInstanceState)
    }

    private fun setupFabActionObserver() {
        holderViewModel.fabAction.observe(viewLifecycleOwner) { action ->
            if (action == FabAction.ADD_FOLDER) {
                onAddFolder()
                holderViewModel.resetFabAction()
            }
        }
    }

    private fun onAddFolder() {
        val createFolderFragment = CreateFolderFragment()
        val folderNames = folderViewModel.takeFolderNames()
        val bundle = Bundle()
        bundle.putStringArrayList(FOLDER_LIST_ARGS, folderNames)
        createFolderFragment.arguments = bundle

        activity?.supportFragmentManager?.let { fragmentManager ->
            createFolderFragment.showNow(
                fragmentManager,
                CREATE_FOLDER_FRAGMENT_TAG
            )
        }
    }

    private fun onFolderClick(folderName: String) {

        val bundle = Bundle()
        bundle.putString(FOLDER_NAME_ARGS_KEY, folderName)
        val folderNotesFragment = FolderNotesFragment()
        folderNotesFragment.arguments = bundle

        activity?.supportFragmentManager?.let {
            it.beginTransaction()
                .replace(R.id.main_fragment_container, folderNotesFragment)
                .addToBackStack(null)
                .commit()
        }
    }
}