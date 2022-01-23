package com.example.englishdictionary.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.englishdictionary.models.WordData
import com.example.englishdictionary.ui.search.MeaningFragment
import com.example.englishdictionary.ui.search.OriginFragment

class ViewPagerAdapter(frm: FragmentManager, var list: List<String>) :
    FragmentPagerAdapter(frm) {
    override fun getCount(): Int = 2

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> MeaningFragment()

            else -> OriginFragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence {
        return list[position]
    }
}