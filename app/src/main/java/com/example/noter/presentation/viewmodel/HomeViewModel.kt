package com.example.noter.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noter.R
import com.example.noter.domain.model.Note
import com.example.noter.domain.usecase.notes.DeleteFolderNotesUseCase
import com.example.noter.domain.usecase.notes.DeleteNotesByIdUseCase
import com.example.noter.domain.usecase.notes.GetFolderNotesUseCase
import com.example.noter.domain.usecase.notes.GetNotesNoFolderUseCase
import com.example.noter.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getNotesNoFolderUseCase: GetNotesNoFolderUseCase,
    private val deleteNotesByIdUseCase: DeleteNotesByIdUseCase,
    private val getFolderNotesUseCase: GetFolderNotesUseCase,
): ViewModel() {

    private val _notes: MutableLiveData<List<Note>> = MutableLiveData()
    val notes = _notes as LiveData<List<Note>>

    private val _selectionMode = MutableLiveData(SelectionMode.NOT_SET)
    val selectionMode = _selectionMode as LiveData<SelectionMode>

    private val selectedNotesIds = MutableLiveData<MutableList<Int>?>()

    private val _selectionModeAction = MutableLiveData(SelectionModeAction.NOT_SET)
    val selectionModeAction = _selectionModeAction as LiveData<SelectionModeAction>

    fun resolveSelectionModeAction(actionId: Int) {
        when (actionId) {
            R.id.selection_action_select_all -> setSelectionModeAction(SelectionModeAction.SELECT_ALL)
            R.id.selection_action_delete -> setSelectionModeAction(SelectionModeAction.DELETE)
            R.id.selection_action_cancel -> setSelectionModeAction(SelectionModeAction.CANCEL)
        }
    }

    private fun setSelectionModeAction(action: SelectionModeAction) {
        if (action == SelectionModeAction.CANCEL) {
            disableSelectionMode()
        } else _selectionModeAction.value = action
    }

    fun disableSelectionMode() { _selectionMode.value = SelectionMode.DISABLED }

    private fun deleteNotesByIds(ids: List<Int>) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteNotesByIdUseCase.execute(ids)
        }
    }

    private fun deleteFolderNotes() {

    }

    private fun resetSelectionModeAction() {
        _selectionModeAction.value = SelectionModeAction.NOT_SET
    }

    // I don't like that setValue is triggered 2 times todo change it
    fun getNotes(rewriteValue: Boolean, folderName: String?) {

        if (rewriteValue) _notes.value = emptyList<Note>().toMutableList()

        if (folderName.isNullOrEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                _notes.postValue(getNotesNoFolderUseCase.execute())
            }
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                _notes.postValue(getFolderNotesUseCase.execute(folderName))
            }
        }
    }

    fun onFirstNoteSelected(note: NoteRV) {
        if (note.selectionState == NoteSelectionState.NO_SELECTION) note.selectionState = NoteSelectionState.SELECTED
        selectedNotesIds.value?.clear()
        addSelectedNoteId(noteId = note.id)
        _selectionMode.value = SelectionMode.ENABLED
    }

    fun handleDeleteSelectedNotes(folderName: String): Boolean {
        if (selectedNotesIds.value.isNullOrEmpty()) return false

        deleteNotesByIds(selectedNotesIds.value!!.toList())
        getNotes(rewriteValue = false, folderName)
        selectedNotesIds.value?.clear()
        resetSelectionModeAction()
        return true
    }

    fun getFirstSelectedNoteId(): Int {
        return if (selectedNotesIds.value.isNullOrEmpty()) {
            NOTE_NOT_FOUND_ID
        } else selectedNotesIds.value?.get(0) ?: NOTE_NOT_FOUND_ID
    }

    fun addSelectedNoteId(noteId: Int) = selectedNotesIds.addAndNotify(noteId)

    fun removeSelectedNoteId(noteId: Int) = selectedNotesIds.removeAndNotify(noteId)
}