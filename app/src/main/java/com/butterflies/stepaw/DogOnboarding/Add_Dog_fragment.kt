package com.butterflies.stepaw.DogOnboarding

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import com.butterflies.stepaw.R
import com.butterflies.stepaw.databinding.FragmentAddDogFragmentBinding
import com.butterflies.stepaw.databinding.FragmentSigninBinding




class Add_Dog_fragment : Fragment() {
    private lateinit var binding:FragmentAddDogFragmentBinding
    private lateinit var onboard:OnBoardingService
interface OnBoardingService{
    fun registerDog(name:String,age:Float,weight:Float,gender:String)
}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
onboard=context as OnBoardingService
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentAddDogFragmentBinding.inflate(layoutInflater,container,false)
binding.dogSave.setOnClickListener {
    val name=binding.dogNameEdit.text.toString()
    val age=binding.dogAgeEdit.text.toString().toFloat()
    val weight=binding.dogWeightEdit.text.toString().toFloat()
    val gender_group_id=binding.dogGenderGroup.checkedRadioButtonId
    val gender_id= binding.root.findViewById<RadioButton>(gender_group_id)
    val gender=gender_id.text.toString()
   onboard.registerDog(name, age, weight, gender)
}
        return binding.root

    }
}