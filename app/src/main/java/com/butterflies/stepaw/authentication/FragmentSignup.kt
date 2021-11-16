package com.butterflies.stepaw.authentication

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.butterflies.stepaw.databinding.FragmentSignupBinding


class FragmentSignup : Fragment() {
    private lateinit var signupInWithActivity:SignUpService
    private lateinit var binding:FragmentSignupBinding
    interface SignUpService{
        fun Signup(email:String,password:String)
        fun googlesignin()
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        signupInWithActivity=context as SignUpService
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding= FragmentSignupBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val email=binding.email.text.toString()
        val password=binding.password.text.toString()
        binding.Signup.setOnClickListener {
            signupInWithActivity.Signup(email,password)
        }
    }

}