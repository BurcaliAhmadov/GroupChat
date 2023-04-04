package com.burcaliahmadov.groupchat.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.burcaliahmadov.groupchat.MainActivity
import com.burcaliahmadov.groupchat.R
import com.burcaliahmadov.groupchat.databinding.ActivityNumberBinding
import com.google.firebase.auth.FirebaseAuth

class NumberActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNumberBinding
    private lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityNumberBinding.inflate(layoutInflater)
        val view: View =binding.root
        setContentView(view)

        auth=FirebaseAuth.getInstance()

        if(auth.currentUser!=null){
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }


        binding.button.setOnClickListener {
            var phone=binding.phoneNumber.text
            if(phone!!.isNotEmpty()){
                if(phone.length==9){
                    val intent=Intent(this,OTPActivity::class.java)
                    intent.putExtra("number",phone!!.toString())
                    startActivity(intent)
                    finish()
                }
                else{
                    Toast.makeText(this,"Please enter correct number!",Toast.LENGTH_LONG).show()
                }

            }
            else{
                Toast.makeText(this,"Please enter your number!",Toast.LENGTH_LONG).show()
            }
        }


    }
}