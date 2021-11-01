package com.butterflies.stepaw.authentication

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.butterflies.stepaw.R
import com.butterflies.stepaw.databinding.FragmentSigninBinding


class signin : Fragment() {
lateinit var sign_in_with_activity:Signin
    interface Signin{
        fun signin(email:String,password:String)
        fun googlesignin()
    }

    private lateinit var binding: FragmentSigninBinding
    override fun onAttach(context: Context) {
        super.onAttach(context)
        sign_in_with_activity=context as Signin
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSigninBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        Navigate to signup fragment
        binding.signup.setOnClickListener {
            view.findNavController().navigate(R.id.signup_fragment)

        }
// Navigate to forgot password fragment
        binding.forgotPassword.setOnClickListener {
            view.findNavController().navigate(R.id.password_reset_fragment)
        }
//        Call Activity's Google sign in method
        binding.googleSignin.setOnClickListener {
            sign_in_with_activity.googlesignin()
        }

//        Call Activity's signinwith email and password method

        binding.signin.setOnClickListener {
            val email=binding.email.text.toString()
            val password=binding.password.text.toString()
//            Validate

//
            sign_in_with_activity.signin(email, password)
        }

    }


}