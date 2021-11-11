package com.butterflies.stepaw.authentication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.butterflies.stepaw.dogonboarding.OnBoardingHost
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
import com.google.firebase.auth.GetTokenResult

import androidx.annotation.NonNull
import com.butterflies.stepaw.network.ApiService
import com.butterflies.stepaw.network.models.UserModel
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


class AuthUIHost : AppCompatActivity(), FragmentSignin.SigninService, FragmentSignup.SignUpService,
    FragmentPasswordReset.PasswordResetService {
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    lateinit var id: String;
    lateinit var userName: String;
    lateinit var firstName: String;
    lateinit var lastName: String;
    lateinit var email: String;

    //    Build retrofit instance
    val retrofit = Retrofit.Builder()
        .baseUrl(ApiService.BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
    val service = retrofit.create(ApiService::class.java)

    //    For signing in user with google call signin()
//    For signing in with email password call signinwithpassword(email,password)
//    For signout signout()
//    For signup call signUp(email,password)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_authuihost)
        val navHostFragment: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host) as NavHostFragment

        navHostFragment.findNavController().setGraph(R.navigation.authentication_nav)


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
            .requestId()
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
                    val user = auth.currentUser
                    user?.uid?.let { storePreferences("com.butterflies.stepaw.uuid", it) }
                    user?.getIdToken(true)
                        ?.addOnCompleteListener { token ->
                            if (token.isSuccessful) {
                                val idToken = token.getResult()?.token
                                if (idToken != null) {
                                    storePreferences("com.butterflies.stepaw.idToken", idToken)
                                }

                            } else {
                                Log.d("failed", "Failed to generate idtoken")
                            }
                        }


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

//        Store id token to shared preferences
        storePreferences("com.butterflies.stepaw.idToken", idToken)

        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("login", "signInWithCredential:success")

                    val user = auth.currentUser
                    if (user?.uid !== null) {
                        storePreferences("com.butterflies.stepaw.uid", user?.uid!!)
                        id=user?.uid!!
                    }
                    if (user?.displayName !== null) {storePreferences(
                        "com.butterflies.stepaw.displayName",
                        user?.displayName!!
                    )
                    userName=user?.displayName!!
                    }
                    else storePreferences("com.butterflies.stepaw.displayName", "invalid")

                    if (user?.displayName !== null) {storePreferences(
                        "com.butterflies.stepaw.firstName",
                        user?.displayName!!
                    )
                    firstName=user?.displayName!!
                    }
                    else storePreferences("com.butterflies.stepaw.firstName", "invalid")
                    if (user?.displayName !== null){ storePreferences(
                        "com.butterflies.stepaw.lastName",
                        user?.displayName!!
                    )
                    lastName=user?.displayName!!
                    }
                    else storePreferences("com.butterflies.stepaw.lastName", "invalid")

                    if (user?.email !== null) {storePreferences(
                        "com.butterflies.stepaw.email",
                        user?.email!!
                    )
                    email=user?.email!!
                    }
                    else storePreferences("com.butterflies.stepaw.email", "invalid")
if(this::id.isInitialized&&this::userName.isInitialized&&this::email.isInitialized){
    createNewUserCall(id,userName,userName,userName,email,"01")
}

                    Intent(this@AuthUIHost, OnBoardingHost::class.java).also { startActivity(it) }
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

    private fun storePreferences(key: String, token: String) {
        val sharedPreferences = getSharedPreferences("com.butterflies.stepaw", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString(key, token)
            apply()
        }
    }

    //    Create a new user in the backend
    fun createNewUserCall(
        UserID: String,
        UserName: String,
        FirstName: String,
        LastName: String,
        EmailID: String,
        BluetoothID: String
    ) {
        val usermodel = UserModel(UserID, UserName, FirstName, LastName, EmailID, BluetoothID)
        val newUserRequest = service.createUser(usermodel)
        newUserRequest.enqueue(object : Callback<UserModel> {
            override fun onResponse(call: Call<UserModel>, response: Response<UserModel>) {
               Log.d("retrofit","Storedsuccessfully")
            }

            override fun onFailure(call: Call<UserModel>, t: Throwable) {

            }

        })
    }
}