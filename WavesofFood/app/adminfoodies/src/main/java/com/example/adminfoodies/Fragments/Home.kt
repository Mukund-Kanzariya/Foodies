package com.example.adminfoodies.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.adminfoodies.R

class Home : Fragment() {

    private lateinit var userCount: TextView
    private lateinit var productCount: TextView
    private lateinit var pendingOrdersCount: TextView
    private lateinit var deliveredOrdersCount: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        userCount = view.findViewById(R.id.userCountId)
        productCount = view.findViewById(R.id.productCountId)
        pendingOrdersCount = view.findViewById(R.id.pendingOrdersCountId)
        deliveredOrdersCount = view.findViewById(R.id.deliverdOrdersCountId)

        fetchCounts()

        return view
    }

    private fun fetchCounts() {
        val url = "http://192.168.100.10/foodiesApi/getCounts.php"

        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                if (response.getBoolean("status")) {
                    val data = response.getJSONObject("data")
                    userCount.text = "${data.getString("total_users")}"
                    productCount.text = "${data.getString("total_products")}"
                    pendingOrdersCount.text = "${data.getString("pending_orders")}"
                    deliveredOrdersCount.text = "${data.getString("delivered_orders")}"
                }
            },
            { error ->
                error.printStackTrace()
            }
        )

        Volley.newRequestQueue(requireContext()).add(request)
    }
}
