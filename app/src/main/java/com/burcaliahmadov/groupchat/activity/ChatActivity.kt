package com.burcaliahmadov.groupchat.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.burcaliahmadov.groupchat.adapter.MessageAdapter
import com.burcaliahmadov.groupchat.databinding.ActivityChatBinding
import com.burcaliahmadov.groupchat.model.MessageModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import java.util.Date

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var senderUid:String
    private lateinit var receiverUid:String
    private lateinit var senderRoom:String
    private lateinit var receiverRoom:String
    private lateinit var list:ArrayList<MessageModel>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        list= ArrayList()


        senderUid=FirebaseAuth.getInstance().uid.toString()
        receiverUid=intent.getStringExtra("uid")!!
        senderRoom=senderUid+receiverUid
        receiverRoom=receiverUid+senderUid

        database=FirebaseDatabase.getInstance()

        binding.sendImage.setOnClickListener{
            if(binding.messageBox.text.isEmpty()){
                Toast.makeText(this, "Please enter your message!", Toast.LENGTH_SHORT).show()
            }
            else{
                val message=MessageModel(binding.messageBox.text.toString(),senderUid,Date().time)
                val randomkey=database.reference.push().key

                database.reference.child("chats").child(senderRoom)
                    .child("message").child(randomkey!!).setValue(message).addOnSuccessListener {

                        database.reference.child("chats").child(receiverRoom)
                            .child("message").child(randomkey!!).setValue(message).addOnSuccessListener {

                                binding.messageBox.text=null
                                Toast.makeText(this, "Message sent!", Toast.LENGTH_SHORT).show()
                            }
                    }

            }
        }

        database.reference.child("chats").child(senderRoom).child("message").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                for(snapshot1 in snapshot.children){
                    val data=snapshot1.getValue(MessageModel::class.java)
                    list.add(data!!)
                    
                }
                
                val adapter=MessageAdapter(this@ChatActivity,list)
                adapter.notifyDataSetChanged()
                binding.chatRecyclerview.layoutManager=LinearLayoutManager(this@ChatActivity)
                binding.chatRecyclerview.adapter=adapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ChatActivity, "Error: $error", Toast.LENGTH_SHORT).show()
            }

        })
    }


}