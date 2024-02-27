package com.agriculture.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.agriculture.myapplication.databinding.FragmentPhoneBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.*
import java.util.concurrent.TimeUnit

class PhoneFragment : DialogFragment() {

    private lateinit var binding: FragmentPhoneBinding
    private lateinit var verificationCode: String
    private var timeOutSeconds = 60L
    private lateinit var token: PhoneAuthProvider.ForceResendingToken
    private lateinit var phoneNumber: String
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPhoneBinding.inflate(inflater, container, false)


        setInProgress(false)

        binding.loginWithPhone.setOnClickListener {
            phoneNumber = binding.phoneNumber.text.toString()
            sendOpt(phoneNumber, false)
        }

        binding.sendOtp.setOnClickListener {
            val otp = binding.otpCode.text.toString()
            val credential = PhoneAuthProvider.getCredential(verificationCode, otp)
            singIn(credential)
        }

        binding.timeResend.setOnClickListener {
            sendOpt(phoneNumber, true)
        }

        return binding.root
    }

    private fun sendOpt(phoneNumber: String, isResend: Boolean) {
        setInProgress(true)
        val builder = PhoneAuthOptions.newBuilder()
            .setPhoneNumber(phoneNumber)
            .setTimeout(timeOutSeconds, TimeUnit.SECONDS)
            .setActivity(requireActivity())
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                    singIn(phoneAuthCredential)
                    setInProgress(false)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    Toast.makeText(requireContext(), "login failed" + e.message, Toast.LENGTH_SHORT).show()
                    setInProgress(false)
                }

                override fun onCodeSent(s: String, forceResendingToken: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(s, forceResendingToken)
                    verificationCode = s
                    token = forceResendingToken
                    Toast.makeText(requireContext(), "code resend successfully", Toast.LENGTH_SHORT).show()
                    setInProgress(false)
                    setViewsVisible()
                }
            })

        if (isResend) {
            PhoneAuthProvider.verifyPhoneNumber(builder.setForceResendingToken(token).build())
        } else {
            PhoneAuthProvider.verifyPhoneNumber(builder.build())
        }
    }

    private fun singIn(phoneAuthCredential: PhoneAuthCredential) {
        startResendTimer()
        setInProgress(true)
        auth.signInWithCredential(phoneAuthCredential).addOnCompleteListener { task ->
            setInProgress(false)
            if (task.isSuccessful) {
                Toast.makeText(requireContext(), "register complete", Toast.LENGTH_SHORT).show()
                startActivity(Intent(requireContext(), HomeActivity::class.java))
            } else {
                Toast.makeText(requireContext(), "otp verification failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startResendTimer() {
        binding.timeResend.isEnabled = false
        val timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            @SuppressLint("SetTextI18n")
            override fun run() {
                timeOutSeconds--
                binding.timeResend.text = "resend code  in $timeOutSeconds s"
                if (timeOutSeconds == 0L) {
                    timeOutSeconds = 60L
                    timer.cancel()
                    requireActivity().runOnUiThread {
                        binding.timeResend.isEnabled = true
                    }
                }
            }
        }, 0, 1000)
    }

    private fun setInProgress(inProgress: Boolean) {
        if (inProgress) {
            binding.progressBar.visibility = View.VISIBLE
            binding.loginWithPhone.visibility = View.INVISIBLE
        } else {
            binding.progressBar.visibility = View.INVISIBLE
            binding.loginWithPhone.visibility = View.VISIBLE
        }
    }

    private fun setViewsVisible() {
        binding.otpCode.visibility = View.VISIBLE
        binding.sendOtp.visibility = View.VISIBLE
        binding.phoneNumber.visibility = View.GONE
        binding.loginWithPhone.visibility = View.GONE
    }
}
