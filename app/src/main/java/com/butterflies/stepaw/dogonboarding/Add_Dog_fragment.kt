package com.butterflies.stepaw.dogonboarding

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import com.butterflies.stepaw.databinding.FragmentAddDogFragmentBinding


class Add_Dog_fragment : Fragment() {
    private lateinit var binding: FragmentAddDogFragmentBinding
    private lateinit var onboard: OnBoardingService

    interface OnBoardingService {
        fun registerDog(name: String, age: Float, weight: Float, gender: String)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        onboard = context as OnBoardingService
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddDogFragmentBinding.inflate(layoutInflater, container, false)
        binding.dogSave.setOnClickListener {
            val colorlist=ColorStateList.valueOf(Color.RED)
           if(TextUtils.isEmpty(binding.dogNameEdit.text)){
               binding.dogNameEdit.backgroundTintList=colorlist
           }
            else if(TextUtils.isEmpty(binding.dogAgeEdit.text)){
                with(binding.dogAgeEdit){
                    backgroundTintList=colorlist
                }
           }
            else if(TextUtils.isEmpty(binding.dogWeightEdit.text)){
                with(binding.dogAgeEdit){
                    backgroundTintList=colorlist
                }
           }
            else{
               val name = binding.dogNameEdit.text.toString()
               val age = binding.dogAgeEdit.text.toString().toFloat()
               val weight = binding.dogWeightEdit.text.toString().toFloat()
               val gender_group_id = binding.dogGenderGroup.checkedRadioButtonId
               val gender_id = binding.root.findViewById<RadioButton>(gender_group_id)
               val gender = gender_id.text.toString()
                onboard.registerDog(name, age, weight, gender)
           }

        }
        return binding.root

    }
}