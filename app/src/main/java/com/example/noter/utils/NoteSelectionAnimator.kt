package com.example.noter.utils

import android.graphics.drawable.AnimatedVectorDrawable
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import com.example.noter.R

class NoteSelectionAnimator {

    fun onNoteSelected(ivSelection: ImageView) {
        ivSelection.setImageDrawable(
            ResourcesCompat.getDrawable(
            ivSelection.resources,
            R.drawable.unselected_vector,
            null
        ))
        (ivSelection.drawable as AnimatedVectorDrawable).start()
    }

    fun onNoteUnselected(ivSelection: ImageView) {
        ivSelection.setImageDrawable(ResourcesCompat.getDrawable(
            ivSelection.resources,
            R.drawable.selected_vector,
            null
        ))
        (ivSelection.drawable as AnimatedVectorDrawable).start()
    }
}