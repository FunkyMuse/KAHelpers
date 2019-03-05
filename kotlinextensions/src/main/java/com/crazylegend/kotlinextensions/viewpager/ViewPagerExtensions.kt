package com.crazylegend.kotlinextensions.viewpager

import androidx.viewpager.widget.ViewPager


/**
 * Created by hristijan on 3/5/19 to long live and prosper !
 */

fun ViewPager.onPageScrollStateChanged(onPageScrollStateChanged: (state: Int) -> Unit = {_ ->}) {
    addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {
            onPageScrollStateChanged(state)
        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        }

        override fun onPageSelected(position: Int) {
        }

    })
}

fun ViewPager.onPageScrolled(onPageScrolled: (position: Int, positionOffset: Float, positionOffsetPixels: Int) -> Unit = {_,_,_ ->}) {
    addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {
        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            onPageScrolled(position, positionOffset, positionOffsetPixels)
        }

        override fun onPageSelected(position: Int) {
        }

    })
}

fun ViewPager.onPageSelected(onPageSelected: (position: Int) -> Unit = {_ ->}) {
    addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {
        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        }

        override fun onPageSelected(position: Int) {
            onPageSelected(position)
        }

    })
}

fun ViewPager.listener(onPageScrollStateChanged: (state: Int) -> Unit = {_ ->},
                       onPageSelected: (position: Int) -> Unit = {_ ->},
                       onPageScrolled: (position: Int, positionOffset: Float, positionOffsetPixels: Int) -> Unit = {_,_,_ ->}) {
    addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {
            onPageScrollStateChanged(state)
        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            onPageScrolled(position, positionOffset, positionOffsetPixels)
        }

        override fun onPageSelected(position: Int) {
            onPageSelected(position)
        }

    })
}

inline fun ViewPager.back(animate: Boolean = true) {
    setCurrentItem(currentItem - 1, animate)
}

inline fun ViewPager.forward(animate: Boolean = true) {
    setCurrentItem(currentItem + 1, animate)
}

inline fun ViewPager.isOnLastPage(): Boolean {
    return currentItem == (adapter?.count ?: 0) - 1
}

inline fun ViewPager.isOnFirstPage(): Boolean {
    return currentItem == 0
}