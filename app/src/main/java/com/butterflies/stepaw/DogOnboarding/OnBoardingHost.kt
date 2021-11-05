package com.butterflies.stepaw.DogOnboarding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.butterflies.stepaw.R
import com.butterflies.stepaw.databinding.ActivityOnBoardingHostBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior

class OnBoardingHost : AppCompatActivity(),Add_Dog_fragment.OnBoardingService {
    private lateinit var binding: ActivityOnBoardingHostBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingHostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(findViewById(R.id.my_toolbar))
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        Bottom sheet settings


        val standardBottomSheetBehavior =
            BottomSheetBehavior.from(findViewById(R.id.bottom_sheet_onboard))
        standardBottomSheetBehavior.state=BottomSheetBehavior.STATE_HALF_EXPANDED
        standardBottomSheetBehavior.isDraggable=false


//      setting navhost nav graph programatically
        val navHostFragment: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host) as NavHostFragment
        navHostFragment.findNavController().setGraph(R.navigation.dogonboarding_nav)
    }

    override fun onSupportNavigateUp(): Boolean {
//        return super.onSupportNavigateUp()
        Toast.makeText(this,"Back pressed",Toast.LENGTH_SHORT).show()
        return true
    }
    override fun registerDog(name: String, age: Float, weight: Float, gender: String) {
        Toast.makeText(this,name,Toast.LENGTH_SHORT).show()
    }
}