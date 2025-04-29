import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.wavesoffood.databinding.PopularItemsBinding

class PopulerAdapter(
    private val userId: Int,
    private val names: List<String>,
    private val prices: List<String>,
    private val imageUrls: List<String>  // Use URL or image filename
) : RecyclerView.Adapter<PopulerAdapter.PopulerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopulerViewHolder {
        val binding = PopularItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PopulerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PopulerViewHolder, position: Int) {
        val name = names[position]
        val price = prices[position]
        val imageUrl = imageUrls[position]

        holder.bind(name, price, imageUrl)

        holder.binding.AddToCartPopular.setOnClickListener {
            val imageName = imageUrl.substringAfterLast("/")
            addToCart(holder.binding.root.context, userId, name, price, imageName)
        }
    }

    override fun getItemCount(): Int = names.size

    class PopulerViewHolder(val binding: PopularItemsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(name: String, price: String, imageUrl: String) {
            binding.populerFoodName.text = name
            binding.populerItemPrice.text = "Rs.$price"

            Glide.with(binding.root.context)
                .load(imageUrl)
                .into(binding.populerImage)
        }
    }

    private fun addToCart(context: android.content.Context, userId: Int, foodName: String, price: String, image: String) {
        val url = "http://192.168.100.10/foodiesApi/addToCart.php"

        val request = object : StringRequest(Request.Method.POST, url,
            { response ->
                Toast.makeText(context, "Added to Cart!", Toast.LENGTH_SHORT).show()
            },
            { error ->
                Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }) {
            override fun getParams(): Map<String, String> {
                return hashMapOf(
                    "user_id" to userId.toString(),
                    "food_name" to foodName,
                    "price" to price,
                    "image" to image
                )
            }
        }

        Volley.newRequestQueue(context).add(request)
    }
}
