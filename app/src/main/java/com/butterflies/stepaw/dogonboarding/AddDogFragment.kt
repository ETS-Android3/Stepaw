package com.butterflies.stepaw.dogonboarding

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import com.butterflies.stepaw.databinding.FragmentAddDogFragmentBinding


class AddDogFragment : Fragment() {
    private lateinit var binding: FragmentAddDogFragmentBinding
    private lateinit var onboard: OnBoardingService

    interface OnBoardingService {
        fun registerDog(name: String, age: Float, weight: Float, gender: String)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        onboard = context as OnBoardingService
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAddDogFragmentBinding.inflate(layoutInflater, container, false)
        binding.dogSave.setOnClickListener {
            val colorlist = ColorStateList.valueOf(Color.RED)
//            Checking for empty text fields
            when {
                TextUtils.isEmpty(binding.dogNameEdit.text) -> {
                    binding.dogNameEdit.backgroundTintList = colorlist
                }
                TextUtils.isEmpty(binding.dogAgeEdit.text) -> {
                    with(binding.dogAgeEdit) {
                        backgroundTintList = colorlist
                    }
                }
                TextUtils.isEmpty(binding.dogWeightEdit.text) -> {
                    with(binding.dogAgeEdit) {
                        backgroundTintList = colorlist
                    }
                }
                else -> {
                    val name = binding.dogNameEdit.text.toString()
                    val age = binding.dogAgeEdit.text.toString().toFloat()
                    val weight = binding.dogWeightEdit.text.toString().toFloat()
                    val genderGroupId = binding.dogGenderGroup.checkedRadioButtonId
                    val genderId = binding.root.findViewById<RadioButton>(genderGroupId)
                    val gender = genderId.text.toString()
                    onboard.registerDog(name, age, weight, gender)
                }
            }

        }
        return binding.root

    }
}