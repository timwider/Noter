package com.example.noter.presentation.view

import android.content.res.Configuration
import android.graphics.Typeface
import android.os.Bundle
import android.text.style.StrikethroughSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.noter.R
import com.example.noter.adapters.NotesAdapter
import com.example.noter.databinding.HomeFragmentBinding
import com.example.noter.domain.model.Note
import com.example.noter.presentation.viewmodel.HolderViewModel
import com.example.noter.presentation.viewmodel.HomeViewModel
import com.example.noter.utils.FabAction
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

const val NOTE_FRAGMENT_TAG = "NoteFragmentTag"
const val ARGS_CLICKED_NOTE_KEY = "clickedNote"

class HomeFragment: Fragment(R.layout.home_fragment) {

    private lateinit var binding: HomeFragmentBinding
    private val homeViewModel: HomeViewModel by sharedViewModel()
    private val holderViewModel: HolderViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        homeViewModel.clearNotesListIfPresent()
        homeViewModel.getNotes()

        binding = HomeFragmentBinding.bind(view)
        val rvAdapter = NotesAdapter { onNoteClick(it)}

        binding.rvNotes.apply {
            adapter = rvAdapter
            layoutManager = LinearLayoutManager(requireContext())
            itemAnimator = null
        }

        homeViewModel.notes.observe(viewLifecycleOwner) { notes ->
            rvAdapter.submitList(notes)
        }

        setupFabActionObserver()

        super.onViewCreated(view, savedInstanceState)
    }

    private fun setupFabActionObserver() {
        holderViewModel.fabAction.observe(viewLifecycleOwner) { action ->
            if (action == FabAction.ADD_NOTE) {
                onAddNote()
                holderViewModel.resetFabAction()
            }
        }
    }

    private fun onNoteClick(note: Note) {
        val bundle = Bundle()
        bundle.putSerializable(ARGS_CLICKED_NOTE_KEY, note)
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