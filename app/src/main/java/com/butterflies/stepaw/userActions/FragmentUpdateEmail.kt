package com.butterflies.stepaw.userActions

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.butterflies.stepaw.R
import com.butterflies.stepaw.authentication.AuthUIHost
import com.butterflies.stepaw.databinding.FragmentUpdateEmailBinding
import com.butterflies.stepaw.network.models.UserModel
import com.butterflies.stepaw.utils.StepawUtils
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import java.lang.Exception


class FragmentUpdateEmail : Fragment() {
    private lateinit var binding: FragmentUpdateEmailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUpdateEmailBinding.inflate(layoutInflater, container, false)

        binding.updatePassword.setOnClickListener {

            view?.findNavController()
                ?.navigate(R.id.action_fragmentUpdateEmail_to_fragmentUpdatePassword)
        }

        val userData = context?.getSharedPreferences("com.butterflies.stepaw", Context.MODE_PRIVATE)
        val userJson = userData?.getString("com.butterflies.stepaw.user", "false")
        val user = Firebase.auth.currentUser
        if (user != null) {
            Log.d("provider", user.providerData.size.toString())
        }
        if (userJson !== "false") {
            val gson = Gson()
            val json = gson.fromJson(userJson, UserModel::class.java)
            binding.userName.text = json.UserName
            binding.userEmail.text = json.EmailID

//            binding.editEmail.setOnClickListener {
//                binding.editEmailText.setText(json.EmailID)
//                binding.editEmailText.visibility = View.VISIBLE
//                binding.saveEmail.visibility = View.VISIBLE
//
//                binding.saveEmail.setOnClickListener {
//                    val utils = StepawUtils()
//                    if (!TextUtils.isEmpty(binding.editEmailText.text.toString())) {
//
//                        user!!.updateEmail("user@example.com")
//                            .addOnCompleteListener { task ->
//                                try {
//                                    if (task.isSuccessful) {
//                                        binding.saveEmail.visibility = View.GONE
//                                        binding.editEmailText.visibility = View.GONE
//                                        Intent(
//                                            context,
//                                            AuthUIHost::class.java
//                                        ).also { startActivity(it) }
//                                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                                    }
//                                } catch (e: Exception) {
//                                    Log.d("stepaw.email", e.message.toString())
//                                    Toast.makeText(context,e.message.toString(),Toast.LENGTH_SHORT).show()
//                                }
//                            }
//                    }
//
//                }
//            }
        }

        return binding.root
    }

}