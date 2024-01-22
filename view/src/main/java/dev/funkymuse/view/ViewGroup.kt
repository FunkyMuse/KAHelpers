package dev.funkymuse.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView





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

val ViewGroup.inflater: LayoutInflater get() = LayoutInflater.from(context)


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
operator fun ViewGroup.get(i: Int): View? = getChildAt(i)


fun ViewGroup.getChildren(): List<View> {
    return (0 until childCount).map {
        getChildAt(it)
    }
}

fun ViewGroup.getBottomLevelChildren(stopAtRecyclerView: Boolean = false): List<View> {
    val result = mutableListOf<View>()

    val children = getChildren()
    children.forEach {
        if (it is ViewGroup && it.childCount > 0 && (!stopAtRecyclerView || (stopAtRecyclerView && it !is RecyclerView))) {
            result.addAll(it.getBottomLevelChildren(stopAtRecyclerView))
        } else {
            result.add(it)
        }
    }

    return result
}

fun ViewGroup.getAllChildren(): List<View> {
    val result = mutableListOf<View>()

    val children = getChildren()
    children.forEach {
        if (it is ViewGroup && it.childCount > 0) {
            result.add(it)
            result.addAll(it.getAllChildren())
        } else {
            result.add(it)
        }
    }

    return result
}


