package com.burcaliahmadov.groupchat.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.burcaliahmadov.groupchat.databinding.ReceiverItemBinding
import com.burcaliahmadov.groupchat.databinding.SentItemBinding
import com.burcaliahmadov.groupchat.model.MessageModel
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(var context: Context,var list:ArrayList<MessageModel>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val ITEM_SENT=1
    val ITEM_RECEIVE=2


    override fun getItemViewType(position: Int): Int {
        return if(FirebaseAuth.getInstance().uid==list[position].senderId) ITEM_SENT else ITEM_RECEIVE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val sentBinding=SentItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        val receiverBinding=ReceiverItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return if(viewType==ITEM_SENT){
            SentViewHolder(sentBinding)
        }
        else{
            ReceiverViewHolder(receiverBinding)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message=list[position]
        if(holder.itemViewType==ITEM_SENT){
            val viewholder=holder as SentViewHolder
            viewholder.binding.sentText.text=message.message
        }else{
            val viewholder=holder as ReceiverViewHolder
            viewholder.binding.receiverText.text=message.message
        }
    }

    inner class SentViewHolder(val binding:SentItemBinding) : ViewHolder(binding.root) {

    }
    inner class ReceiverViewHolder(var binding:ReceiverItemBinding) : ViewHolder(binding.root) {

    }
}