package com.example.noter.presentation.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewAnimationUtils
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.noter.R
import com.example.noter.adapters.NotesAdapter
import com.example.noter.databinding.HomeFragmentBinding
import com.example.noter.presentation.viewmodel.HolderViewModel
import com.example.noter.presentation.viewmodel.HomeViewModel
import com.example.noter.utils.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import kotlin.math.hypot

const val NOTE_FRAGMENT_TAG = "NoteFragmentTag"
const val ARGS_CLICKED_NOTE_KEY = "clickedNote"

class HomeFragment: Fragment(R.layout.home_fragment) {
    private lateinit var binding: HomeFragmentBinding
    private val homeViewModel: HomeViewModel by sharedViewModel()
    private val holderViewModel: HolderViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        homeViewModel.clearNotesListIfPresent()
        homeViewModel.getNotes()

        val mapper = RecyclerViewAdapterModelMapper()

        binding = HomeFragmentBinding.bind(view)

        val rvAdapter = NotesAdapter(
            noteClickListener = { noteRV, imageView ->
                onNoteClick(note = noteRV, ivSelection = imageView)
            },
            noteLongClickListener = { noteRV, _ ->
                noteLongClickListener(note = noteRV)
            }
        )

        binding.rvNotes.apply {
            adapter = rvAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        
        homeViewModel.notes.observe(viewLifecycleOwner) { notes ->
            rvAdapter.submitList(mapper.listToAdapterModel(notes))
        }

        homeViewModel.selectionModeAction.observe(viewLifecycleOwner) { action ->
            when (action) {
                SelectionModeAction.SELECT_ALL -> {
                    handleSelectAllAction(rvAdapter)
                }
                SelectionModeAction.CANCEL -> handleCancelAction(rvAdapter)
                SelectionModeAction.DELETE -> {
                    handleDeleteAction(adapter = rvAdapter)
                }
                null -> handleCancelAction(rvAdapter)
            }
        }

        homeViewModel.selectionMode.observe(viewLifecycleOwner) { selectionMode ->
            if (selectionMode == SelectionMode.ENABLED) {
                enableNotesSelectionInLayout(rvAdapter)
            } else clearNotesSelectionInLayout(rvAdapter)
        }

        setupFabActionObserver()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun enableNotesSelectionInLayout(adapter: NotesAdapter) {
        val currentList = adapter.currentList
        val clickedIds = homeViewModel.selectedNotesIds.value

        for (i in currentList) {
            if (clickedIds!!.contains(i.id)) {
                i.selectionState = NoteSelectionState.SELECTED
            } else i.selectionState = NoteSelectionState.NOT_SELECTED
        }
        adapter.submitList(currentList)
        adapter.notifyDataSetChanged()

    }

    private fun clearNotesSelectionInLayout(adapter: NotesAdapter) {
        val currentList = adapter.currentList

        for (note in currentList) {
            note.selectionState = NoteSelectionState.NO_SELECTION
        }
        adapter.submitList(currentList)
        adapter.notifyDataSetChanged()
    }

    // We already selected one note, so it's not null for sure.
    private fun handleSelectAllAction(adapter: NotesAdapter) {
        val rvNotes = adapter.currentList
        for (rvNote in rvNotes) {
            rvNote.selectionState = NoteSelectionState.SELECTED
        }
        adapter.submitList(rvNotes)
        adapter.notifyDataSetChanged()
    }

    private fun handleCancelAction(adapter: NotesAdapter) {
        val rvNotes = adapter.currentList
        for (rvNote in rvNotes) {
            rvNote.selectionState = NoteSelectionState.NO_SELECTION
        }
        adapter.submitList(rvNotes)
    }

    // This is called after positive button was clicked in a dialog
    private fun handleDeleteAction(adapter: NotesAdapter) {
        val rvNotes = adapter.currentList.toMutableList()
        val idsToDelete = mutableListOf<Int>()
        for (note in rvNotes) {
            if (note.selectionState == NoteSelectionState.SELECTED) {
                idsToDelete.add(note.id)
            }
        }
        homeViewModel.deleteNotesByIds(idsToDelete.toList())
        homeViewModel.getNotes()
    }

    private fun noteLongClickListener(note: NoteRV) {
        if (note.selectionState == NoteSelectionState.NO_SELECTION) {
            note.selectionState = NoteSelectionState.SELECTED
            homeViewModel.addSelectedNoteId(noteId = note.id)
            homeViewModel.enableSelectionMode()
        }
    }

    private fun makeNoteSelectedAnimated(ivSelection: ImageView, noteId: Int) {
        ivSelection.setImageDrawable(ResourcesCompat.getDrawable(
            resources,
            R.drawable.unselected_vector,
            null
        ))
        (ivSelection.drawable as AnimatedVectorDrawable).start()
        homeViewModel.addSelectedNoteId(noteId)
    }

    private fun makeNoteUnselected(ivSelection: ImageView, noteId: Int) {
        ivSelection.setImageDrawable(ResourcesCompat.getDrawable(
            resources,
            R.drawable.selected_vector,
            null
        ))
        (ivSelection.drawable as AnimatedVectorDrawable).start()
        homeViewModel.removeSelectedNoteId(noteId)
    }

    private fun setupFabActionObserver() {
        holderViewModel.fabAction.observe(viewLifecycleOwner) { action ->
            if (action == FabAction.ADD_NOTE) {
                onAddNote()
                holderViewModel.resetFabAction()
            }
        }
    }

    private fun onNoteClick(note: NoteRV, ivSelection: ImageView) {

        when (note.selectionState) {

            NoteSelectionState.NOT_SELECTED -> {
                makeNoteSelectedAnimated(ivSelection, note.id)
                note.selectionState = NoteSelectionState.SELECTED
            }

            NoteSelectionState.SELECTED -> {
                makeNoteUnselected(ivSelection, note.id)
                note.selectionState = NoteSelectionState.NOT_SELECTED
            }

            NoteSelectionState.NO_SELECTION -> openNote(note)
        }
    }

    private fun openNote(note: NoteRV) {
        val noteDomain = RecyclerViewAdapterModelMapper().toDomainModel(note)
        val bundle = Bundle()
        bundle.putSerializable(ARGS_CLICKED_NOTE_KEY, noteDomain)
        bundle.putBoolean(IS_NEW_NOTE_ARGS_KEY, false)
        val noteFragment = NoteFragment()
        noteFragment.arguments = bundle
        navigateToNote(noteFragment = noteFragment)
    }

    private fun navigateToNote(noteFragment: NoteFragment) {
        activity?.supportFragmentManager?.let {
            it.beginTransaction()
                .replace(R.id.main_fragment_container, noteFragment, NOTE_FRAGMENT_TAG)
                .addToBackStack(NOTE_FRAGMENT_TAG)
                .commit()
        }
    }

    private fun onAddNote() {
        val noteFragment = NoteFragment()
        val args = Bundle()
        args.putBoolean(IS_NEW_NOTE_ARGS_KEY, true)
        noteFragment.arguments = args
        activity?.supportFragmentManager?.let {
            it.beginTransaction()
                .replace(R.id.main_fragment_container, noteFragment, NOTE_FRAGMENT_TAG)
                .addToBackStack(NOTE_FRAGMENT_TAG)
                .commit()
        }
    }

}