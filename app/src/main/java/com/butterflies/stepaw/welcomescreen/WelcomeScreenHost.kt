package com.butterflies.stepaw.welcomescreen

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.butterflies.stepaw.authentication.AuthUIHost
import com.butterflies.stepaw.databinding.ActivityWelcomeScreenHostBinding
import com.butterflies.stepaw.utils.StepawUtils
import com.google.android.material.tabs.TabLayoutMediator

private const val NUM_PAGES = 3

class WelcomeScreenHost : FragmentActivity() {
    private lateinit var binding: ActivityWelcomeScreenHostBinding
    private lateinit var viewpager: ViewPager2
    val utils=StepawUtils()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        Updating user status

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
                if (position==2){
                    utils.storePreferences(this@WelcomeScreenHost,"com.butterflies.stepaw.firstTimeUser","false")
                    Intent(this@WelcomeScreenHost,AuthUIHost::class.java).also { startActivity(it) }
                }
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
            listOf(WelcomeFragment1(), WelcomeFragment2(),WelcomeFragment3())[position]
    }

}