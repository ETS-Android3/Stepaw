package com.butterflies.stepaw.welcomescreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.butterflies.stepaw.databinding.ActivityWelcomeScreenHostBinding
import com.google.android.material.tabs.TabLayoutMediator

private const val NUM_PAGES = 3

class WelcomeScreenHost : FragmentActivity() {
    private lateinit var binding: ActivityWelcomeScreenHostBinding
    private lateinit var viewpager: ViewPager2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeScreenHostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewpager = binding.viewpager
        val pagerAdapter = ScreenSlidePagerAdapter(this)
        viewpager.adapter = pagerAdapter

        val tabLayout=binding.tabLayout
        TabLayoutMediator(tabLayout,viewpager){ _, _ ->


        }.attach()

        viewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
            }
        })

    }

    override fun onBackPressed() {
        if (viewpager.currentItem == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed()
        } else {
            // Otherwise, select the previous step.
            viewpager.currentItem = viewpager.currentItem - 1
        }
    }


    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = NUM_PAGES

        override fun createFragment(position: Int): Fragment =
            listOf<Fragment>(WelcomeFragment1(), WelcomeFragment2(),WelcomeFragment3())[position]
    }

}