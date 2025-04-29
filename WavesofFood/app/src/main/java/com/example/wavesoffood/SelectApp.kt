package com.example.wavesoffood

import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.wavesoffood.databinding.ActivitySelectAppBinding

class SelectApp : AppCompatActivity() {

    private lateinit var binding : ActivitySelectAppBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectAppBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.userLoginBtn.setOnClickListener {
            val intent = Intent(this, StartActivity::class.java)
            startActivity(intent)
        }

        binding.adminLoginBtn.setOnClickListener {
            val intent = Intent()
            intent.setClassName(
                "com.example.adminfoodies",
                "com.example.adminfoodies.SplashScreenActivity"
            )
            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(this, "Admin app is not installed!", Toast.LENGTH_SHORT).show()
            }
        }

    }
}