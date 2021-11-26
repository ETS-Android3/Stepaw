package com.butterflies.stepaw.authentication

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.butterflies.stepaw.DogList
import com.butterflies.stepaw.R
import com.butterflies.stepaw.dogonboarding.OnBoardingHost
import com.butterflies.stepaw.network.ApiService
import com.butterflies.stepaw.network.models.UserModel
import com.butterflies.stepaw.network.networkCall.NetworkCall
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import kotlin.system.exitProcess

class AuthUIHost : AppCompatActivity(), FragmentSignin.SigninService, FragmentSignup.SignUpService,
    FragmentPasswordReset.PasswordResetService {
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    lateinit var id: String
    private lateinit var userName: String
    private lateinit var firstName: String
    private lateinit var lastName: String
    private lateinit var email: String

    //    Build retrofit instance
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(ApiService.BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
    private val service: ApiService = retrofit.create(ApiService::class.java)

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

    override fun onBackPressed() {
//        super.onBackPressed()
        val homeIntent = Intent(Intent.ACTION_MAIN)
        homeIntent.addCategory(Intent.CATEGORY_HOME)
        homeIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(homeIntent)
        exitProcess(1)
    }

    @SuppressLint("LogNotTimber")
    private fun signinWithPassword(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("signin", "signInWithEmail:success")
                    val user = auth.currentUser
                    taskSuccessGetIdToken(user)

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

    private fun taskSuccessGetIdToken(user: FirebaseUser?) {
        user?.uid?.let { storePreferences("com.butterflies.stepaw.uuid", it) }
        user?.getIdToken(true)
            ?.addOnCompleteListener { token ->
                if (token.isSuccessful) {
                    val idToken = token.result?.token
                    if (idToken != null) {
                        storePreferences("com.butterflies.stepaw.idToken", idToken)
                        Toast.makeText(this,"Got id token",Toast.LENGTH_SHORT).show()
                        taskSuccessCreateNewUser(idToken)
                    }

                } else {
                    Log.d("idToken", "Failed to generate idtoken")
                    Toast.makeText(this,"id token failed",Toast.LENGTH_SHORT).show()
                }
            }
    }

    @SuppressLint("LogNotTimber")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 0) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d("login", "firebaseAuthWithGoogle:$account")
                firebaseAuthWithGoogle(account.idToken!!)

            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("login", "Google sign in failed", e)
            }
        }
    }

    @SuppressLint("LogNotTimber")
    private fun firebaseAuthWithGoogle(idToken: String) {

//        Store id token to shared preferences
        storePreferences("com.butterflies.stepaw.idToken", idToken)

        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    taskSuccessCreateNewUser(idToken)

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("login", "signInWithCredential:failure", task.exception)
                    Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun taskSuccessCreateNewUser(idToken: String) {
        val user = auth.currentUser
        if (user?.uid !== null) {
            id = user.uid
        }
        if (user?.displayName !== null) {

            userName = user.displayName!!
        } else storePreferences("com.butterflies.stepaw.displayName", "invalid")

        if (user?.displayName !== null) {

            firstName = user.displayName!!
        } else storePreferences("com.butterflies.stepaw.firstName", "invalid")
        if (user?.displayName !== null) {

            lastName = user.displayName!!
        } else storePreferences("com.butterflies.stepaw.lastName", "invalid")

        if (user?.email !== null) {

            email = user.email!!
        } else storePreferences("com.butterflies.stepaw.email", "invalid")
        if (this::id.isInitialized && this::userName.isInitialized && this::email.isInitialized) {
            createNewUserCall(id, userName, userName, userName, email, "01", idToken)
        }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, 0)
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        currentUser?.displayName?.let { Log.d("onStart", it) }
    }

    private fun signUp(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information

                    val user = auth.currentUser
                    taskSuccessGetIdToken(user)

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("signup", "createUserWithEmail:failure", task.exception)
                    Log.d("signup", task.result.toString())
                }
            }
    }

    @SuppressLint("LogNotTimber")
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
//            apply()
            commit()
        }

    }

    //Storing user data as a string in shared preferences
    fun storeUser(r: Response<UserModel>) {
        val sharedp = getSharedPreferences("com.butterflies.stepaw", Context.MODE_PRIVATE)
        with(sharedp.edit()) {
            val gson = Gson()
            val json = gson.toJson(r.body())
            putString("com.butterflies.stepaw.user", json.toString())
//        apply()
            commit()
        }

    }

    //    Create a new user in the backend
    private fun createNewUserCall(
        UserID: String,
        UserName: String,
        FirstName: String,
        LastName: String,
        EmailID: String,
        BluetoothID: String,
        token: String
    ) {

        val usermodel = UserModel(UserID, UserName, FirstName, LastName, EmailID, BluetoothID)
        val newUserRequest = service.createUser(token = " Bearer $token", usermodel)
        newUserRequest.enqueue(object : Callback<UserModel> {
            @SuppressLint("LogNotTimber")
            override fun onResponse(call: Call<UserModel>, response: Response<UserModel>) {
//                Checking response status
                if (response.code() == 200) {
                    storeUser(response)
                    Log.d("authUI",response.message())
                    Intent(this@AuthUIHost, DogList::class.java).run { startActivity(this) }
                }
                if (response.code() == 500) {
                    val personWithID = service.getPersonWithId(token = " Bearer $token", UserID)
                    personWithID.enqueue(object : Callback<UserModel> {
                        override fun onResponse(
                            call: Call<UserModel>,
                            response: Response<UserModel>
                        ) {
                            storeUser(response)
                            if (response.code() == 200) {
                                Intent(this@AuthUIHost, DogList::class.java).run {
                                    startActivity(
                                        this
                                    )
                                }
                            }else{
                                Log.d("authUI",response.message())
                            }
                        }

                        override fun onFailure(call: Call<UserModel>, t: Throwable) {
                            Log.d("authUI", "We don't know what's happening around here.")
                        }

                    })
                }

            }

            @SuppressLint("LogNotTimber")
            override fun onFailure(call: Call<UserModel>, t: Throwable) {
                Log.d("authUI", t.message.toString())
            }

        })
    }
}



