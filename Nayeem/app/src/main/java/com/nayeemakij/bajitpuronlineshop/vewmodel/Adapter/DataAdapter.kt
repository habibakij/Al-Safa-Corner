package com.nayeemakij.bajitpuronlineshop.vewmodel.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.nayeemakij.bajitpuronlineshop.R
import com.nayeemakij.bajitpuronlineshop.Model.ProductInfo
import kotlinx.android.synthetic.main.recycler_item.view.*

class DataAdapter(options: FirebaseRecyclerOptions<ProductInfo>) :
    FirebaseRecyclerAdapter<ProductInfo, DataAdapter.DataViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val inflater= LayoutInflater.from(parent.context).inflate(R.layout.recycler_item, parent, false)
        return DataViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int, info: ProductInfo) {
        holder.productName.text = info.name
        holder.productPrize.text = info.prize
        holder.productSize.text = info.size
        holder.productDescription.text = info.description
    }


    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImage: ImageView = itemView.item_product_image
        val productName: TextView = itemView.item_product_name
        val productSize: TextView = itemView.item_product_size
        val productPrize: TextView = itemView.item_product_prize
        val productDescription: TextView = itemView.item_product_description
    }

}