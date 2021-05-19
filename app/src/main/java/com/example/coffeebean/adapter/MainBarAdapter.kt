package com.example.coffeebean.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.coffeebean.AllCallFragment
import com.example.coffeebean.MissCallFragment


class MainBarAdapter(fragmentActivity: Fragment) :
    FragmentStateAdapter(fragmentActivity) {

    private val pages = ArrayList<Fragment>()

    init {
        val menuDetailFragment1 = AllCallFragment()
        val menuDetailFragment2 = MissCallFragment()

        with(pages) {
            add(menuDetailFragment1)
            add(menuDetailFragment2)

        }
    }

    override fun getItemCount() = pages.size


    override fun createFragment(position: Int) = pages[position]
}