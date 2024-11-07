package com.exceseats.util

import android.view.View
import android.widget.ImageView
import androidx.core.view.AccessibilityDelegateCompat
import androidx.core.view.ViewCompat
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat

object AccessibilityUtils {
    fun setClickActionDescription(view: View, description: String) {
        ViewCompat.setAccessibilityDelegate(view, object : AccessibilityDelegateCompat() {
            override fun onInitializeAccessibilityNodeInfo(
                host: View,
                info: AccessibilityNodeInfoCompat
            ) {
                super.onInitializeAccessibilityNodeInfo(host, info)
                info.addAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat(
                    AccessibilityNodeInfoCompat.ACTION_CLICK, description))
            }
        })
    }

    fun setImageDescription(imageView: ImageView, description: String) {
        imageView.contentDescription = description
        imageView.importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_YES
    }
}
