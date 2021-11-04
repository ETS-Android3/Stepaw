package com.butterflies.stepaw.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.butterflies.stepaw.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AuthUIHost : AppCompatActivity(), Signin.SigninService, Signup.SignUpService,PasswordReset.PasswordResetService {
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    //    For signing in user with google call signin()
//    For signing in with email password call signinwithpassword(email,password)
//    For signout signout()
//    For signup call signUp(email,password)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authuihost)
        val standardBottomSheetBehavior =
            BottomSheetBehavior.from(findViewById(R.id.bottom_sheet_signin))
//       Disabling Bottom sheet draggable status
        standardBottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        standardBottomSheetBehavior.isDraggable = false


//        Bottom sheet
        val bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                // Do something for new state.

            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // Do something for slide offset.
            }
        }
standardBottomSheetBehavior.addBottomSheetCallback(bottomSheetCallback)

//

        //Configure signin
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("598844256855-s9c5mjpt9kmpnmu04a11egq188t491qo.apps.googleusercontent.com")
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        auth = Firebase.auth


    }

    private fun signinWithPassword(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("signin", "signInWithEmail:success")
//                    val user = auth.currentUser

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("signin", "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 0) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d("login", "firebaseAuthWithGoogle:" + account.toString())
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("login", "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("login", "signInWithCredential:success")
                    val user = auth.currentUser
                    Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("login", "signInWithCredential:failure", task.exception)
                    Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, 0)
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        Log.d("userdetails", currentUser.toString())
        currentUser?.displayName?.let { Log.d("onStart", it) }
    }

    private fun signUp(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser

                } else {
                    // If sign in fails, display a message to the user.

                }
            }
    }

    private fun passwordReset(password: String) {
        val user = auth.currentUser
        user!!.updatePassword(password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("password", "User password updated.")
                }
            }
    }

    private fun logout() {
        Firebase.auth.signOut()
    }

    override fun signin(email: String, password: String) {

        if (email.length <= 1 || password.length <= 1) {
            Toast.makeText(this, "Password or email is incorrect", Toast.LENGTH_SHORT).show()
            return
        }
        signinWithPassword(email, password)
    }

    override fun Signup(email: String, password: String) {
        signUp(email, password)
    }

    override fun googlesignin() {
        signIn()
    }

    override fun resetPassword(email: String, Password: String) {
        passwordReset(Password)
    }
}