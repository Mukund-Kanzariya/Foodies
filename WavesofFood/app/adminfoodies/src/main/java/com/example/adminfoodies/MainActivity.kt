package com.example.adminfoodies

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.example.adminfoodies.Fragments.Products
import com.example.adminfoodies.Fragments.Users
import com.example.adminfoodies.databinding.ActivityMainBinding
import com.example.wavesoffood.LoginActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)

        // Load user session data
        val username = sharedPreferences.getString("username", "N/A")
        val email = sharedPreferences.getString("email", "N/A")
        val imageName = sharedPreferences.getString("image", null)

        // Set to views
        binding.userName.text = username
        binding.userEmail.text = email

        val imageUrl = "http://192.168.100.10/foodiesApi/uploads/$imageName"

        imageUrl?.let {
            Glide.with(this)
                .load(imageUrl)
                .into(binding.userImage)
        }

        // Logout button click listener
        binding.logoutButton.setOnClickListener {
            sharedPreferences.edit().clear().apply() // Clear session
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        // Setup Navigation
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)

        val goTo = intent.getStringExtra("goTo")

        if (goTo == "users") {
            binding.bottomNavigationView.selectedItemId = R.id.users
        }

        val goTopr = intent.getStringExtra("goToPr")

        if (goTopr == "products") {
            binding.bottomNavigationView.selectedItemId = R.id.products
        }


    }
}
