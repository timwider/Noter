package com.example.noter.presentation.view

import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.noter.R
import com.example.noter.adapters.NotesAdapter
import com.example.noter.databinding.HomeFragmentBinding
import com.example.noter.presentation.view.dialogs.DeleteBottomSheetDialog
import com.example.noter.presentation.viewmodel.HolderViewModel
import com.example.noter.presentation.viewmodel.HomeViewModel
import com.example.noter.utils.*
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

const val NOTE_FRAGMENT_TAG = "NoteFragmentTag"
const val ARGS_CLICKED_NOTE_KEY = "clickedNote"
const val IS_FOLDER_CONTENT_ARGS_KEY = "isFolderContent"
const val FOLDER_NAME_ARGS_KEY = "folderName"

class HomeFragment: Fragment(R.layout.home_fragment) {

    private lateinit var binding: HomeFragmentBinding
    private val homeViewModel: HomeViewModel by sharedViewModel()
    private val holderViewModel: HolderViewModel by sharedViewModel()
    private val rvMapper = AdapterMapper()
    private var isFolderContent = false
    private val folderName by lazy { extractFolderName() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = HomeFragmentBinding.bind(view)

        val rvAdapter = NotesAdapter(
            noteClickListener = { noteRV, imageView -> onNoteClick(noteRV, imageView) },
            noteLongClickListener = { noteRV -> homeViewModel.onFirstNoteSelected(noteRV) }
        )
        val selectionHelper = SelectionHelper(adapter = rvAdapter)
        binding.rvNotes.adapter = rvAdapter
        binding.rvNotes.layoutManager = LinearLayoutManager(requireContext())

        isFolderContent = arguments?.getBoolean(IS_FOLDER_CONTENT_ARGS_KEY) ?: false

        if (isFolderContent) {
            binding.fabAddNoteToFolder.visibility = View.VISIBLE
            binding.fabAddNoteToFolder.setOnClickListener {
                openOrAddNote(true, null, folderName)
            }
        }
        homeViewModel.getNotes(true, folderName)

        registerObservers(adapter = rvAdapter, selectionHelper = selectionHelper)
        super.onViewCreated(view, savedInstanceState)
    }

    private fun registerObservers(selectionHelper: SelectionHelper, adapter: NotesAdapter) {
        homeViewModel.notes.observe(viewLifecycleOwner) { notes ->
            adapter.submitList(rvMapper.listToAdapterModel(notes))
        }

        homeViewModel.selectionModeAction.observe(viewLifecycleOwner) { action ->
            onSelectionModeAction(action, selectionHelper)
        }

        homeViewModel.selectionMode.observe(viewLifecycleOwner) { selectionMode ->
            toggleSelectionMode(selectionMode, selectionHelper)
        }

        holderViewModel.fabAction.observe(viewLifecycleOwner) { action ->
            if (action == FabAction.ADD_NOTE) {
                openOrAddNote(true, null, folderName)
                holderViewModel.resetFabAction()
            }
        }
    }

    private fun onSelectionModeAction(action: SelectionModeAction, selectionHelper: SelectionHelper) {
        if (action == SelectionModeAction.DELETE) {
            showDeleteDialog()
        } else selectionHelper.onSelectionModeAction(action)
    }

    private fun toggleSelectionMode(mode: SelectionMode, selectionHelper: SelectionHelper) {
        when (mode) {
            SelectionMode.ENABLED -> selectionHelper.enableSelection(homeViewModel.getFirstSelectedNoteId())
            SelectionMode.DISABLED -> selectionHelper.changeListSelection(NoteSelectionState.NO_SELECTION)
            else -> {}
        }
    }

    private fun showDeleteDialog() {
        val dialog = DeleteBottomSheetDialog(::handleDeleteAction)
        val args = Bundle()
        args.putString(DELETE_DIALOG_TYPE_ARGS_KEY, DELETE_SELECTED_NOTES)
        dialog.arguments = args
        dialog.show(parentFragmentManager, null)
    }

    // This is called after positive button was clicked in a dialog
    private fun handleDeleteAction() {
        if (homeViewModel.handleDeleteSelectedNotes(folderName)) {
            homeViewModel.disableSelectionMode()
        } else {
            Snackbar.make(requireView(), getString(R.string.select_notes_to_delete), Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun onNoteClick(note: NoteRV, ivSelection: ImageView) {
        when (note.selectionState) {
            NoteSelectionState.SELECTED -> {
                ivSelection.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.selected_vector, null))
                (ivSelection.drawable as AnimatedVectorDrawable).start()
                homeViewModel.removeSelectedNoteId(note.id)
                note.selectionState = NoteSelectionState.NOT_SELECTED
            }
            NoteSelectionState.NOT_SELECTED -> {
                ivSelection.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.unselected_vector, null))
                (ivSelection.drawable as AnimatedVectorDrawable).start()
                homeViewModel.addSelectedNoteId(note.id)
                note.selectionState = NoteSelectionState.SELECTED
            }
            NoteSelectionState.NO_SELECTION -> openOrAddNote(false, note, folderName)
        }
    }

    private fun openOrAddNote(isNewNote: Boolean, note: NoteRV?, folderName: String?) {
        val fragment = NoteFragment()
        val args = Bundle()
        if (!isNewNote) args.putSerializable(CLICKED_NOTE_ARGS_KEY, rvMapper.toDomainModel(note!!))
        if (!folderName.isNullOrEmpty()) args.putString(FOLDER_NAME_ARGS_KEY, folderName)
        args.putBoolean(IS_NEW_NOTE_ARGS_KEY, isNewNote)
        fragment.arguments = args
        navigateToNote(fragment)
    }

    private fun navigateToNote(noteFragment: NoteFragment) {
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.main_fragment_container, noteFragment, NOTE_FRAGMENT_TAG)
            ?.addToBackStack(NOTE_FRAGMENT_TAG)
            ?.commit()
    }

    private fun extractFolderName() : String {
        return if (isFolderContent) {
            arguments?.getString(FOLDER_NAME_ARGS_KEY) ?: ""
        } else ""
    }
}