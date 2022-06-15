package com.example.noter.utils

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.noter.presentation.view.ARGS_CLICKED_NOTE_KEY
import com.example.noter.presentation.view.IS_NEW_NOTE_ARGS_KEY
import com.example.noter.presentation.view.NoteFragment

class FragmentProvider {

    fun provideNoteFragment(isNewNote: Boolean, note: NoteRV?): NoteFragment {
        val noteFragment = NoteFragment()
        val args = Bundle()
        args.putBoolean(IS_NEW_NOTE_ARGS_KEY, isNewNote)
        if (!isNewNote) {
            val noteDomain = RecyclerViewAdapterModelMapper().toDomainModel(note!!)
            args.putSerializable(ARGS_CLICKED_NOTE_KEY, noteDomain)
        }
        noteFragment.arguments = args
        return noteFragment
    }
}