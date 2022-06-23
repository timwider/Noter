package com.example.noter.utils

import com.example.noter.adapters.NotesAdapter

const val NOTE_NOT_FOUND_ID = -1

class SelectionHelper(
    private val adapter: NotesAdapter
) {

    fun onSelectionModeAction(action: SelectionModeAction) {
        when (action) {
            SelectionModeAction.SELECT_ALL -> changeListSelection(NoteSelectionState.SELECTED)
            SelectionModeAction.CANCEL -> changeListSelection(NoteSelectionState.NO_SELECTION)
        }
    }


    fun changeListSelection(selectionState: NoteSelectionState) {
        val notes = adapter.currentList
        notes.forEach { it.selectionState = selectionState }
        adapter.submitList(notes)
        adapter.notifyDataSetChanged()
    }

    fun enableSelection(selectedNoteId: Int) {

        if (selectedNoteId != NOTE_NOT_FOUND_ID) {
            val currentNotesList = adapter.currentList
            for (note in currentNotesList) {
                note.selectionState = if (note.id == selectedNoteId) {
                    NoteSelectionState.SELECTED
                } else NoteSelectionState.NOT_SELECTED
            }
            adapter.submitList(currentNotesList)
            adapter.notifyDataSetChanged()
        }
    }

}