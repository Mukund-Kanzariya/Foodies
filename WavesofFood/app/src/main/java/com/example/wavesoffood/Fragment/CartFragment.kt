//package com.example.wavesoffood.Fragment
//
//import android.content.Context
//import android.content.Intent
//import android.os.Bundle
//import android.util.Log
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Toast
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.android.volley.Request
//import com.android.volley.toolbox.JsonObjectRequest
//import com.android.volley.toolbox.Volley
//import com.example.wavesoffood.ShippingActivity
//import com.example.wavesoffood.SignupActivity
//import com.example.wavesoffood.adapter.CartAdapter
//import com.example.wavesoffood.databinding.FragmentCartBinding
//import org.json.JSONArray
//import org.json.JSONException
//import org.json.JSONObject
//
//class CartFragment : Fragment() {
//
//    private lateinit var binding: FragmentCartBinding
//    private lateinit var adapter: CartAdapter
//
//    private val cartFoodName = ArrayList<String>()
//    private val cartItemPrice = ArrayList<Double>()
//    private val cartImage = ArrayList<String>()
//    private val cartItemIds = ArrayList<Int>()
//
//    private var subtotal = 0.0
//    private val shipping = 100.0
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//
//
//        binding = FragmentCartBinding.inflate(inflater, container, false)
//
//        adapter = CartAdapter(cartFoodName, cartItemPrice, cartImage) { position ->
//            deleteCartItem(position)
//        }
//        binding.cartRecyclerView.layoutManager = LinearLayoutManager(requireContext())
//        binding.cartRecyclerView.adapter = adapter
//
////        binding.ProcedeToCheckout.setOnClickListener {
////            startActivity(Intent(requireContext(), ShippingActivity::class.java))
////        }
//
//        binding.ProcedeToCheckout.setOnClickListener {
//            val cartData = JSONObject().apply {
//                put("items", JSONArray().apply {
//                    for (i in cartFoodName.indices) {
//                        put(JSONObject().apply {
//                            put("foodName", cartFoodName[i])
//                            put("price", cartItemPrice[i])
//                            put("image_url", cartImage[i])
//                            put("quantity", 1) // Assuming quantity is always 1; modify if needed
//                        })
//                    }
//                })
//            }
//
//            val intent = Intent(requireContext(), ShippingActivity::class.java)
//            intent.putExtra("cart_data", cartData.toString())
//            startActivity(intent)
//        }
//
//
//        fetchCartItems()
//
//        return binding.root
//    }
//
//    private fun fetchCartItems() {
//        val sharedPref = requireActivity().getSharedPreferences("UserSession", Context.MODE_PRIVATE)
//        val userId = sharedPref.getInt("user_id", -1)
//
//        if (userId == -1) {
//            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        val url = "http://192.168.100.10/foodiesApi/selectCart.php?user_id=$userId"
//        val requestQueue = Volley.newRequestQueue(requireContext())
//
//        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
//            { response ->
//                try {
//                    cartFoodName.clear()
//                    cartItemPrice.clear()
//                    cartImage.clear()
//                    cartItemIds.clear()
//                    subtotal = 0.0
//
//                    val cartArray = response.getJSONArray("cart_items")
//
//                    if (cartArray.length() == 0) {
//                        Toast.makeText(requireContext(), "Your cart is empty", Toast.LENGTH_SHORT).show()
//                        updateCartTotal()
//                        return@JsonObjectRequest
//                    }
//
//                    for (i in 0 until cartArray.length()) {
//                        val item = cartArray.getJSONObject(i)
//                        cartFoodName.add(item.getString("foodName"))
//                        val price = item.getDouble("price")
//                        cartItemPrice.add(price)
//                        cartImage.add(item.getString("image_url"))
//                        cartItemIds.add(item.getInt("id"))
//                        subtotal += price
//                    }
//
//                    adapter.notifyDataSetChanged()
//                    updateCartTotal()
//                } catch (e: JSONException) {
//                    e.printStackTrace()
//                    Toast.makeText(requireContext(), "Error parsing data", Toast.LENGTH_SHORT).show()
//                }
//            },
//            { error ->
//                val statusCode = error.networkResponse?.statusCode ?: 0
//                if (statusCode == 401) {
//                    Toast.makeText(requireContext(), "Session expired. Please log in again.", Toast.LENGTH_SHORT).show()
//                } else {
//                    Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
//                }
//            })
//
//        requestQueue.add(jsonObjectRequest)
//    }
//
//    private fun updateCartTotal() {
//        val total = subtotal + shipping
//        binding.subTotalViewId.text = " Rs.${"%.2f".format(subtotal)}"
//        binding.shipingViewId.text = " Rs.${"%.2f".format(shipping)}"
//        binding.totalViewId.text = " Rs.${"%.2f".format(total)}"
//    }
//
//    private fun deleteCartItem(position: Int) {
//        val sharedPref = requireActivity().getSharedPreferences("UserSession", Context.MODE_PRIVATE)
//        val userId = sharedPref.getInt("user_id", -1)
//
//        if (userId == -1) {
//            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        val itemId = cartItemIds[position]
//        val url = "http://192.168.100.10/foodiesApi/deleteCart.php?item_id=$itemId"
//        val requestQueue = Volley.newRequestQueue(requireContext())
//
//        val jsonObjectRequest = JsonObjectRequest(
//            Request.Method.GET, url, null,
//            { response ->
//                try {
//                    Log.d("DELETE_RESPONSE", response.toString())
//                    if (response.getString("status") == "success") {
//                        subtotal -= cartItemPrice[position]
//                        cartFoodName.removeAt(position)
//                        cartItemPrice.removeAt(position)
//                        cartImage.removeAt(position)
//                        cartItemIds.removeAt(position)
//                        adapter.notifyItemRemoved(position)
//                        adapter.notifyItemRangeChanged(position, cartFoodName.size)
//                        updateCartTotal()
//                        Toast.makeText(requireContext(), "Item deleted successfully", Toast.LENGTH_SHORT).show()
//                    } else {
//                        Toast.makeText(requireContext(), "Failed to delete item: ${response.getString("message")}", Toast.LENGTH_SHORT).show()
//                    }
//                } catch (e: JSONException) {
//                    e.printStackTrace()
//                    Toast.makeText(requireContext(), "Error parsing response: ${response.toString()}", Toast.LENGTH_LONG).show()
//                }
//            },
//            { error ->
//                Log.e("DELETE_ERROR", "Volley error: ${error.message}")
//                Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
//            })
//
//        requestQueue.add(jsonObjectRequest)
//    }
//
//
//}


package com.example.wavesoffood.Fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.wavesoffood.ShippingActivity
import com.example.wavesoffood.adapter.CartAdapter
import com.example.wavesoffood.databinding.FragmentCartBinding
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class CartFragment : Fragment(), CartAdapter.CartActionListener {

    private lateinit var binding: FragmentCartBinding
    private lateinit var adapter: CartAdapter

    private val cartFoodName = ArrayList<String>()
    private val cartItemPrice = ArrayList<Double>()
    private val cartImage = ArrayList<String>()
    private val cartItemIds = ArrayList<Int>()
    private val itemQuantities = ArrayList<Int>()

    private var subtotal = 0.0
    private val delivery = 50.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(inflater, container, false)

        adapter = CartAdapter(cartFoodName, cartItemPrice, cartImage, itemQuantities, this)
        binding.cartRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.cartRecyclerView.adapter = adapter

        binding.ProcedeToCheckout.setOnClickListener {
            val totalPrice = subtotal + delivery

            val cartData = JSONObject().apply {
                put("total_price", totalPrice)
                put("items", JSONArray().apply {
                    for (i in cartFoodName.indices) {
                        put(JSONObject().apply {
                            put("foodName", cartFoodName[i])
                            put("price", cartItemPrice[i])
                            put("quantity", itemQuantities[i]) // Add quantity
                            val imageName = cartImage[i].substringAfterLast("/")
                            put("image_name", imageName)
                        })
                    }
                })
            }

            val intent = Intent(requireContext(), ShippingActivity::class.java)
            intent.putExtra("cart_data", cartData.toString())
            startActivity(intent)
        }

        fetchCartItems()

        return binding.root
    }

    private fun fetchCartItems() {
        val sharedPref = requireActivity().getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        val userId = sharedPref.getInt("user_id", -1)

        if (userId == -1) {
            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        val url = "http://192.168.100.10/foodiesApi/selectCart.php?user_id=$userId"
        val requestQueue = Volley.newRequestQueue(requireContext())

        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                try {
                    cartFoodName.clear()
                    cartItemPrice.clear()
                    cartImage.clear()
                    cartItemIds.clear()
                    itemQuantities.clear()
                    subtotal = 0.0

                    val cartArray = response.getJSONArray("cart_items")

                    for (i in 0 until cartArray.length()) {
                        val item = cartArray.getJSONObject(i)
                        cartFoodName.add(item.getString("foodName"))
                        val price = item.getDouble("price")
                        cartItemPrice.add(price)
                        cartImage.add(item.getString("image_url"))
                        cartItemIds.add(item.getInt("id"))
                        itemQuantities.add(1)
                        subtotal += price
                    }

                    adapter.notifyDataSetChanged()
                    updateCartTotal()
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(requireContext(), "Error parsing data", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            })

        requestQueue.add(jsonObjectRequest)
    }

    private fun updateCartTotal() {
        val total = subtotal + delivery
        binding.subTotalViewId.text = " Rs.${"%.2f".format(subtotal)}"
        binding.shipingViewId.text = " Rs.${"%.2f".format(delivery)}"
        binding.totalViewId.text = " Rs.${"%.2f".format(total)}"
    }

    override fun onQuantityIncrease(position: Int) {
        if (itemQuantities[position] < 10) {
            itemQuantities[position]++
            subtotal += cartItemPrice[position]
            adapter.notifyItemChanged(position)
            updateCartTotal()
        }
    }

    override fun onQuantityDecrease(position: Int) {
        if (itemQuantities[position] > 1) {
            itemQuantities[position]--
            subtotal -= cartItemPrice[position]
            adapter.notifyItemChanged(position)
            updateCartTotal()
        }
    }

    override fun onItemDelete(position: Int) {
        val itemId = cartItemIds[position]
        val url = "http://192.168.100.10/foodiesApi/deleteCart.php?item_id=$itemId"
        val requestQueue = Volley.newRequestQueue(requireContext())

        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                if (response.getString("status") == "success") {
                    // Subtract full amount of the item (price * quantity)
                    val itemTotalPrice = cartItemPrice[position] * itemQuantities[position]
                    subtotal -= itemTotalPrice

                    // Remove from lists
                    cartFoodName.removeAt(position)
                    cartItemPrice.removeAt(position)
                    cartImage.removeAt(position)
                    cartItemIds.removeAt(position)
                    itemQuantities.removeAt(position)

                    adapter.notifyItemRemoved(position)
                    Toast.makeText(requireContext(), "Item deleted successfully", Toast.LENGTH_SHORT).show()
                    updateCartTotal()
                }
            },
            { error ->
                Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            })

        requestQueue.add(jsonObjectRequest)
    }

}


