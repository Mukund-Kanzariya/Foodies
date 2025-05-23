package com.example.adminfoodies

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import com.example.wavesoffood.LoginActivity

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

//        val buttonBack : ImageView = findViewById(R.id.backButton)
//
//        buttonBack.setOnClickListener {
//            onBackPressedDispatcher.onBackPressed()
//        }

        val postDelayed = Handler().postDelayed(
            {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            },
            3000,
        )
    }
}