package com.butterflies.stepaw.authentication

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.butterflies.stepaw.databinding.FragmentSignupBinding
import com.butterflies.stepaw.utils.StepawUtils
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.w3c.dom.Text


class FragmentSignup : Fragment() {
    private lateinit var signupInWithActivity:SignUpService
    private lateinit var binding:FragmentSignupBinding
    interface SignUpService{
        fun Signup(email:String,password:String,signupusername:String)
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
        val root=FragmentSignupBinding.bind(binding.root)
        root.Signup.setOnClickListener {
            val email=root.emailSignup.text
            val password=root.passwordSignup.text
            val confirmPassword=root.confirmPasswordSignup.text
            val userName=root.usernamesignup.text
           if(!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(password)&&!TextUtils.isEmpty(confirmPassword)&&!TextUtils.isEmpty(userName)){
               val utils=StepawUtils().validateEmail(email.toString())
               if(utils&&(password.toString()==confirmPassword.toString())){
                   signupInWithActivity.Signup(email.toString(), password = password.toString(), signupusername = userName.toString())
               }
           }else{
               Toast.makeText(context,"Email/Password/Username empty",Toast.LENGTH_SHORT).show()
           }
        }
    }

}