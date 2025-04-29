package com.example.adminfoodies.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.JsonObjectRequest
import com.bumptech.glide.Glide
import com.example.adminfoodies.databinding.OrdersItemsBinding
import org.json.JSONObject

class OrderAdapter(
    private val foodNames: List<String>,
    private val prices: List<String>,
    private val quantities: List<String>,
    private val totals: List<String>,
    private val images: List<String>,
    private val personNames: List<String>,
    private val mobiles: List<String>,
    private val cities: List<String>,
    private val emails: List<String>,
    private val orderIds: List<String>,
    private val statuses: List<String>,
    private val context: android.content.Context
) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    inner class OrderViewHolder(val binding: OrdersItemsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = OrdersItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding)
    }


    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        with(holder.binding) {
            orderHistoryFoodName.text = foodNames[position]
            orderHistoryFoodPrice.text = "Rs. ${prices[position]}"
            orderHistoryFoodQuantity.text = quantities[position]
            orderHistoryFoodTotal.text = "Rs. ${totals[position]}"
            orderHistoryPersonName.text = personNames[position]
            orderHistoryPersonMobile.text = mobiles[position]
            orderHistoryPersonCity.text = cities[position]
            orderHistoryPersonEmail.text = emails[position]

            Glide.with(holder.itemView.context)
                .load(images[position])
                .into(orderHistoryFoodImage)

            val status = statuses[position]

            if (status.equals("Pending", ignoreCase = true)) {
                orderStatusButton.text = "Accept"
                orderStatusButton.setBackgroundColor(android.graphics.Color.RED)
                orderStatusButton.isEnabled = true
            } else {
                orderStatusButton.text = "Delivered"
                orderStatusButton.setBackgroundColor(android.graphics.Color.parseColor("#388E3C"))
                orderStatusButton.isEnabled = false
            }


            orderStatusButton.setOnClickListener {
                val orderId = orderIds[position]
                val url = "http://192.168.100.10/foodiesApi/updateOrderStatus.php?id=$orderId"
                val jsonBody = JSONObject()
                jsonBody.put("id", orderId)

                val jsonRequest = object : JsonObjectRequest(
                    Method.POST, url, jsonBody,
                    { response ->
                        if (response.getBoolean("success")) {
                            orderStatusButton.text = "Delivered"
                            orderStatusButton.setBackgroundColor(android.graphics.Color.parseColor("#388E3C"))
                            orderStatusButton.isEnabled = false
                            Toast.makeText(context, "Order Delivered", Toast.LENGTH_SHORT).show()
                        }
                    },
                    { error ->
                        Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                    }
                ) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-Type"] = "application/json"
                        return headers
                    }
                }

                com.android.volley.toolbox.Volley.newRequestQueue(context).add(jsonRequest)
            }
        }

    }
        override fun getItemCount(): Int = foodNames.size
}
