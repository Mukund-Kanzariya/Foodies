package com.example.wavesoffood

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.wavesoffood.databinding.ActivityUpdateUsersBinding

class UpdateUsers : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateUsersBinding
    private lateinit var db:UsersDatabaseHelper
    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = UsersDatabaseHelper(this)

        userId = intent.getIntExtra("user_id",-1)
        if (userId == -1){
            finish()
            return
        }

        val user = db.getUserById(userId)
        binding.updateName.setText(user.name)
        binding.updateMobile.setText(user.mobile)
        binding.updateAddress.setText(user.address)
        binding.updateEmail.setText(user.email)
        binding.updatePassword.setText(user.password)

        binding.updateUser.setOnClickListener{
            val newName = binding.updateName.text.toString()
            val newMobile = binding.updateMobile.text.toString()
            val newAddress = binding.updateAddress.text.toString()
            val newEmail = binding.updateEmail.text.toString()
            val newPassword =  binding.updatePassword.text.toString()

            val updateUser = Users(userId,newName,newMobile,newAddress,newEmail,newPassword)
            db.updateUser(updateUser)
            finish()
            Toast.makeText(this,"User Updated....",Toast.LENGTH_SHORT).show()
        }

    }
}