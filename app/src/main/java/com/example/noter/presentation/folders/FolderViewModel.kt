package com.example.noter.presentation.folders

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noter.domain.model.NoteFolder
import com.example.noter.domain.usecase.note_folders.GetFoldersUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FolderViewModel(
    private val getFoldersUseCase: GetFoldersUseCase
): ViewModel() {

    private val _noteFolders = MutableLiveData<List<NoteFolder>>()
    val noteFolders = _noteFolders as LiveData<List<NoteFolder>>

    fun getFolders() {
        viewModelScope.launch(Dispatchers.IO) {
            _noteFolders.postValue(getFoldersUseCase.execute())
        }
    }

    // I suppose I'll need this in the future as CreateFolderFragment is BottomSheet,
    // and when it makes DB operations and exits, FoldersFragment upd 25.05 what?
    fun addNewFolder(newFolder: NoteFolder) {
        val newMutableList = mutableListOf<NoteFolder>()
        _noteFolders.value?.forEach { newMutableList.add(it) }
        newMutableList.add(newFolder)
        _noteFolders.value = newMutableList.toList()
    }

    fun takeFolderNames() : ArrayList<String> {
        val folderNames = arrayListOf<String>()
        noteFolders.value!!.forEach { folderNames.add(it.title) }

        return folderNames
    }
}