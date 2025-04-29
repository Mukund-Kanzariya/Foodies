package com.example.adminfoodies

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.Volley
import com.example.adminfoodies.databinding.ActivityAddProductBinding
import org.json.JSONObject
import java.io.InputStream

class AddProduct : AppCompatActivity() {

    private lateinit var binding: ActivityAddProductBinding
    private var imageUri: Uri? = null
    private val apiUrl = "http://192.168.100.10/foodiesApi/addProduct.php"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }


        binding.selectImage.setOnClickListener {
            pickImage.launch("image/*")
        }

        binding.addProductButton.setOnClickListener {
            val name = binding.productName.text.toString().trim()
            val price = binding.productPrice.text.toString().trim()

            if (name.isEmpty() || price.isEmpty() || imageUri == null) {
                Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show()
            } else {
                addProduct(name, price, imageUri!!)
            }
        }
    }

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            imageUri = uri
            binding.selectedImage.setImageURI(uri)
        }
    }

    private fun addProduct(name: String, price: String, imageUri: Uri) {
        val url = "http://192.168.100.10/foodiesApi/addProduct.php"
        val requestQueue = Volley.newRequestQueue(this)

        val request = object : VolleyMultipartRequest(
            Method.POST, url,
            Response.Listener { response ->
                Toast.makeText(this@AddProduct, "Product added successfully!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@AddProduct, MainActivity::class.java)
                intent.putExtra("goToPr", "products")
                startActivity(intent)
                finish()
            },
            Response.ErrorListener { error ->
                Toast.makeText(this@AddProduct, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        ) {
            init {
                setParams(
                    mapOf(
                        "name" to name,
                        "price" to price
                    )
                )

                val inputStream = contentResolver.openInputStream(imageUri)
                val imageBytes = inputStream?.readBytes()
                inputStream?.close()

                if (imageBytes != null) {
                    val uniqueName = "product_${System.currentTimeMillis()}.jpg"
                    addFileUpload(
                        "image",
                        DataPart(uniqueName, imageBytes, "image/jpeg")
                    )
                }

            }
        }

        requestQueue.add(request)
    }

}
