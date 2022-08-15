package com.example.noter.presentation.holder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.noter.utils.FabAction

class HolderViewModel: ViewModel() {

    private val _fabAction: MutableLiveData<FabAction> = MutableLiveData()
    val fabAction: LiveData<FabAction> = _fabAction

    private val currentItemVP = MutableLiveData(0)

    fun setFabAction(currentItem: Int) {
        val action =
            if (currentItem == HOME_FRAGMENT_ITEM) {
            FabAction.ADD_NOTE
        } else FabAction.ADD_FOLDER
        _fabAction.value = action
    }
    fun resetFabAction() { _fabAction.value = FabAction.EMPTY }
    fun saveCurrentItem(item: Int) { currentItemVP.value = item }
    fun getCurrentItem(): Int = currentItemVP.value!!
}