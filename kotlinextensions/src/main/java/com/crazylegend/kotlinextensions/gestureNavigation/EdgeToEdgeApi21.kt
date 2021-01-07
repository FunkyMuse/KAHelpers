/*
 * Copyright 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.crazylegend.kotlinextensions.gestureNavigation

import android.app.Activity
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowInsets
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.crazylegend.kotlinextensions.R
import com.crazylegend.kotlinextensions.activity.setNavigationBarColor
import com.crazylegend.kotlinextensions.context.getColorCompat
import com.crazylegend.kotlinextensions.views.activity
import com.google.android.material.appbar.AppBarLayout

/**
 * A utility for edge-to-edge display. It provides several features needed to make the app
 * displayed edge-to-edge on Android Q with gestural navigation.
 */

/**
 * Usage
 *
 *   EdgeToEdge.setUpRoot(findViewById(R.id.root))
 *   EdgeToEdge.setUpAppBar(findViewById(R.id.app_bar), toolbar)
 *   EdgeToEdge.setUpScrollingContent(findViewById(R.id.content))

And add insets ex.

ViewCompat.setOnApplyWindowInsetsListener(view) { _, insets ->
toolbar.updateLayoutParams<CollapsingToolbarLayout.LayoutParams> {
topMargin = insets.systemWindowInsetTop
}
// The collapsed app bar gets taller by the toolbar's top margin. The CoordinatorLayout
// has to have a bottom margin of the same amount so that the scrolling content is
// completely visible.
scroll.updateLayoutParams<CoordinatorLayout.LayoutParams> {
bottomMargin = insets.systemWindowInsetTop
}
content.updatePadding(
left = insets.systemWindowInsetLeft,
right = insets.systemWindowInsetRight,
bottom = insets.systemWindowInsetBottom
)
insets
}


Ex2

ViewCompat.setOnApplyWindowInsetsListener(root) { _, insets ->
fab.updateLayoutParams<CoordinatorLayout.LayoutParams> {
leftMargin = fabMargin + insets.systemWindowInsetLeft
rightMargin = fabMargin + insets.systemWindowInsetRight
bottomMargin = fabMargin + insets.systemWindowInsetBottom
}
sheet.updateLayoutParams<CoordinatorLayout.LayoutParams> {
leftMargin = fabMargin + insets.systemWindowInsetLeft
rightMargin = fabMargin + insets.systemWindowInsetRight
bottomMargin = fabMargin + insets.systemWindowInsetBottom
}
insets
}

 */

@Suppress("DEPRECATION")
internal class EdgeToEdgeApi21 : EdgeToEdgeImpl {

    override fun setUpRoot(root: ViewGroup) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            root.windowInsetsController?.hide(WindowInsetsCompat.Type.systemGestures())
        } else {
            root.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun setUpRoot(activity: Activity, root: ViewGroup, @ColorRes navBarColor: Int) {
        activity.window.setDecorFitsSystemWindows(true)
        root.activity
        activity.setNavigationBarColor(activity.getColorCompat(navBarColor))
        setUpRoot(root)
    }

    override fun setUpAppBar(appBar: AppBarLayout, toolbar: Toolbar) {
        val toolbarPadding = toolbar.resources.getDimensionPixelSize(R.dimen.spacing_medium)
        appBar.setOnApplyWindowInsetsListener { _, windowInsets ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val insets = windowInsets.getInsets(WindowInsets.Type.systemBars())
                appBar.updatePadding(top = insets.top)
                toolbar.updatePadding(
                        left = toolbarPadding + insets.left,
                        right = insets.right
                )
            } else {
                appBar.updatePadding(top = windowInsets.systemWindowInsetTop)
                toolbar.updatePadding(
                        left = toolbarPadding + windowInsets.systemWindowInsetLeft,
                        right = windowInsets.systemWindowInsetRight
                )
            }

            windowInsets
        }
    }

    override fun setDecorFitsSystemWindows(activity: Activity, setDecor: Boolean) {
        WindowCompat.setDecorFitsSystemWindows(activity.window, setDecor)

    }

    override fun setDecorFitsSystemWindows(window: Window, setDecor: Boolean) {
        WindowCompat.setDecorFitsSystemWindows(window, setDecor)
    }

    override fun setUpScrollingContent(scrollingContent: ViewGroup, includePaddingBottom: Boolean) {
        val originalPaddingLeft = scrollingContent.paddingLeft
        val originalPaddingRight = scrollingContent.paddingRight
        val originalPaddingBottom = scrollingContent.paddingBottom
        val originalPaddingTop = scrollingContent.paddingTop


        scrollingContent.setOnApplyWindowInsetsListener { _, windowInsets ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val insets = windowInsets.getInsets(WindowInsets.Type.systemBars())
                val bottomPadding = if (includePaddingBottom) originalPaddingBottom + insets.bottom else originalPaddingBottom
                scrollingContent.updatePadding(
                        left = originalPaddingLeft + insets.left,
                        right = originalPaddingRight + insets.right,
                        bottom = bottomPadding,
                        top = originalPaddingTop + insets.top
                )
            } else {
                val paddingBottomCompat = if (includePaddingBottom) originalPaddingBottom + windowInsets.systemWindowInsetBottom else originalPaddingBottom
                scrollingContent.updatePadding(
                        left = originalPaddingLeft + windowInsets.systemWindowInsetLeft,
                        right = originalPaddingRight + windowInsets.systemWindowInsetRight,
                        bottom = paddingBottomCompat,
                        top = originalPaddingTop + windowInsets.systemWindowInsetTop
                )
            }
            windowInsets
        }
    }
}
