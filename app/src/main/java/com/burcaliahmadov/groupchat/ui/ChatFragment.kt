package com.burcaliahmadov.groupchat.ui

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.burcaliahmadov.groupchat.R
import com.burcaliahmadov.groupchat.adapter.ChatAdapter
import com.burcaliahmadov.groupchat.databinding.FragmentChatBinding
import com.burcaliahmadov.groupchat.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatFragment : Fragment() {
    private lateinit var binding: FragmentChatBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var userList:ArrayList<UserModel>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentChatBinding.inflate(layoutInflater)

        database=FirebaseDatabase.getInstance()

        userList= ArrayList()

        database.reference.child("user")
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    //userList.clear()
                    for(snapshot1 in snapshot.children){

                        val user=snapshot1.getValue(UserModel::class.java)
                        if(user!!.uId!=FirebaseAuth.getInstance().uid){
                            userList.add(user)
                            println(user.name+user.number)
                        }
                    }
                    val adapter=ChatAdapter(requireContext(),userList)
                    adapter!!.notifyDataSetChanged()
                    binding.userListRecyeclerView.layoutManager= LinearLayoutManager (requireContext())
                    binding.userListRecyeclerView.adapter=adapter

                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w(TAG, "loadPost:onCancelled", error.toException())
                }

            })
        return binding.root
    }

}