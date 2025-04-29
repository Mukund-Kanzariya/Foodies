package com.example.wavesoffood

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.wavesoffood.Fragment.HistoryFragment
import org.json.JSONArray
import org.json.JSONObject

class ShippingActivity : AppCompatActivity() {

    private lateinit var nameShipping: EditText
    private lateinit var phoneShipping: EditText
    private lateinit var emailShipping: EditText
    private lateinit var cityShipping: EditText
    private lateinit var addressShipping: EditText
    private lateinit var paymentMethodSpinner: Spinner
    private lateinit var placeOrderBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shipping)

        // Initialize views
        nameShipping = findViewById(R.id.nameShipping)
        phoneShipping = findViewById(R.id.phoneShipping)
        emailShipping = findViewById(R.id.emailShipping)
        cityShipping = findViewById(R.id.cityShipping)
        addressShipping = findViewById(R.id.addressShipping)
        paymentMethodSpinner = findViewById(R.id.paymentMethodSpinner)
        placeOrderBtn = findViewById(R.id.PlaceOrder)

        // Get cart data passed from CartFragment
        val cartItems = intent.getStringExtra("cart_data")

        placeOrderBtn.setOnClickListener {
            placeOrder(cartItems)


        }

    }

    private fun placeOrder(cartItems: String?) {
        if (cartItems.isNullOrEmpty()) {
            Toast.makeText(this, "No cart data available", Toast.LENGTH_SHORT).show()
            return
        }

        val name = nameShipping.text.toString().trim()
        val phone = phoneShipping.text.toString().trim()
        val email = emailShipping.text.toString().trim()
        val city = cityShipping.text.toString().trim()
        val address = addressShipping.text.toString().trim()
        val paymentMethod = paymentMethodSpinner.selectedItem.toString()

        if (name.isEmpty() || phone.isEmpty() || email.isEmpty() || city.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val sharedPref = getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        val userId = sharedPref.getInt("user_id", -1)

        if (userId == -1) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }


        // Extract total price
        val cartData = JSONObject(cartItems ?: "")
        val totalPrice = cartData.getDouble("total_price")

        val orderDetails = JSONObject().apply {
            put("user_id", userId)
            put("name", name)
            put("phone", phone)
            put("email", email)
            put("city", city)
            put("address", address)
            put("paymentMethod", paymentMethod)
            put("total_price", totalPrice)
            put("cart_items", cartData.getJSONArray("items"))
        }


        val url = "http://192.168.100.10/foodiesApi/placeOrder.php"

        val requestQueue = Volley.newRequestQueue(this)
        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, orderDetails,
            { response ->
                try {
                    if (response.getString("status") == "success") {
                        Toast.makeText(this, "Order placed successfully!", Toast.LENGTH_SHORT).show()

                        val intent = Intent(this@ShippingActivity, MainActivity::class.java)
                        intent.putExtra("goTo", "orders")
                        startActivity(intent)
                        finish()

                    } else {
                        Toast.makeText(this, "Failed to place order: ${response.getString("message")}", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this, "Error parsing response", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                Log.e("ORDER_ERROR", "Volley error: ${error.message}")
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            })

        requestQueue.add(jsonObjectRequest)
    }

}
