package com.butterflies.stepaw.utils

import android.content.Context
import java.util.regex.Pattern

class StepawUtils {
    private val emailAddressPattern: Pattern = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )

    fun validateEmail(email: String): Boolean {
        return emailAddressPattern.matcher(email).matches();
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




    fun storePreferences(context:Context,key: String, token: String) {
        val sharedPreferences = context.getSharedPreferences("com.butterflies.stepaw", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString(key, token)
            apply()
        }
    }


}