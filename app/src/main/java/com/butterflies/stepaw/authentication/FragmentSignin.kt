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


class FragmentSignin : Fragment() {
    private lateinit var signinwithactivity: SigninService

    interface SigninService {
        fun signin(email: String, password: String)
        fun googlesignin()
    }

    private lateinit var binding: FragmentSigninBinding
    override fun onAttach(context: Context) {
        super.onAttach(context)
        signinwithactivity = context as SigninService
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSigninBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val root = FragmentSigninBinding.bind(binding.root)
//        Navigate to signup fragment
        binding.signup.setOnClickListener {
            view.findNavController().navigate(R.id.action_signin_to_signup)

        }
// Navigate to forgot password fragment
        binding.forgotPassword.setOnClickListener {
            view.findNavController().navigate(R.id.action_signin_to_password_reset_fragment)
        }
//        Call Activity's Google sign in method
        binding.googleSignin.setOnClickListener {
            signinwithactivity.googlesignin()
        }

//        Call Activity's signinwith email and password method

        binding.signin.setOnClickListener {
            val email = root.email.text.toString()
            val password = root.password.text.toString()
//            Validate
            signinwithactivity.signin(email, password)
        }

    }


}