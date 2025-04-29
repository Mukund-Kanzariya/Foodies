package com.example.wavesoffood.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wavesoffood.databinding.BuyAgainItemsBinding
import com.example.wavesoffood.R

class BuyAgainAdapter(
    private val buyAgainFoodName: ArrayList<String>,
    private val buyAgainFoodPrice: ArrayList<String>,
    private val buyAgainFoodImage: ArrayList<String>,
    private val buyAgainFoodQuantity: ArrayList<Int>,
    private val buyAgainTotalPrice: ArrayList<String>,
    private val buyAgainStatus: ArrayList<String>
) : RecyclerView.Adapter<BuyAgainAdapter.BuyAgainViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuyAgainViewHolder {
        val binding = BuyAgainItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BuyAgainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BuyAgainViewHolder, position: Int) {
        if (position in buyAgainFoodName.indices &&
            position in buyAgainFoodPrice.indices &&
            position in buyAgainFoodImage.indices &&
            position in buyAgainFoodQuantity.indices) {
            holder.bind(
                buyAgainFoodName[position],
                buyAgainFoodPrice[position],
                buyAgainFoodImage[position],
                buyAgainFoodQuantity[position],
                buyAgainTotalPrice[position],
                buyAgainStatus[position]
            )
        } else {
            Log.e("BuyAgainAdapter", "Invalid index: $position, Data size mismatch")
        }
    }

    override fun getItemCount(): Int = buyAgainFoodName.size

    class BuyAgainViewHolder(private val binding: BuyAgainItemsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            foodName: String,
            foodPrice: String,
            foodImageUrl: String,
            foodQuantity: Int,
            totalPrice: String,
            status: String
        ) {
            binding.buyAgainFoodNameid.text = foodName
            binding.buyAgainFoodPriceid.text = foodPrice
            binding.buyAgainFoodQuantityid.text = foodQuantity.toString()
            binding.buyAgainTotalid.text = totalPrice

            val status = status

            if (status.equals("Pending", ignoreCase = true)) {
                binding.orderStatusButton.text = "Pending"
                binding.orderStatusButton.setBackgroundColor(android.graphics.Color.RED)
                binding.orderStatusButton.isEnabled = true
            } else {
                binding.orderStatusButton.text = "Delivered"
                binding.orderStatusButton.setBackgroundColor(android.graphics.Color.parseColor("#388E3C"))
                binding.orderStatusButton.isEnabled = false
            }


            val fullImageUrl = if (foodImageUrl.startsWith("http")) foodImageUrl
            else "http://192.168.100.10/foodiesApi/foodimages/$foodImageUrl"

            Log.d("BuyAgainAdapter", "Loading image: $fullImageUrl")

            Glide.with(binding.root.context)
                .load(fullImageUrl)
                .placeholder(R.drawable.burger)
                .error(R.drawable.chips)
                .into(binding.buyAgainFoodImageid)
        }
    }
}
