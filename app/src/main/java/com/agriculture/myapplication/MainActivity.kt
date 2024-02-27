package com.agriculture.myapplication

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.agriculture.myapplication.Utils.Utils
import com.agriculture.myapplication.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var firebaseAuth = FirebaseAuth.getInstance()
    private lateinit var auth1:FirebaseAuth
    private lateinit var database: FirebaseDatabase



    ///

    private lateinit var auth: FirebaseAuth

    private val googleSignInLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    // Google Sign In failed
                    Toast.makeText(this, "Google Sign In failed", Toast.LENGTH_SHORT).show()
                }
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database = FirebaseDatabase.getInstance()

        // registration pop up
        binding.createAccount.setOnClickListener {
            val fragment = RegisterFragment()
            fragment.show(supportFragmentManager, null)
        }


        binding.logIn.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()

            if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password))
                Utils.showToast(baseContext, "required email and password")


            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener{
                    if (it.isSuccessful) {
                        Utils.showToast(baseContext, "Welcome !")
                        startActivity(Intent(baseContext, HomeActivity::class.java))
                    }
                    else
                        Utils.showToast(baseContext, "Wrong email or password verification")
                }
        }


        // login with google account
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id))
            .requestEmail()
            .build()

        auth = FirebaseAuth.getInstance()

        binding.gmailRegister.setOnClickListener{
            val signInIntent = GoogleSignIn.getClient(this, gso).signInIntent
            googleSignInLauncher.launch(signInIntent)
        }


        // phone registration
        binding.phoneRegister.setOnClickListener{
            val fragment = PhoneFragment()
            fragment.show(supportFragmentManager, null)
        }

        // programming the dashboard button
        binding.dash.setOnClickListener {
            val fragment = DashFragment()
            fragment.show(supportFragmentManager, null)
        }


    }


    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    Toast.makeText(this, "Authentication successful", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(baseContext, HomeActivity::class.java))
                } else {
                    // Sign in failed
                    Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }
    }
}