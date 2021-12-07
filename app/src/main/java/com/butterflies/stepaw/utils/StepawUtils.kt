package com.butterflies.stepaw.utils

import android.content.Context
import com.butterflies.stepaw.network.models.UserModel
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*
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


    fun extractUser(context: Context): UserModel? {
        val p = context.getSharedPreferences("com.butterflies.stepaw", Context.MODE_PRIVATE)
        val u = p.getString("com.butterflies.stepaw.user", "invalid")
        val j = Gson()
        return j.fromJson(u, UserModel::class.java)
    }


    fun storePreferences(context: Context, key: String, token: String) {
        val sharedPreferences =
            context.getSharedPreferences("com.butterflies.stepaw", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString(key, token)
            apply()
        }
    }

    fun getUniqueID(): Int {
        val now = Date()
        return SimpleDateFormat("ddHHmmss", Locale.US).format(now).toInt()
    }

    fun getDate(): String {
        val d = Calendar.getInstance()

        val day = d.get(Calendar.DAY_OF_MONTH)
        val month = d.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()).take(3)
        val year = d.get(Calendar.YEAR)

        return "${day}-${month}-${year}"
    }

    fun getIdToken(context: Context): String {
        val sharedData =
            context.getSharedPreferences("com.butterflies.stepaw", Context.MODE_PRIVATE)
        return sharedData.getString("com.butterflies.stepaw.idToken", "invalid")!!
    }

}