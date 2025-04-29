package com.example.wavesoffood.Fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.wavesoffood.LoginActivity
import com.example.wavesoffood.databinding.FragmentProfileBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        binding.buttonLogout.setOnClickListener {
            Toast.makeText(requireContext(), "Logging out...", Toast.LENGTH_SHORT).show()
            Handler(Looper.getMainLooper()).postDelayed({
                logoutUser()
            }, 1000) // 1 second delay
        }

        fetchUserProfile()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // ✅ Prevent memory leaks
    }

    private fun fetchUserProfile() {
        val sharedPref = requireActivity().getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        val userId = sharedPref.getInt("user_id", -1)

        if (userId == -1) {
            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        val url = "http://192.168.100.10/foodiesApi/getUser.php"
        val jsonRequest = JSONObject().apply {
            put("user_id", userId)
        }

        val requestQueue = Volley.newRequestQueue(requireContext())
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url, jsonRequest,
            { response ->
                try {
                    Log.d("ProfileFragment", "API Response: $response")

                    if (response.optString("status") == "success") {
                        val userData = response.optJSONObject("data") ?: JSONObject()

                        binding.userUsername.text = "Username: ${userData.optString("username", "N/A")}"
                        binding.userEmail.text = "Email: ${userData.optString("email", "N/A")}"
                        binding.userAddress.text = "Address: ${userData.optString("address", "N/A")}"
                        binding.userMobile.text = "Mobile: ${userData.optString("mobile", "N/A")}"

                    } else {
                        Toast.makeText(requireContext(), response.optString("message", "User not found"), Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Log.e("ProfileFragment", "Error parsing JSON", e)
                    Toast.makeText(requireContext(), "Error parsing data", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                Log.e("ProfileFragment", "Volley Error: ${error.message}", error)
                Toast.makeText(requireContext(), "Failed to fetch profile", Toast.LENGTH_SHORT).show()
            }
        )

        requestQueue.add(jsonObjectRequest)
    }

    private fun logoutUser() {
        // ✅ Clear SharedPreferences session data
        val sharedPreferences = requireActivity().getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear() // Remove stored session data
        editor.apply()

        // ✅ Navigate to LoginActivity and clear back stack
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}
