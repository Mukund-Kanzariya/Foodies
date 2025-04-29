package com.example.adminfoodies.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adminfoodies.databinding.UsersItemsBinding

class UserAdapter(
    private val idList: MutableList<Int>,
    private val nameList: MutableList<String>,
    private val emailList: MutableList<String>,
    private val imageList: MutableList<String>,
    private val onDeleteClick: (userId: Int, position: Int) -> Unit
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    class UserViewHolder(val binding: UsersItemsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = UsersItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.binding.userName.text = nameList[position]
        holder.binding.userEmail.text = emailList[position]

        Glide.with(holder.itemView.context)
            .load(imageList[position])
            .into(holder.binding.userImage)

        holder.binding.deleteButton.setOnClickListener {
            onDeleteClick(idList[position], position)
        }
    }

    override fun getItemCount(): Int = nameList.size

    fun removeItem(position: Int) {
        idList.removeAt(position)
        nameList.removeAt(position)
        emailList.removeAt(position)
        imageList.removeAt(position)
        notifyItemRemoved(position)
    }
}
