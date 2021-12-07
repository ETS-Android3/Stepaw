package com.butterflies.stepaw.dogonboarding

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import com.butterflies.stepaw.databinding.FragmentEditDogBinding
import com.butterflies.stepaw.network.ApiService
import com.butterflies.stepaw.network.models.PetGetModel
import com.butterflies.stepaw.utils.StepawUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


class EditDogFragment : Fragment() {
    private lateinit var binding: FragmentEditDogBinding
    val retrofit: Retrofit = Retrofit.Builder().baseUrl(ApiService.BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
    val service: ApiService = retrofit.create(ApiService::class.java)
    private var petId: String? = null
    private lateinit var editdog: EditDogFragment.OnBoardingService


    interface OnBoardingService {
        fun editDog(name: String, age: Float, weight: Float, gender: String)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        editdog = context as OnBoardingService
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEditDogBinding.inflate(layoutInflater, container, false)

        val args = requireArguments().containsKey("petId")
        if (args) {
            petId = requireArguments().getString("petId")
        }
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val root = FragmentEditDogBinding.bind(binding.root)
        Log.d("petId", petId!!)
        if (petId !== null) {
            val idToken = StepawUtils().getIdToken(requireContext())
            val petData = service.getPetById(
                token = " Bearer $idToken", id = StepawUtils().extractUser(
                    requireContext()
                )!!.UserID
            )

            petData.enqueue(object : Callback<List<PetGetModel>> {
                override fun onResponse(
                    call: Call<List<PetGetModel>>,
                    response: Response<List<PetGetModel>>
                ) {
                    val r = response.body()
                    val pet = r!!.filter { it.PetID == petId }

//                Update data inside edit dog fragment now


                    with(root) {
                        dogNameEdit.setText(pet[0].PetName)
                        dogAgeEdit.setText(pet[0].Age)
                        dogWeightEdit.setText(pet[0].Weight)
                        val gender = pet[0].Gender
                        if (gender.equals("Male", true)) {
                            dogGenderGroup.check(com.butterflies.stepaw.R.id.dog_male)
                        } else if (gender.equals("Female", true)) {
                            dogGenderGroup.check(com.butterflies.stepaw.R.id.dog_female)
                        } else {
                            dogGenderGroup.check(com.butterflies.stepaw.R.id.dog_others)
                        }
                    }


//


                }

                override fun onFailure(call: Call<List<PetGetModel>>, t: Throwable) {
                    Log.d("editdog", t.message.toString())
                }

            })

        }

        root.dogSave.setOnClickListener {
            val name = binding.dogNameEdit.text.toString()
            val age = binding.dogAgeEdit.text.toString().toFloat()
            val weight = binding.dogWeightEdit.text.toString().toFloat()
            val genderGroupId = binding.dogGenderGroup.checkedRadioButtonId
            val genderId = binding.root.findViewById<RadioButton>(genderGroupId)
            val gender = genderId.text.toString()
            editdog.editDog(name, age, weight, gender)
        }


        root.dogDelete.setOnClickListener {
            val cd = CustomDialogClass(requireActivity())
            cd.show()
        }

    }

    class CustomDialogClass     // TODO Auto-generated constructor stub
        (var c: Activity) : Dialog(c), View.OnClickListener {
        var d: Dialog? = null
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(com.butterflies.stepaw.R.layout.custom_delete_dialog)
            val yes = findViewById<Button>(com.butterflies.stepaw.R.id.custom_dog_save)
            val no = findViewById<Button>(com.butterflies.stepaw.R.id.custom_dog_delete)
            yes.setOnClickListener(this)
            no.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            when (v.id) {
                com.butterflies.stepaw.R.id.custom_dog_save -> dismiss()
                com.butterflies.stepaw.R.id.custom_dog_delete -> dismiss()
                else -> {}
            }
            dismiss()
        }
    }

}