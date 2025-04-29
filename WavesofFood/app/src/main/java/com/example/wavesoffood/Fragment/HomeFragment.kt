package com.example.wavesoffood.Fragment

import PopulerAdapter
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.wavesoffood.R
import com.example.wavesoffood.databinding.FragmentHomeBinding
import org.json.JSONArray

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var populerAdapter: PopulerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        val sharedPref = requireActivity().getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        val userId = sharedPref.getInt("user_id", -1)
        val username = sharedPref.getString("username", "Guest") ?: "Guest"

//        binding.usernameTextView.text = username

        setupImageSlider()
        fetchPoductItems(userId)

        return binding.root
    }

    private fun setupImageSlider() {
        val imageList = arrayListOf(
            SlideModel(R.drawable.banner1, ScaleTypes.FIT),
            SlideModel(R.drawable.banner2, ScaleTypes.FIT),
            SlideModel(R.drawable.banner3, ScaleTypes.FIT),
            SlideModel(R.drawable.banner4, ScaleTypes.FIT),
        )
        binding.imageSlider.setImageList(imageList, ScaleTypes.FIT)
    }

    private fun fetchPoductItems(userId: Int) {
        val url = "http://192.168.100.10/foodiesApi/selectProduct.php"

        val request = StringRequest(Request.Method.GET, url, { response ->
            try {
                val jsonObject = org.json.JSONObject(response)
                val success = jsonObject.getBoolean("success")

                if (success) {
                    val jsonArray = jsonObject.getJSONArray("products")

                    val names = mutableListOf<String>()
                    val prices = mutableListOf<String>()
                    val images = mutableListOf<String>()

                    for (i in 0 until jsonArray.length()) {
                        val item = jsonArray.getJSONObject(i)

                        val name = item.getString("name")
                        val price = item.getString("price")
                        val image = item.getString("image")

                        names.add(name)
                        prices.add(price)
                        images.add(image)
                    }

                    populerAdapter = PopulerAdapter(userId, names, prices, images)
                    binding.PopulerRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                    binding.PopulerRecyclerView.adapter = populerAdapter
                } else {
                    Log.e("FetchPopularFood", "No products found")
                }
            } catch (e: Exception) {
                Log.e("FetchPopularFood", "Parsing error: ${e.message}")
            }
        }, { error ->
            Log.e("FetchPopularFood", "Volley error: ${error.message}")
        })

        Volley.newRequestQueue(requireContext()).add(request)
    }

}
