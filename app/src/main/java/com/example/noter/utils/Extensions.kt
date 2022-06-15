package com.example.noter.utils

import androidx.lifecycle.MutableLiveData


fun <T> MutableLiveData<T>.notifyObservers() {
    value = value
}

fun <T> MutableLiveData<MutableList<T>?>.addAndNotify(element: T) {
    if (value != null) {
        value?.add(element)
        notifyObservers()
    } else value = mutableListOf(element)
}

fun <T> MutableLiveData<MutableList<T>?>.removeAndNotify(element: T) {
    value?.remove(element)
    notifyObservers()
}


