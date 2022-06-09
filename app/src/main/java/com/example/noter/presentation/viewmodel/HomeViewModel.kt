package com.example.noter.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noter.domain.model.Note
import com.example.noter.domain.usecase.notes.DeleteNotesByIdUseCase
import com.example.noter.domain.usecase.notes.GetNotesNoFolderUseCase
import com.example.noter.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getNotesNoFolderUseCase: GetNotesNoFolderUseCase,
    private val deleteNotesByIdUseCase: DeleteNotesByIdUseCase
): ViewModel() {

    private val _notes: MutableLiveData<List<Note>> = MutableLiveData()
    val notes = _notes as LiveData<List<Note>>

    private val _spanContainers: MutableLiveData<MutableList<SpanContainer>> = MutableLiveData()
    val spanContainers = _spanContainers as LiveData<MutableList<SpanContainer>>

    private val _selectionMode = MutableLiveData<SelectionMode>()
    val selectionMode = _selectionMode as LiveData<SelectionMode>

    val selectedNotesIds = MutableLiveData<MutableList<Int>?>()

    private val _selectionModeAction = MutableLiveData<SelectionModeAction>()
    val selectionModeAction = _selectionModeAction as LiveData<SelectionModeAction>

    fun setSelectionModeAction(action: SelectionModeAction) {
        _selectionModeAction.value = action
    }

    fun enableSelectionMode() {
        _selectionMode.value = SelectionMode.ENABLED
    }

    fun disableSelectionMode() {
        _selectionMode.value = SelectionMode.DISABLED
    }

    fun addSelectedNoteId(noteId: Int) {
        var newList = selectedNotesIds.value
        if (newList == null) {
            newList = mutableListOf(noteId)
        } else newList.add(noteId)
        selectedNotesIds.value = newList
    }

    fun removeSelectedNoteId(noteId: Int) {
        val newList = selectedNotesIds.value
        newList?.remove(noteId)
        selectedNotesIds.value = newList
    }

    fun deleteNotesByIds(ids: List<Int>) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteNotesByIdUseCase.execute(ids)
        }
    }

    fun getNotes() {
        viewModelScope.launch(Dispatchers.IO) {
            _notes.postValue(getNotesNoFolderUseCase.execute())
        }
    }

    fun clearNotesListIfPresent() {
        _notes.value = emptyList<Note>().toMutableList()
    }

    fun updateNotesList(newList: List<Note>) {
        _notes.value = newList
    }

}