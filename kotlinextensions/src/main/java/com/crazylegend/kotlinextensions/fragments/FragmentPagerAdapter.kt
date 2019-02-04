package com.crazylegend.kotlinextensions.fragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import java.util.ArrayList


/**
 * Created by Hristijan on 2/4/19 to long live and prosper !
 */

class FragmentPagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {

    private val fragmentList = ArrayList<Fragment>()
    private val fragmentTitleList = ArrayList<String>()

    override fun getCount(): Int {
       return fragmentList.size
    }

    fun addFragment(fragment: Fragment, fragmentTitle: String) {
        fragmentList.add(fragment)
        fragmentTitleList.add(fragmentTitle)
    }

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getPageTitle(position: Int): CharSequence {
        return fragmentTitleList[position]
    }
}
