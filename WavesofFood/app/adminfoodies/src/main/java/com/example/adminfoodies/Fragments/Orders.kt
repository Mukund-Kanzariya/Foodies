package com.example.adminfoodies.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.adminfoodies.Adapter.OrderAdapter
import com.example.adminfoodies.databinding.FragmentOrdersBinding

class Orders : Fragment() {

    private lateinit var binding: FragmentOrdersBinding
    private lateinit var adapter: OrderAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrdersBinding.inflate(inflater, container, false)

        fetchOrders()

        return binding.root
    }

    private fun fetchOrders() {
        val url = "http://192.168.100.10/foodiesApi/orderHistoryAdmin.php"
        val requestQueue = Volley.newRequestQueue(requireContext())

        val jsonObjectRequest = JsonObjectRequest(
            url,
            { response ->
                try {
                    if (response.getBoolean("success")) {
                        val orders = response.getJSONArray("orders")

                        val foodNames = mutableListOf<String>()
                        val foodPrices = mutableListOf<String>()
                        val quantities = mutableListOf<String>()
                        val totals = mutableListOf<String>()
                        val images = mutableListOf<String>()
                        val personNames = mutableListOf<String>()
                        val mobiles = mutableListOf<String>()
                        val cities = mutableListOf<String>()
                        val emails = mutableListOf<String>()
                        val orderIds = mutableListOf<String>()
                        val statuses = mutableListOf<String>()

                        for (i in 0 until orders.length()) {
                            val order = orders.getJSONObject(i)

                            val price = order.getString("price").toInt()
                            val quantity = order.getString("quantity").toInt()
                            val totalPrice = price * quantity

                            foodNames.add(order.getString("foodName"))
                            foodPrices.add(order.getString("price"))
                            quantities.add(order.getString("quantity"))
                            totals.add(totalPrice.toString())
                            images.add(order.getString("image_name"))
                            personNames.add(order.getString("name"))
                            mobiles.add(order.getString("phone"))
                            cities.add(order.getString("city"))
                            emails.add(order.getString("email"))
                            orderIds.add(order.getString("id"))
                            statuses.add(order.getString("order_status"))

                        }

                        adapter = OrderAdapter(
                            foodNames, foodPrices, quantities, totals, images,
                            personNames, mobiles, cities, emails, orderIds, statuses, requireContext()
                        )

                        binding.orderHistoryRecycleView.layoutManager = LinearLayoutManager(requireContext())
                        binding.orderHistoryRecycleView.adapter = adapter
                    }
                } catch (e: Exception) {
                    Log.e("ORDER_FETCH_ERROR", "Error: ${e.message}")
                }
            },
            { error ->
                Log.e("ORDER_FETCH_ERROR", "Volley error: ${error.message}")
            }
        )

        requestQueue.add(jsonObjectRequest)
    }

}
