package com.butterflies.stepaw.userActions

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.butterflies.stepaw.R
import com.butterflies.stepaw.authentication.AuthUIHost
import com.butterflies.stepaw.databinding.FragmentUpdatePasswordBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class FragmentUpdatePassword : Fragment() {
private lateinit var binding:FragmentUpdatePasswordBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentUpdatePasswordBinding.inflate(layoutInflater,container,false)

        val newPassword by lazy { binding.confirmNewPassword.text.toString() }
        val confirmPassword by lazy { binding.confirmNewPassword.text.toString() }
        binding.savePassword.setOnClickListener {
            if((!TextUtils.isEmpty(binding.newPassword.text)&&(!TextUtils.isEmpty(binding.confirmNewPassword.text)))&&newPassword==confirmPassword){

                val user = Firebase.auth.currentUser

                user!!.updatePassword(newPassword)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("stepaw.password", "User password updated.")
                            Intent(context,AuthUIHost::class.java).also { startActivity(it) }.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        }else{
                            Log.d("stepaw.password", task.exception?.message.toString())
                        }
                    }


            }else{
                Toast.makeText(context,"Failed to change the password",Toast.LENGTH_SHORT).show()
            }

        }


        return binding.root
    }

}