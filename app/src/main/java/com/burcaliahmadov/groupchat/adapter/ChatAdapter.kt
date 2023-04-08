package com.burcaliahmadov.groupchat.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.burcaliahmadov.groupchat.activity.ChatActivity
import com.burcaliahmadov.groupchat.databinding.ChatUserItemBinding
import com.burcaliahmadov.groupchat.model.UserModel

class ChatAdapter(var context: Context,var list:ArrayList<UserModel>): RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    inner class  ChatViewHolder(val binding:ChatUserItemBinding) : ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val binding=ChatUserItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ChatViewHolder(binding)

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val user=list[position]
        Glide.with(context).load(user.imgUrl).into(holder.binding.userImage)
        holder.binding.username.text=user.name
        holder.itemView.setOnClickListener {
            val intent= Intent(context,ChatActivity::class.java)
            intent.putExtra("uid",user.uId)
            context.startActivity(intent)
        }
    }
}