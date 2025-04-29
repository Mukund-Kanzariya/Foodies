package com.example.adminfoodies.Fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.adminfoodies.Adapter.ProductAdapter
import com.example.adminfoodies.AddProduct
import com.example.adminfoodies.databinding.FragmentProductsBinding
import org.json.JSONObject

class Products : Fragment() {

    private lateinit var binding: FragmentProductsBinding
    private lateinit var adapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductsBinding.inflate(inflater, container, false)

        binding.addProduct.setOnClickListener {
            val intent = Intent(requireContext(), AddProduct::class.java)
            startActivity(intent)
        }

        fetchProducts()

        return binding.root
    }

    private fun fetchProducts() {
        val url = "http://192.168.100.10/foodiesApi/selectProduct.php"

        val requestQueue = Volley.newRequestQueue(requireContext())
        val jsonObjectRequest = JsonObjectRequest(
            url,
            { response ->
                try {
                    if (response.getBoolean("success")) {
                        val products = response.getJSONArray("products")

                        val names = mutableListOf<String>()
                        val prices = mutableListOf<String>()
                        val images = mutableListOf<String>()
                        val ids = mutableListOf<Int>()

                        for (i in 0 until products.length()) {
                            val item = products.getJSONObject(i)
                            ids.add(item.getInt("id"))
                            names.add(item.getString("name"))
                            prices.add(item.getString("price"))
                            images.add(item.getString("image"))
                        }

                        adapter = ProductAdapter(names, images, prices, ids) { productId, position ->
                            deleteProduct(productId, position)
                        }

                        binding.productRecyclerView.layoutManager =
                            LinearLayoutManager(requireContext())
                        binding.productRecyclerView.adapter = adapter
                    }
                } catch (e: Exception) {
                    Log.e("API_ERROR", "Parsing error: ${e.message}")
                }
            },
            { error ->
                Log.e("API_ERROR", "Volley error: ${error.message}")
            }
        )

        requestQueue.add(jsonObjectRequest)
    }

    private fun deleteProduct(productId: Int, position: Int) {
        val url = "http://192.168.100.10/foodiesApi/deleteProduct.php"

        val requestQueue = Volley.newRequestQueue(requireContext())
        val params = HashMap<String, Any>()
        params["id"] = productId
        val jsonObject = JSONObject(params as Map<*, *>)

        val jsonObjectRequest = object : JsonObjectRequest(
            Method.POST, url, jsonObject,
            { response ->
                try {
                    if (response.getBoolean("success")) {
                        adapter.removeItem(position)
                        Toast.makeText(requireContext(), "Product deleted successfully!", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(requireContext(), "Failed to delete product!", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Log.e("DELETE_ERROR", "JSON error: ${e.message}")
                }
            },
            { error ->
                Log.e("DELETE_ERROR", "Volley error: ${error.message}")
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                return headers
            }
        }

        requestQueue.add(jsonObjectRequest)
    }
}
