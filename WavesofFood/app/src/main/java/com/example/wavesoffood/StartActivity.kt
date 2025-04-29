package com.example.wavesoffood

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        val button: Button = findViewById(R.id.nextButton)
        val buttonBack : ImageView = findViewById(R.id.backButton)

        buttonBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        button.setOnClickListener{
            val intent = Intent(this,LoginActivity::class.java);
            startActivity(intent)
        }

    }
}