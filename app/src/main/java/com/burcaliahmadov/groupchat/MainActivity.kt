package com.burcaliahmadov.groupchat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.burcaliahmadov.groupchat.activity.NumberActivity
import com.burcaliahmadov.groupchat.adapter.ViewPagerAdapter
import com.burcaliahmadov.groupchat.databinding.ActivityMainBinding
import com.burcaliahmadov.groupchat.ui.CallFragment
import com.burcaliahmadov.groupchat.ui.ChatFragment
import com.burcaliahmadov.groupchat.ui.StatusFragment
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var  binding:ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        val view :View= binding!!.root
        setContentView(view)

        val fragmentArrayList=ArrayList<Fragment>()
        fragmentArrayList.add(ChatFragment())
        fragmentArrayList.add(StatusFragment())
        fragmentArrayList.add(CallFragment())

        auth=FirebaseAuth.getInstance()

        if(auth.currentUser==null){
            val intent=Intent(this@MainActivity,NumberActivity::class.java)
            startActivity(intent)
            finish()
        }

        val adapter=ViewPagerAdapter(this,supportFragmentManager,fragmentArrayList)
        binding!!.viewPager.adapter=adapter
        binding!!.tabs.setupWithViewPager(binding!!.viewPager)
    }

}