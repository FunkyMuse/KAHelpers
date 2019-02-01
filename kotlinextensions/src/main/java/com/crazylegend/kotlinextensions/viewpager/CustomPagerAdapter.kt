package com.crazylegend.kotlinextensions.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter


/**
 * Created by Hristijan on 2/1/19 to long live and prosper !
 */
class CustomPagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {

    private val fragmentList: ArrayList<Fragment> = ArrayList()
    private val fragmentListName: ArrayList<String> = ArrayList()

    fun addFragment(fragment: Fragment, title: String) {
        fragmentList.add(fragment)
        fragmentListName.add(title)
    }

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return fragmentListName[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

}