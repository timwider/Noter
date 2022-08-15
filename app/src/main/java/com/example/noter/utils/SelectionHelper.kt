package com.example.noter.utils

import android.view.View
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import com.example.noter.R
import com.example.noter.adapters.NotesAdapter

const val NOTE_NOT_FOUND_ID = -1

class SelectionHelper(
    private val adapter: NotesAdapter
) {

    fun onSelectionModeAction(action: SelectionModeAction) {
        when (action) {
            SelectionModeAction.SELECT_ALL -> adapter.changeListSelection(NoteSelectionState.SELECTED)
            SelectionModeAction.CANCEL -> adapter.changeListSelection(NoteSelectionState.NO_SELECTION)
            else -> {}
        }
    }

    fun enableSelection(selectedNoteId: Int, itemPosition: Int) {
        if (selectedNoteId != NOTE_NOT_FOUND_ID) {
            adapter.currentList[selectedNoteId].selectionState = NoteSelectionState.SELECTED
            adapter.notifyItemChanged(itemPosition)
        }
    }
}