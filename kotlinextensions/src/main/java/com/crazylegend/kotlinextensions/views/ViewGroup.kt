package com.crazylegend.kotlinextensions.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


/**
 * Created by hristijan on 2/22/19 to long live and prosper !
 */


/**
 * USAGE

override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
val view = parent.inflate(R.layout.view_item)
return ViewHolder(view)
}

 */

fun ViewGroup.inflate(layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}

fun ViewGroup.getFirstChildOrNull(): View? {
    if (childCount == 0) return null
    return getChildAt(0)
}

fun ViewGroup.getLastChildOrNull(): View? {
    if (childCount == 0) return null
    return getChildAt(childCount - 1)
}


/**
 * Get views by tag for ViewGroup.
 */
fun ViewGroup.getViewsByTag(tag: String): ArrayList<View> {
    val views = ArrayList<View>()
    val childCount = childCount
    for (i in 0 until childCount) {
        val child = getChildAt(i)
        if (child is ViewGroup) {
            views.addAll(child.getViewsByTag(tag))
        }

        val tagObj = child.tag
        if (tagObj != null && tagObj == tag) {
            views.add(child)
        }

    }
    return views
}


/**
 * Remove views by tag ViewGroup.
 */
fun ViewGroup.removeViewsByTag(tag: String) {
    for (i in 0 until childCount) {
        val child = getChildAt(i)
        if (child is ViewGroup) {
            child.removeViewsByTag(tag)
        }

        if (child.tag == tag) {
            removeView(child)
        }
    }
}


/**
 * get All the Children's as Iterator
 */
fun ViewGroup.children() = object : Iterator<View> {
    var index = 0
    override fun hasNext(): Boolean {
        return index < childCount
    }

    override fun next(): View {
        return getChildAt(index++)
    }

}

/**
 * Hides all child views
 */
fun ViewGroup.hideAll() {
    eachChild {
        it.gone()
    }
}

/**
 * SHows all child views
 */
fun ViewGroup.showAll() {
    eachChild {
        it.visible()
    }
}

/**
 * Applys given func to all child views
 */
inline fun ViewGroup.eachChild(func: (view: View) -> Unit) {
    for (i in 0 until childCount) {
        func(getChildAt(i))
    }
}


/**
 * Gets Child View at index
 */
inline operator fun ViewGroup.get(i: Int): View? = getChildAt(i)