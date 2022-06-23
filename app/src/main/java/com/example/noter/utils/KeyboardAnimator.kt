package com.example.noter.utils

import androidx.transition.TransitionManager
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.transition.ChangeBounds

class KeyboardAnimator(private val window: Window) {

    private val sceneRoot: ViewGroup? by lazy(LazyThreadSafetyMode.NONE) {
        window.decorView.findViewById<ViewGroup?>(Window.ID_ANDROID_CONTENT).parent as? ViewGroup
    }

    private val insetsListener = View.OnApplyWindowInsetsListener { view, windowInsets ->
        sceneRoot?.let { TransitionManager.beginDelayedTransition(it, ChangeBounds()) }
        return@OnApplyWindowInsetsListener view.onApplyWindowInsets(windowInsets)
    }

    fun start() = window.decorView.setOnApplyWindowInsetsListener(insetsListener)

    fun end() = window.decorView.setOnApplyWindowInsetsListener(null)

}