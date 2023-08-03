package com.app.jayesh.infobyte

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.RelativeLayout
import androidx.core.widget.addTextChangedListener
import com.google.android.material.button.MaterialButton
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.hbb20.CountryCodePicker
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    // This is the main activity of the app
    lateinit var ccp: CountryCodePicker
    lateinit var etMobileNumber: EditText
    lateinit var btnContinue: MaterialButton
    private lateinit var auth: FirebaseAuth
    lateinit var rlProgressBar: RelativeLayout

    lateinit var countryCode: String
    lateinit var phoneNumber: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etMobileNumber = findViewById(R.id.etMobileNumber)
        btnContinue = findViewById(R.id.btnContinue)
        auth = FirebaseAuth.getInstance()
        rlProgressBar = findViewById(R.id.rlProgressBar)

        etMobileNumber.addTextChangedListener { value ->
            btnContinue.isEnabled = !(value.isNullOrEmpty() || value.length < 10)
        }
        btnContinue.setOnClickListener {
            countryCode = findViewById<CountryCodePicker>(R.id.ccp).selectedCountryCodeWithPlus
            //extracting phone number with country code
            phoneNumber = countryCode + etMobileNumber.text.toString()
            sendVerificationCode(phoneNumber)
            //process bar enable
            rlProgressBar.visibility = View.VISIBLE

        }
    }

    private fun sendVerificationCode(moNo: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(moNo)
            .setActivity(this)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setCallbacks(mCallBack)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private val mCallBack: PhoneAuthProvider.OnVerificationStateChangedCallbacks =
        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                //process bar disable
                rlProgressBar.visibility = View.GONE
                val intent = Intent(this@MainActivity, OtpActivity::class.java)
                intent.putExtra("code", p0)
                intent.putExtra("mono", phoneNumber)
                intent.putExtra("token", p1)
                startActivity(intent)
                finish()
            }

            override fun onVerificationCompleted(p0: PhoneAuthCredential) {

            }

            override fun onVerificationFailed(p0: FirebaseException) {

            }

        }
}