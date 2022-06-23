package com.example.noter.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noter.domain.usecase.note_folders.DeleteFolderUseCase
import com.example.noter.domain.usecase.notes.DeleteFolderNotesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FolderNotesViewModel(
    private val deleteFolderUseCase: DeleteFolderUseCase,
    private val deleteFolderNotesUseCase: DeleteFolderNotesUseCase
): ViewModel() {

    fun deleteFolderNotes(folderName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteFolderNotesUseCase.execute(folderName)
        }
    }

    fun deleteFolder(noteFolderTitle: String) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteFolderUseCase.execute(noteFolderTitle = noteFolderTitle)
        }
    }
}