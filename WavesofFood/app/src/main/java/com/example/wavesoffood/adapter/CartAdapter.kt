//package com.example.wavesoffood.adapter
//
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.example.wavesoffood.databinding.CartItemsBinding
//import com.example.wavesoffood.R  // Ensure you import your R file
//
//class CartAdapter(
//    private val cartItems: MutableList<String>,
//    private val cartItemPrices: ArrayList<Double>,
//    private val cartImages: MutableList<String>,
//    private val deleteItemCallback: (position: Int) -> Unit // Callback for deletion
//) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {
//
//    private val itemQuantities = MutableList<Int>(cartItems.size) { 1 }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
//        val binding = CartItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return CartViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
//        if (cartItems.isEmpty() || cartItemPrices.isEmpty() || cartImages.isEmpty()) {
//            Log.e("CartAdapter", "One or more lists are empty")
//            return
//        }
//        holder.bind(position)
//    }
//
//    override fun getItemCount(): Int {
//        return if (cartItems.isEmpty()) 1 else cartItems.size
//    }
//
//    inner class CartViewHolder(private val binding: CartItemsBinding) : RecyclerView.ViewHolder(binding.root) {
//        fun bind(position: Int) {
//            binding.apply {
//                cartFoodName.text = cartItems[position]
//                cartItemPrice.text = cartItemPrices[position].toString()
//
//                val imageUrl = cartImages[position]
//                Glide.with(cartImage.context)
//                    .load(imageUrl)
//                    .into(cartImage)
//
//                // Handle button clicks (minus, plus, delete)
//                minusButton.setOnClickListener { decreaseQuantity(position) }
//                plusButton.setOnClickListener { increaseQuantity(position) }
//                deleteButton.setOnClickListener { deleteItem(position) }
//            }
//        }
//
//        private fun decreaseQuantity(position: Int) {
//            if (itemQuantities[position] > 1) {
//                itemQuantities[position]--
//                binding.cartItemQuantity.text = itemQuantities[position].toString()
//            }
//        }
//
//        private fun increaseQuantity(position: Int) {
//            if (itemQuantities[position] < 10) {
//                itemQuantities[position]++
//                binding.cartItemQuantity.text = itemQuantities[position].toString()
//            }
//        }
//
//        private fun deleteItem(position: Int) {
//            if (position >= 0 && position < cartItems.size) {
//                // Call the callback to delete the item from the database
//                deleteItemCallback(position)
//            }
//        }
//    }
//}
//
//

package com.example.wavesoffood.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wavesoffood.databinding.CartItemsBinding

class CartAdapter(
    private val cartItems: MutableList<String>,
    private val cartItemPrices: MutableList<Double>,
    private val cartImages: MutableList<String>,
    private val itemQuantities: MutableList<Int>,  // Add quantity list
    private val cartActionListener: CartActionListener // Use interface
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = CartItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        if (cartItems.isEmpty() || cartItemPrices.isEmpty() || cartImages.isEmpty()) {
            Log.e("CartAdapter", "One or more lists are empty")
            return
        }
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return cartItems.size
    }

    inner class CartViewHolder(private val binding: CartItemsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                cartFoodName.text = cartItems[position]
                cartItemPrice.text = "Rs.${cartItemPrices[position]}"

                // Load image
                Glide.with(cartImage.context)
                    .load(cartImages[position])
                    .into(cartImage)

                // Set quantity
                cartItemQuantity.text = itemQuantities[position].toString()

                // Button clicks for increase, decrease, and delete
                minusButton.setOnClickListener { cartActionListener.onQuantityDecrease(position) }
                plusButton.setOnClickListener { cartActionListener.onQuantityIncrease(position) }
                deleteButton.setOnClickListener { cartActionListener.onItemDelete(position) }
            }
        }
    }

    // Define an interface for handling cart actions
    interface CartActionListener {
        fun onQuantityIncrease(position: Int)
        fun onQuantityDecrease(position: Int)
        fun onItemDelete(position: Int)
    }
}




