package com.crazylegend.kotlinextensions.viewpager

import androidx.viewpager2.widget.ViewPager2


/**
 * Created by hristijan on 7/26/19 to long live and prosper !
 */

fun ViewPager2.listener(onPageScrollStateChanged: (state: Int) -> Unit = { _ -> },
                        onPageSelected: (position: Int) -> Unit = { _ -> },
                        onPageScrolled: (position: Int, positionOffset: Float, positionOffsetPixels: Int) -> Unit = { _, _, _ -> }) {
    registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
        override fun onPageScrollStateChanged(state: Int) {
            super.onPageScrollStateChanged(state)
            onPageScrollStateChanged(state)
        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            onPageScrolled(position, positionOffset, positionOffsetPixels)
        }

        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            onPageSelected(position)
        }
    })
}

fun ViewPager2.onPageScrollStateChanged(onPageScrollStateChanged: (state: Int) -> Unit = { _ -> }) {
    registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
        override fun onPageScrollStateChanged(state: Int) {
            super.onPageScrollStateChanged(state)
            onPageScrollStateChanged(state)
        }
    })
}

fun ViewPager2.onPageSelected(
        onPageSelected: (position: Int) -> Unit = { _ -> }) {
    registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            onPageSelected(position)
        }
    })
}

fun ViewPager2.onPageScrolled(
        onPageSelected: (position: Int) -> Unit = { _ -> }) {
    registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            onPageSelected(position)
        }
    })
}


inline fun ViewPager2.back(animate: Boolean = true) {
    setCurrentItem(currentItem - 1, animate)
}

inline fun ViewPager2.forward(animate: Boolean = true) {
    setCurrentItem(currentItem + 1, animate)
}

inline fun ViewPager2.isOnLastPage(): Boolean {
    return currentItem == (adapter?.itemCount ?: 0) - 1
}

inline fun ViewPager2.isOnFirstPage(): Boolean {
    return currentItem == 0
}

/**
 * Checks if ViewPager can swipe back.
 */
fun ViewPager2.canGoBack() = currentItem > 0

/**
 * Checks if ViewPager can swipe next
 */
fun ViewPager2.canGoNext() = adapter != null && currentItem < adapter!!.itemCount - 1

/**
 * Swipes ViewPager back
 */
fun ViewPager2.goPrevious() {
    if (canGoBack()) currentItem -= 1
}

/**
 * Swipes ViewPager next
 */
fun ViewPager2.goNext() {
    if (canGoNext()) currentItem += 1
}

val ViewPager2.length: Int?
    get() = adapter?.itemCount

val ViewPager2.lastIndex: Int?
    get() = adapter?.itemCount?.minus(1)

val ViewPager2.isLastView: Boolean
    get() = currentItem == length?.minus(1)

fun ViewPager2.next() {
    if (!isLastView) {
        currentItem += 1
    }
}

fun ViewPager2.next(lastCallback: () -> Unit) {
    if (!isLastView) {
        currentItem += 1
    } else {
        lastCallback()
    }
}

fun ViewPager2.nextCircular() {
    if (!isLastView) {
        currentItem += 1
    } else {
        currentItem = 0
    }
}