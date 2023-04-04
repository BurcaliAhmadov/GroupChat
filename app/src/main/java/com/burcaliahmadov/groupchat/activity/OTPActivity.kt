package com.burcaliahmadov.groupchat.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.burcaliahmadov.groupchat.R
import com.burcaliahmadov.groupchat.databinding.ActivityOtpactivityBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class OTPActivity : AppCompatActivity() {
    private lateinit var binding:ActivityOtpactivityBinding
    private lateinit var auth:FirebaseAuth
    private lateinit var verificationId:String
    private lateinit var dialog:AlertDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityOtpactivityBinding.inflate(layoutInflater)
        val view: View =binding.root
        setContentView(view)

        auth=FirebaseAuth.getInstance()

        val builder=AlertDialog.Builder(this)
        builder.setMessage("Please waiting...")
        builder.setTitle("Loading")
        builder.setCancelable(false)

        dialog=builder.create()
        dialog.show()

        val phoneNumber="+994"+intent.getStringExtra("number")
        println(phoneNumber)

        val options=PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L,TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(object :PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                    //
                }

                override fun onVerificationFailed(p0: FirebaseException) {
                    dialog.dismiss()
                    Toast.makeText(this@OTPActivity,"Please, try again! $p0",Toast.LENGTH_LONG)
                }

                override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(p0, p1)
                    dialog.dismiss()
                    verificationId=p0
                }

            }).build()

        PhoneAuthProvider.verifyPhoneNumber(options)

        binding.button2.setOnClickListener {
            var otp=binding.otp.text
            if(otp!!.isNotEmpty()){
                val credential=PhoneAuthProvider.getCredential(verificationId,otp.toString())

                auth.signInWithCredential(credential)
                    .addOnCompleteListener {
                        if(it.isSuccessful){
                            dialog.dismiss()
                            val intent= Intent(this@OTPActivity,ProfileActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        else{
                            dialog.dismiss()
                            Toast.makeText(
                                this@OTPActivity,
                                "error ${it.exception}",
                                Toast.LENGTH_SHORT
                            ).show()
                            dialog.show()
                        }
                    }
            }
            else{
                Toast.makeText(this@OTPActivity, "Please, enter OTP", Toast.LENGTH_SHORT).show()
            }
        }


    }
}