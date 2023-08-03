package com.app.jayesh.infobyte

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class OtpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var etOtp: EditText
    private lateinit var btnVerifyContinue: Button
    private lateinit var txtEnterDesc: TextView
    private lateinit var txtDontReceive: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)

        etOtp = findViewById(R.id.etOtp)
        btnVerifyContinue = findViewById(R.id.btnVerifyContinue)
        txtDontReceive = findViewById(R.id.txtDontReceive)
        txtEnterDesc = findViewById(R.id.txtEnterDesc)
        auth = FirebaseAuth.getInstance()


        val code = intent.getStringExtra("code")
        val mono = intent.getStringExtra("mono")
        val mresendToken: PhoneAuthProvider.ForceResendingToken? =
            intent.getParcelableExtra("token")
        txtEnterDesc.text = mono

        etOtp.addTextChangedListener { value ->
            btnVerifyContinue.isEnabled = !(value.isNullOrEmpty() || value.length < 6)
        }
        //setting click listener on button verify and continue
        btnVerifyContinue.setOnClickListener {
            verifyCode(code.toString(), etOtp.text.toString())
        }

        txtDontReceive.setOnClickListener {
            resendVerificationCode(mono.toString(), mresendToken!!)
        }


    }

    private fun verifyCode(authCode: String, etOtp: String) {
        val credentials = PhoneAuthProvider.getCredential(authCode, etOtp)
        signInWithCredentials(credentials)
    }


    private fun signInWithCredentials(credentials: PhoneAuthCredential) {
        auth.signInWithCredential(credentials)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(
                        this,
                        "You have entered wrong OTP, please re-enter correct OTP",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    // [START resend_verification]
    private fun resendVerificationCode(
        phoneNumber: String,
        token: PhoneAuthProvider.ForceResendingToken
    ) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(mCallBack) // OnVerificationStateChangedCallbacks
            .setForceResendingToken(token) // ForceResendingToken from callbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private val mCallBack: PhoneAuthProvider.OnVerificationStateChangedCallbacks =
        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                Toast.makeText(
                    this@OtpActivity,
                    "OTP Resent on your mobile number",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onVerificationCompleted(p0: PhoneAuthCredential) {

            }

            override fun onVerificationFailed(p0: FirebaseException) {

            }

        }


}