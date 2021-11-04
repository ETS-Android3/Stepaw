package com.butterflies.stepaw.WelcomeScreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.butterflies.stepaw.R
import com.butterflies.stepaw.databinding.FragmentWelcome1Binding


class WelcomeFragment1 : Fragment() {
    private lateinit var binding:FragmentWelcome1Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentWelcome1Binding.inflate(layoutInflater,container,false)
        return binding.root
    }


}