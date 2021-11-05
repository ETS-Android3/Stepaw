package com.butterflies.stepaw.authentication

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.butterflies.stepaw.R


class PasswordReset_fragment : Fragment() {
    lateinit var password_reset_activity:PasswordResetService
    interface PasswordResetService{
        fun resetPassword(email:String,Password:String);
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        password_reset_activity=context as PasswordResetService
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_password_reset, container, false)
    }


}