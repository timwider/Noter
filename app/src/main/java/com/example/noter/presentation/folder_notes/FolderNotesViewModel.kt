package com.example.noter.presentation.folder_notes

import androidx.lifecycle.*
import com.example.noter.domain.usecase.note_folders.DeleteFolderUseCase
import com.example.noter.domain.usecase.notes.DeleteFolderNotesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FolderNotesViewModel(
    private val deleteFolderUseCase: DeleteFolderUseCase,
    private val deleteFolderNotesUseCase: DeleteFolderNotesUseCase
): ViewModel() {

    private val folderName = MutableLiveData<String>()

    fun setFolderName(name: String) {
        folderName.value = name
    }

    fun getFolderName() = folderName.value

    fun deleteFolderNotes(folderName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteFolderNotesUseCase.execute(folderName)
        }
    }

    /**
     * It deletes current folder and the notes inside.
     */
    fun deleteFolder() {
        viewModelScope.launch(Dispatchers.IO) {
            deleteFolderUseCase.execute(noteFolderTitle = folderName.value!!)
        }
    }
}