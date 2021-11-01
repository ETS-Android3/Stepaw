package com.butterflies.stepaw.authentication

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.butterflies.stepaw.R
import com.butterflies.stepaw.databinding.FragmentSignupBinding


class signup : Fragment() {
    lateinit var signup_in_with_activity:SignUp
    private lateinit var binding:FragmentSignupBinding
    interface SignUp{
        fun Signup(email:String,password:String)
        fun googlesignin()
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        signup_in_with_activity=context as SignUp
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding= FragmentSignupBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val email=binding.email.text.toString()
        val password=binding.password.text.toString()
        binding.Signup.setOnClickListener {
            signup_in_with_activity.Signup(email,password)
        }
    }

}