package com.butterflies.stepaw.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.butterflies.stepaw.network.models.UserModel
import com.google.gson.Gson
import java.util.regex.Pattern

class StepawUtils {
    private val EMAIL_ADDRESS_PATTERN: Pattern = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )

    fun validateEmail(email: String): Boolean {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }

    fun validatePassword(): Boolean = true
//    Getting user data in any activity
//    try {
//        val p = getSharedPreferences("com.butterflies.stepaw", Context.MODE_PRIVATE)
//        val u = p.getString("com.butterflies.stepaw.user", "invalid")
//        val j = Gson()
//        val d = j.fromJson(u, UserModel::class.java)
//        Toast.makeText(this, d.EmailID, Toast.LENGTH_SHORT).show()
//    } catch (e: Exception) {
//        Log.d("userdata", e.localizedMessage)
//    }

}