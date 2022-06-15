package com.example.noter.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noter.R
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

    private val _selectionMode = MutableLiveData(SelectionMode.NOT_SET)
    val selectionMode = _selectionMode as LiveData<SelectionMode>

    private val selectedNotesIds = MutableLiveData<MutableList<Int>?>()

    private val _selectionModeAction = MutableLiveData(SelectionModeAction.NOT_SET)
    val selectionModeAction = _selectionModeAction as LiveData<SelectionModeAction>

    fun resolveSelectionModeAction(actionId: Int) {
        when (actionId) {
            R.id.selection_action_select_all ->  _selectionModeAction.value = SelectionModeAction.SELECT_ALL
            R.id.selection_action_delete -> onSelectionModeAction(SelectionModeAction.DELETE)
            R.id.selection_action_cancel ->  onSelectionModeAction(SelectionModeAction.CANCEL)
        }
    }

    private fun onSelectionModeAction(action: SelectionModeAction) {
        _selectionModeAction.value = action
        _selectionMode.value = SelectionMode.DISABLED
    }

    fun deleteNotesByIds(ids: List<Int>) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteNotesByIdUseCase.execute(ids)
        }
    }

    fun resetSelectionModeAction() {
        _selectionModeAction.value = SelectionModeAction.NOT_SET
    }

    // I don't like that setValue is triggered 2 times
    fun getNotes(rewriteValue: Boolean) {

        if (rewriteValue) _notes.value = emptyList<Note>().toMutableList()

        viewModelScope.launch(Dispatchers.IO) {
            _notes.postValue(getNotesNoFolderUseCase.execute())
        }
    }

    fun onFirstNoteSelected(note: NoteRV) {
        if (note.selectionState == NoteSelectionState.NO_SELECTION) note.selectionState = NoteSelectionState.SELECTED
        selectedNotesIds.value?.clear()
        addSelectedNoteId(noteId = note.id)
        _selectionMode.value = SelectionMode.ENABLED
    }

    fun handleDeleteSelectedNotes(): Boolean {
        if (selectedNotesIds.value.isNullOrEmpty()) return false

        deleteNotesByIds(selectedNotesIds.value!!.toList())
        getNotes(rewriteValue = false)
        selectedNotesIds.value?.clear()
        resetSelectionModeAction()
        return true
    }

    fun getFirstSelectedNoteId(): Int = selectedNotesIds.value?.get(0) ?: -1

    fun addSelectedNoteId(noteId: Int) = selectedNotesIds.addAndNotify(noteId)

    fun removeSelectedNoteId(noteId: Int) = selectedNotesIds.removeAndNotify(noteId)
}