package com.example.noter.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noter.domain.model.NoteFolder
import com.example.noter.domain.usecase.note_folders.GetFoldersUseCase
import com.example.noter.presentation.view.HOME_FRAGMENT_ITEM
import com.example.noter.utils.FabAction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HolderViewModel: ViewModel() {

    private val _fabAction: MutableLiveData<FabAction> = MutableLiveData()
    val fabAction: LiveData<FabAction> = _fabAction

    fun setFabAction(currentItem: Int) {
        val action =
            if (currentItem == HOME_FRAGMENT_ITEM) {
            FabAction.ADD_NOTE
        } else FabAction.ADD_FOLDER
        _fabAction.value = action
    }

    fun resetFabAction() { _fabAction.value = FabAction.EMPTY }
}