//package com.example.wavesoffood
//
//import android.content.Intent
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.example.wavesoffood.Fragment.ProfileFragment
//
//class UsersAdapter(private var users: List<Users>, private val context: ProfileFragment) :
//    RecyclerView.Adapter<UsersAdapter.UserViewHolder>() {
//
//    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val nameTextView: TextView = itemView.findViewById(R.id.username)
//        val emailTextView: TextView = itemView.findViewById(R.id.useremail)
//        val updateButton: TextView = itemView.findViewById(R.id.userUpdateButton)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)
//        return UserViewHolder(view)
//    }
//
//    override fun getItemCount(): Int = users.size
//
//    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
//        val user = users[position]
//        holder.nameTextView.text = user.name
//        holder.emailTextView.text = user.email
//
//        holder.updateButton.setOnClickListener {
//            val intent = Intent(holder.itemView.context, UpdateUsers::class.java).apply {
//                putExtra("user_id", user.id)
//            }
//            holder.itemView.context.startActivity(intent)
//        }
//    }
//
//    fun refreshData(newUsers: List<Users>) {
//        users = newUsers
//        notifyDataSetChanged()
//    }
//}
