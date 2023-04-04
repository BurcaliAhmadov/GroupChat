package com.burcaliahmadov.groupchat.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPagerAdapter(
     context: Context,
     fm:FragmentManager?,
     val list :ArrayList<Fragment>

) :FragmentPagerAdapter(fm!!){
    override fun getCount(): Int {
        return 3
    }

    override fun getItem(position: Int): Fragment {
        return list[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return TAP_TITLES[position]
    }
    companion object{
        val TAP_TITLES= arrayOf("Chat","Status","Call")
    }
}