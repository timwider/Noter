package com.example.noter.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noter.domain.model.Note
import com.example.noter.domain.usecase.notes.GetNotesNoFolderUseCase
import com.example.noter.utils.SpanContainer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getNotesNoFolderUseCase: GetNotesNoFolderUseCase
): ViewModel() {

    private val _notes: MutableLiveData<List<Note>> = MutableLiveData()
    val notes = _notes as LiveData<List<Note>>

    private val _spanContainers: MutableLiveData<MutableList<SpanContainer>> = MutableLiveData()
    val spanContainers = _spanContainers as LiveData<MutableList<SpanContainer>>


    fun getNotes() {
        viewModelScope.launch(Dispatchers.IO) {
            _notes.postValue(getNotesNoFolderUseCase.execute())
        }
    }

    fun updateNote(note: Note) {
        val newList = _notes.value?.toMutableList()

        _notes.value?.let { notesList ->
            for (i in notesList) {
                if (i.id == note.id) {
                    val noteIndex = notesList.indexOf(i)
                    newList!![noteIndex] = note
                }
            }
        }

        _notes.value = newList?.toList()
    }

    fun clearNotesListIfPresent() {
        _notes.value = emptyList<Note>().toMutableList()
    }

}