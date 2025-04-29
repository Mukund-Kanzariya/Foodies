package com.example.adminfoodies

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.example.adminfoodies.databinding.ActivityAddUserBinding

class AddUser : AppCompatActivity() {

    private lateinit var binding: ActivityAddUserBinding
    private var imageUri: Uri? = null
    private val apiUrl = "http://192.168.100.10/foodiesApi/addUser.php"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }


        binding.selectImage.setOnClickListener {
            pickImage.launch("image/*")
        }

        binding.buttonAddUser.setOnClickListener {
            val name = binding.nameAdduser.text.toString().trim()
            val mobile = binding.mobileAddUser.text.toString().trim()
            val address = binding.addressAddUser.text.toString().trim()
            val email = binding.emailAddUser.text.toString().trim()
            val password = binding.passwordAddUser.text.toString().trim()

            if (name.isEmpty() || mobile.isEmpty() || email.isEmpty() || password.isEmpty() || address.isEmpty() || imageUri == null) {
                Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show()
            } else {
                addUser(name, mobile, address, email, password, imageUri!!)
            }
        }
    }

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            imageUri = uri
            binding.selectedImage.setImageURI(uri)
        }
    }

    private fun addUser(name: String, mobile: String,address: String, email: String, password: String, imageUri: Uri) {
        val requestQueue = Volley.newRequestQueue(this)

        val request = object : VolleyMultipartRequest(
            Method.POST, apiUrl,
            Response.Listener { response ->
                Toast.makeText(this@AddUser, "User added successfully!", Toast.LENGTH_SHORT).show()

                val intent = Intent(this@AddUser, MainActivity::class.java)
                intent.putExtra("goTo", "users")
                startActivity(intent)
                finish()
            },
            Response.ErrorListener { error ->
                Toast.makeText(this@AddUser, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        ) {
            init {
                setParams(
                    mapOf(
                        "name" to name,
                        "mobile" to mobile,
                        "address" to address,
                        "email" to email,
                        "password" to password
                    )
                )

                val inputStream = contentResolver.openInputStream(imageUri)
                val imageBytes = inputStream?.readBytes()
                inputStream?.close()

                if (imageBytes != null) {
                    addFileUpload("image", DataPart("user.jpg", imageBytes, "image/jpeg"))
                }
            }
        }

        requestQueue.add(request)
    }
}
