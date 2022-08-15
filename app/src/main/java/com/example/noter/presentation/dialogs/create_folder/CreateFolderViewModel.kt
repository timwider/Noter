package com.example.noter.presentation.dialogs.create_folder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noter.domain.model.NoteFolder
import com.example.noter.domain.usecase.note_folders.SaveFolderUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.ArrayList

class CreateFolderViewModel(
    private val saveFolderUseCase: SaveFolderUseCase
): ViewModel() {

    private val _createdFolder: MutableLiveData<NoteFolder> = MutableLiveData()
    val createdFolder: LiveData<NoteFolder> = _createdFolder

    private val _folderNamesList: MutableLiveData<ArrayList<String>> = MutableLiveData()

    fun updateFolderNamesList(folderNamesList: ArrayList<String>) {
        _folderNamesList.value = folderNamesList
    }

    fun checkAndSaveFolder(folderName: String): Boolean {
        return if (!_folderNamesList.value!!.contains(folderName)) {
            _folderNamesList.value!!.add(folderName)

            val newFolder = NoteFolder(id = 0, title = folderName)
            _createdFolder.value = newFolder

            viewModelScope.launch(Dispatchers.IO) {
                saveFolderUseCase.execute(noteFolder = _createdFolder.value!!)
            }
            true
        } else false
    }
}