package com.example.noter.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.noter.presentation.view.FoldersFragment
import com.example.noter.presentation.view.HomeFragment

const val FRAGMENTS_COUNT = 2

class NotesTabsAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {


    override fun getItemCount(): Int {
        return FRAGMENTS_COUNT
    }

    override fun createFragment(position: Int): Fragment {

        return when (position) {
            0 -> getFragments()[0]
            1 -> getFragments()[1]
            else -> HomeFragment()
        }
     }

    fun getFragments(): Array<Fragment> {
        val fragmentOne = HomeFragment()
        val fragmentTwo = FoldersFragment()
        return arrayOf(fragmentOne, fragmentTwo)
    }
}