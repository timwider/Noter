package com.example.noter.presentation.dialogs.create_notification

import android.view.MotionEvent
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior

/**
 * Doesn't work. Maybe I need to override some more methods or dig into this little deeper.
 */
class CreateNotificationBottomSheetBehavior<V: View>: BottomSheetBehavior<V>() {

    override fun onInterceptTouchEvent(
        parent: CoordinatorLayout,
        child: V,
        event: MotionEvent
    ): Boolean = false

    override fun onTouchEvent(parent: CoordinatorLayout, child: V, event: MotionEvent): Boolean = false
}