package com.example.noter.adapters

import android.view.View
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import com.example.noter.R
import com.example.noter.utils.NoteSelectionState

class NotesAdapterHelper {

    fun setSelectionVisibility(ivSelection: ImageView, selectionState: NoteSelectionState) {

        when (selectionState) {
            NoteSelectionState.SELECTED -> {
                ivSelection.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        ivSelection.context.resources,
                        R.drawable.selected_vector,
                        null
                    )
                )
                ivSelection.visibility = View.VISIBLE
            }
            NoteSelectionState.NOT_SELECTED -> {
                ivSelection.visibility = View.VISIBLE
                ivSelection.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        ivSelection.context.resources,
                        R.drawable.unselected_vector,
                        null
                    )
                )
            }
            NoteSelectionState.NO_SELECTION ->  ivSelection.visibility = View.GONE
        }
    }
}