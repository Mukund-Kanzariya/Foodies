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
import com.example.adminfoodies.Adapter.UserAdapter
import com.example.adminfoodies.AddUser
import com.example.adminfoodies.databinding.FragmentUsersBinding
import org.json.JSONObject

class Users : Fragment() {

    private lateinit var binding: FragmentUsersBinding
    private lateinit var adapter: UserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUsersBinding.inflate(inflater, container, false)

        binding.addUser.setOnClickListener{
            val intent = Intent(requireContext(),AddUser::class.java)
            startActivity(intent)
        }

        fetchUsers()
        return binding.root
    }

    private fun fetchUsers() {
        val url = "http://192.168.100.10/foodiesApi/selectUser.php"

        val requestQueue = Volley.newRequestQueue(requireContext())
        val jsonObjectRequest = JsonObjectRequest(
            url,
            { response ->
                try {
                    if (response.getBoolean("success")) {
                        val users = response.getJSONArray("users")

                        val ids = mutableListOf<Int>()
                        val names = mutableListOf<String>()
                        val emails = mutableListOf<String>()
                        val images = mutableListOf<String>()

                        for (i in 0 until users.length()) {
                            val user = users.getJSONObject(i)
                            ids.add(user.getInt("id"))
                            names.add(user.getString("name"))
                            emails.add(user.getString("email"))
                            images.add(user.getString("image"))
                        }

                        adapter = UserAdapter(ids, names, emails, images) { userId, position ->
                            deleteUser(userId, position)
                        }

                        binding.userRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                        binding.userRecyclerView.adapter = adapter
                    }
                } catch (e: Exception) {
                    Log.e("USER_FETCH_ERROR", "Error: ${e.message}")
                }
            },
            { error ->
                Log.e("USER_FETCH_ERROR", "Volley error: ${error.message}")
            }
        )

        requestQueue.add(jsonObjectRequest)
    }

    private fun deleteUser(userId: Int, position: Int) {
        val url = "http://192.168.100.10/foodiesApi/deleteUser.php"
        val requestQueue = Volley.newRequestQueue(requireContext())

        val params = HashMap<String, Any>()
        params["id"] = userId
        val jsonObject = JSONObject(params as Map<*, *>)

        val jsonObjectRequest = object : JsonObjectRequest(
            Method.POST, url, jsonObject,
            { response ->
                try {
                    if (response.getBoolean("success")) {
                        adapter.removeItem(position)
                        Toast.makeText(requireContext(), "User deleted successfully!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "Failed to delete user", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Log.e("USER_DELETE_ERROR", "JSON error: ${e.message}")
                }
            },
            { error ->
                Log.e("USER_DELETE_ERROR", "Volley error: ${error.message}")
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                return hashMapOf("Content-Type" to "application/json")
            }
        }

        requestQueue.add(jsonObjectRequest)
    }
}
