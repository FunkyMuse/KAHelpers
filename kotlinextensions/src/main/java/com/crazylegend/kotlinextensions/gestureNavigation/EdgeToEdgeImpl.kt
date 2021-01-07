package com.crazylegend.kotlinextensions.gestureNavigation

import android.app.Activity
import android.os.Build
import android.view.ViewGroup
import android.view.Window
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import com.google.android.material.appbar.AppBarLayout

/**
 * Created by crazy on 1/7/21 to long live and prosper !
 */
internal interface EdgeToEdgeImpl {
    /**
     * Configures a root view of an Activity in edge-to-edge display.
     * @param root A root view of an Activity.
     */
    fun setUpRoot(root: ViewGroup)

    @RequiresApi(Build.VERSION_CODES.R)
    fun setUpRoot(activity: Activity, root: ViewGroup, @ColorRes navBarColor: Int = android.R.color.transparent)

    /**
     * Configures an app bar and a toolbar for edge-to-edge display.
     * @param appBar An [AppBarLayout].
     * @param toolbar A [Toolbar] in the [appBar].
     */
    fun setUpAppBar(appBar: AppBarLayout, toolbar: Toolbar)

    /**
     * Configures a scrolling content for edge-to-edge display.
     * @param scrollingContent A scrolling ViewGroup. This is typically a RecyclerView or a
     * ScrollView. It should be as wide as the screen, and should touch the bottom edge of
     * the screen.
     */
    fun setUpScrollingContent(scrollingContent: ViewGroup, includePaddingBottom: Boolean = false)

    /**
     * Tell the window that we want to handle/fit any system windows
     */
    fun setDecorFitsSystemWindows(window: Window, setDecor: Boolean = true)
    fun setDecorFitsSystemWindows(activity: Activity, setDecor: Boolean = true)
}