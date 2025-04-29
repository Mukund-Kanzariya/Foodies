package com.example.wavesoffood

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.adminfoodies.MainActivity
import com.example.adminfoodies.databinding.ActivityLoginBinding
import org.json.JSONException
import org.json.JSONObject

class LoginActivityold : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)

        binding.loginButton.setOnClickListener {
            val email = binding.EmailAddress.text.toString().trim()
            val password = binding.Password.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter both email and password!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            loginUser(email, password)
        }

    }

    private fun loginUser(email: String, password: String) {
        val url = "http://192.168.100.10/foodiesApi/adminLogin.php"

        val request = object : StringRequest(Request.Method.POST, url,
            { response ->
                Log.d("LoginResponse", "Server Response: $response")

                try {
                    val jsonResponse = JSONObject(response)

                    if (jsonResponse.getBoolean("success")) {
                        val userId = jsonResponse.getInt("user_id")
                        val username = jsonResponse.getString("username")
                        val userEmail = jsonResponse.getString("email")
                        val userImage = jsonResponse.getString("image")

                        // Save all session data
                        saveUserSession(userId, username, userEmail, userImage)

                        Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this,MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, jsonResponse.getString("message"), Toast.LENGTH_SHORT).show()
                    }
                } catch (e: JSONException) {
                    Toast.makeText(this, "JSON Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }) {
            override fun getParams(): Map<String, String> {
                return hashMapOf(
                    "email" to email,
                    "password" to password
                )
            }
        }

        Volley.newRequestQueue(this).add(request)
    }

    private fun saveUserSession(userId: Int, username: String, email: String, image: String) {
        with(sharedPreferences.edit()) {
            putInt("user_id", userId)
            putString("username", username)
            putString("email", email)
            putString("image", image)
            apply()
        }
    }

//    private fun saveUserSession(userId: Int) {
//        with(sharedPreferences.edit()) {
//            putInt("user_id", userId)
//            apply()
//        }
//    }
}
