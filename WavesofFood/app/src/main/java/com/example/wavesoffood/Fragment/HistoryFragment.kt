package com.example.wavesoffood.Fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.wavesoffood.adapter.BuyAgainAdapter
import com.example.wavesoffood.databinding.FragmentHistoryBinding
import org.json.JSONObject
import kotlin.math.roundToInt

class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding
    private lateinit var buyAgainAdapter: BuyAgainAdapter

    private val buyAgainFoodName = ArrayList<String>()
    private val buyAgainFoodPrice = ArrayList<String>()
    private val buyAgainFoodImage = ArrayList<String>()
    private val buyAgainFoodQuantity = ArrayList<Int>()
    private val buyAgainTotalPrice = ArrayList<String>()
    private val buyAgainStatus = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)

        setUpRecyclerView()
        fetchOrderHistory()
        return binding.root
    }

    private fun setUpRecyclerView() {
        buyAgainAdapter = BuyAgainAdapter(buyAgainFoodName, buyAgainFoodPrice, buyAgainFoodImage, buyAgainFoodQuantity, buyAgainTotalPrice, buyAgainStatus)
        binding.BuyAgainRecyclerView.apply {
            adapter = buyAgainAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun fetchOrderHistory() {
        val sharedPref = requireActivity().getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        val userId = sharedPref.getInt("user_id", -1)

        if (userId == -1) {
            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        val url = "http://192.168.100.10/foodiesApi/orderHistory.php"
        val jsonRequest = JSONObject().apply {
            put("user_id", userId)
        }

        val requestQueue = Volley.newRequestQueue(requireContext())
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url, jsonRequest,
            { response ->
                try {
                    Log.d("HistoryFragment", "API Response: $response")

                    if (response.optBoolean("success", false)) {
                        val dataArray = response.getJSONArray("orders")

                        if (dataArray.length() == 0) {
                            Toast.makeText(requireContext(), "No order history found", Toast.LENGTH_SHORT).show()
                            return@JsonObjectRequest
                        }

                        buyAgainFoodName.clear()
                        buyAgainFoodPrice.clear()
                        buyAgainFoodImage.clear()
                        buyAgainFoodQuantity.clear()
                        buyAgainTotalPrice.clear()
                        buyAgainStatus.clear()

                        for (i in 0 until dataArray.length()) {
                            val item = dataArray.getJSONObject(i)

                            val foodName = item.optString("foodName", "Unknown")
                            val priceString = item.optString("price", "0")
                            val price = priceString.toInt()
                            val foodPrice = "Rs.$priceString"
                            val imageUrl = item.optString("image_name", "")
                            val foodQuantity = item.optInt("quantity", 1)
                            val totPrice = price * foodQuantity
                            val totalPrice = "Rs.$totPrice"
                            val status = item.optString("order_status", "Pending")

//                            val totalPrice = price.toDoubleOrNull()?.times(foodQuantity) ?: 0.0
//                            val totalPriceRounded = totalPrice.roundToInt()
//                            val totalPriceString = "Rs. $totalPriceRounded"

                            buyAgainFoodName.add(foodName)
                            buyAgainFoodPrice.add(foodPrice)
                            buyAgainFoodImage.add(imageUrl)
                            buyAgainFoodQuantity.add(foodQuantity)
                            buyAgainTotalPrice.add(totalPrice)
                            buyAgainStatus.add(status)
                        }


                        buyAgainAdapter.notifyDataSetChanged()
                    } else {
                        val message = response.optString("message", "Failed to load order history")
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(requireContext(), "Error parsing data", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                Toast.makeText(requireContext(), "Failed to fetch orders: ${error.message}", Toast.LENGTH_SHORT).show()
                Log.e("HistoryFragment", "Volley Error: ${error.message}")
            }
        )

        requestQueue.add(jsonObjectRequest)
    }

}
