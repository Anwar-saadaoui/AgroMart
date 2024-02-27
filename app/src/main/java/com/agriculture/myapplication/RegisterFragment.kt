package com.agriculture.myapplication

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.agriculture.myapplication.Utils.Utils
import com.google.firebase.auth.FirebaseAuth


class RegisterFragment : DialogFragment() {

    var firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.register_layout_pop_up, container, false)

        val emailField = view.findViewById<EditText>(R.id.email_up)
        val passwordField = view.findViewById<EditText>(R.id.password_up)
        val signUpBtn = view.findViewById<Button>(R.id.sign_up)

        signUpBtn.setOnClickListener {
            val email = emailField.text.toString()
            val password = passwordField.text.toString()


            if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password)) {
                Utils.showToast(requireContext(), "required email and password")
            }

            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener{
                    if (it.isSuccessful)
                        Utils.showToast(requireContext(), "account created successful")
                    else
                        Utils.showToast(requireContext(), "creation account failed")
                }
        }

        return view
    }

}