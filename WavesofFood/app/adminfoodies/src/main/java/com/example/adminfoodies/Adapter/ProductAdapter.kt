package com.example.adminfoodies.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adminfoodies.databinding.ProductsItemsBinding

class ProductAdapter(
    private val nameList: MutableList<String>,
    private val imageList: MutableList<String>,
    private val priceList: MutableList<String>,
    private val idList: MutableList<Int>,
    private val onDeleteClick: (productId: Int, position: Int) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(val binding: ProductsItemsBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ProductsItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.binding.productName.text = nameList[position]
        holder.binding.productPrice.text = "Rs. ${priceList[position]}"

        Glide.with(holder.itemView.context)
            .load(imageList[position])
            .into(holder.binding.productImage)

        holder.binding.deleteButton.setOnClickListener {
            onDeleteClick(idList[position], position)
        }
    }

    override fun getItemCount(): Int = nameList.size

    fun removeItem(position: Int) {
        nameList.removeAt(position)
        imageList.removeAt(position)
        priceList.removeAt(position)
        idList.removeAt(position)
        notifyItemRemoved(position)
    }
}
