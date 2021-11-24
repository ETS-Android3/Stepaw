package com.butterflies.stepaw.welcomescreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.butterflies.stepaw.R
import com.butterflies.stepaw.databinding.FragmentWelcome2Binding

class WelcomeFragment2 : Fragment() {

private lateinit var binding:FragmentWelcome2Binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentWelcome2Binding.inflate(layoutInflater,container,false)
        Glide.with(this).load(R.raw.splash2).into(binding.welcomeImage2)

        return binding.root
    }


}