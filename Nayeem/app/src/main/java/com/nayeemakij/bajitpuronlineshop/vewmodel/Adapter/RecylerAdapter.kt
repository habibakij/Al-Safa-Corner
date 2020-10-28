package com.nayeemakij.bajitpuronlineshop.vewmodel.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nayeemakij.bajitpuronlineshop.R
import com.nayeemakij.bajitpuronlineshop.Model.ProductInfo
import com.nayeemakij.bajitpuronlineshop.view.UserPanel.ProductCard
import kotlinx.android.synthetic.main.recycler_item.view.*

class RecylerAdapter(private val context: Context?, private val mutableList: List<ProductInfo>):
    RecyclerView.Adapter<RecylerAdapter.ProductViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecylerAdapter.ProductViewHolder {
        val inflater= LayoutInflater.from(context).inflate(R.layout.recycler_item, parent, false)
        return ProductViewHolder(inflater)
    }

    override fun getItemCount(): Int {
        return mutableList.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val item= mutableList[position]
        holder.productName.text = item.name
        holder.productPrize.text = item.prize
        holder.productSize.text = item.size
        holder.productDescription.text = item.description

        holder.layout.setOnClickListener {
            val intent= Intent(context, ProductCard::class.java)
            intent.putExtra("passItemName", holder.productName.text.toString())
            intent.putExtra("passItemPrize", holder.productPrize.text.toString())
            intent.putExtra("passItemDescription", holder.productDescription.text.toString())
            context?.startActivity(intent)
        }

    }

    class ProductViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val layout: LinearLayout= itemView.recylar_item_view
        val productImage: ImageView = itemView.item_product_image
        val productName: TextView = itemView.item_product_name
        val productSize: TextView = itemView.item_product_size
        val productPrize: TextView = itemView.item_product_prize
        val productDescription: TextView = itemView.item_product_description
    }

}