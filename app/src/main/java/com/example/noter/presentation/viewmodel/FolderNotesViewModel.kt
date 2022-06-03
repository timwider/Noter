package com.example.noter.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noter.domain.model.Note
import com.example.noter.domain.usecase.note_folders.DeleteFolderUseCase
import com.example.noter.domain.usecase.notes.DeleteFolderNotesUseCase
import com.example.noter.domain.usecase.notes.GetFolderNotesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FolderNotesViewModel(
    private val getFolderNotesUseCase: GetFolderNotesUseCase,
    private val deleteFolderUseCase: DeleteFolderUseCase,
    private val deleteFolderNotesUseCase: DeleteFolderNotesUseCase
): ViewModel() {

    private val _folderNotes = MutableLiveData<List<Note>>()
    val folderNotes: LiveData<List<Note>> = _folderNotes

    fun getFolderNotes(folderName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _folderNotes.postValue(getFolderNotesUseCase.execute(folderName = folderName))
        }
    }

    fun deleteFolderNotes(folderName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteFolderNotesUseCase.execute(folderName = folderName)
        }
    }

    // It's like suicide, you know? It deletes itself and cleans up after.
    fun deleteFolder(noteFolderTitle: String) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteFolderUseCase.execute(noteFolderTitle = noteFolderTitle)
        }
    }
}