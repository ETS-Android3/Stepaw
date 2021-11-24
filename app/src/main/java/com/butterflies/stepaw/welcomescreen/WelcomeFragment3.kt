package com.butterflies.stepaw.welcomescreen

import android.os.Binder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.butterflies.stepaw.R
import com.butterflies.stepaw.databinding.FragmentWelcome3Binding


class WelcomeFragment3 : Fragment() {
private lateinit var binding: FragmentWelcome3Binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentWelcome3Binding.inflate(layoutInflater,container,false)

        Glide.with(this).load(R.raw.splash3).into(binding.welcomeImage3)

        return binding.root
    }


}