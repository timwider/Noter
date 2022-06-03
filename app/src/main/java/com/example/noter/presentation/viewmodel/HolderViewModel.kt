package com.example.noter.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noter.domain.model.NoteFolder
import com.example.noter.domain.usecase.note_folders.GetFoldersUseCase
import com.example.noter.utils.FabAction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HolderViewModel(
    private val getFoldersUseCase: GetFoldersUseCase
): ViewModel() {

    private val _fabAction: MutableLiveData<FabAction> = MutableLiveData()
    val fabAction: LiveData<FabAction> = _fabAction

    private val noteFolders: MutableLiveData<List<NoteFolder>> = MutableLiveData()

    fun setFabAction(action: FabAction) {
        _fabAction.value = action
    }

    fun resetFabAction() {
        _fabAction.value = FabAction.EMPTY
    }

    fun getFolders() : List<NoteFolder>? {
        viewModelScope.launch(context = Dispatchers.IO) {
            noteFolders.postValue(getFoldersUseCase.execute())
        }
        return noteFolders.value
    }
}