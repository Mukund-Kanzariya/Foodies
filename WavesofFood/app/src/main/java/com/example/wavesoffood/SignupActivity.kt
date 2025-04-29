package com.example.wavesoffood

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.wavesoffood.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.AlreadyHaveAnAccount.setOnClickListener{
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }

        binding.buttonCreateAccount.setOnClickListener {
            val name = binding.nameSignup.text.toString().trim()
            val mobile = binding.mobileSignup.text.toString().trim()
            val address = binding.addressSignup.text.toString().trim()
            val email = binding.emailSignup.text.toString().trim()
            val password = binding.passwordSignup.text.toString().trim()

            if (name.isEmpty() || mobile.isEmpty() || address.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show()
            } else {
                registerUser(name, mobile, address, email, password)
            }
        }
    }

    private fun registerUser(name: String, mobile: String, address: String, email: String, password: String) {
        val url = "http://192.168.100.10/foodiesApi/register.php"

        val request = object : StringRequest(Request.Method.POST, url,
            { response ->
                Toast.makeText(this, "Signup Successful!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            },
            { error ->
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["name"] = name
                params["mobile"] = mobile
                params["address"] = address
                params["email"] = email
                params["password"] = password
                return params
            }
        }

        Volley.newRequestQueue(this).add(request)
    }
}


