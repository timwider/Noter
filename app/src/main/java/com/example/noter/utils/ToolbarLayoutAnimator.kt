package com.example.noter.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import android.widget.LinearLayout
import com.google.android.material.tabs.TabLayout

const val ALPHA_ITEM_INVISIBLE = 0F
const val ALPHA_ITEM_VISIBLE = 1F
const val MIN_ANIMATION_DURATION = 200L

class ToolbarLayoutAnimator(
    private val selectionLayout: LinearLayout,
    private val tabLayout: TabLayout
) {

    private fun makeTabLayoutAppear(tabLayout: TabLayout) {
        tabLayout.alpha = ALPHA_ITEM_INVISIBLE
        tabLayout.visibility = View.VISIBLE

        tabLayout.animate()
            .alpha(ALPHA_ITEM_VISIBLE)
            .setDuration(MIN_ANIMATION_DURATION)
            .setListener(null)
            .start()

    }

    private fun hideTabLayout() {

        val defaultTranslationY = tabLayout.translationY

        tabLayout.animate()
            .alpha(0F)
            .setDuration(200)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    tabLayout.visibility = View.GONE
                    showSelectionLayout()
                    tabLayout.translationY = defaultTranslationY
                    super.onAnimationEnd(animation)
                }
            })
            .start()
    }

    private fun showSelectionLayout() {

        selectionLayout.alpha = ALPHA_ITEM_INVISIBLE
        selectionLayout.visibility = View.VISIBLE

        selectionLayout.animate()
            .alpha(ALPHA_ITEM_VISIBLE)
            .setDuration(MIN_ANIMATION_DURATION)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator?) {
                    tabLayout.visibility = View.GONE
                    super.onAnimationStart(animation)
                }
            })
            .start()
    }

    private fun hideSelectionLayout() {

        val defaultTranslationY = selectionLayout.translationY

        selectionLayout.animate()
            .alpha(ALPHA_ITEM_INVISIBLE)
            .setDuration(MIN_ANIMATION_DURATION)
            .setListener( object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    selectionLayout.visibility = View.GONE
                    selectionLayout.translationY = defaultTranslationY
                    makeTabLayoutAppear(tabLayout)
                    super.onAnimationEnd(animation)
                }
            })
            .start()
    }

    /**
     * We have two if statements because SelectionMode has a 3rd param: NOT_SET,
     * which is not handled here.
     */
    fun onSelectionModeChanged(selectionMode: SelectionMode) {
        if (selectionMode == SelectionMode.ENABLED) hideTabLayout()
        if (selectionMode == SelectionMode.DISABLED) hideSelectionLayout()
    }
}