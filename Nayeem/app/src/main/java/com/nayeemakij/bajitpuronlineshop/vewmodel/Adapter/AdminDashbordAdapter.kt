package com.nayeemakij.bajitpuronlineshop.vewmodel.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nayeemakij.bajitpuronlineshop.view.AdminPanel.*
import com.nayeemakij.bajitpuronlineshop.R

class AdminDashbordAdapter(private val context: Context?, private val titleList:List<String>, private val imageList: List<Int>):
    RecyclerView.Adapter<AdminDashbordAdapter.AdminDashboardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminDashboardViewHolder {
        val inflater= LayoutInflater.from(context).inflate(R.layout.admin_dashbord_recycleritem, parent, false)
        return AdminDashboardViewHolder(inflater)
    }
    override fun onBindViewHolder(holder: AdminDashboardViewHolder, position: Int) {
        holder.productTitle.text= titleList[position]
        holder.productImage.setImageResource(imageList[position])

        holder.productImage.setOnClickListener(){
            if (holder.productTitle.text.toString() == "Library"){
                val intent= Intent(context, LibraryData::class.java)
                context!!.startActivity(intent)
            } else if(holder.productTitle.text.toString() == "Foods"){
                val intent= Intent(context, FoodsData::class.java)
                context!!.startActivity(intent)
            } else if(holder.productTitle.text.toString() == "Stationary"){
                val intent= Intent(context, StationaryData::class.java)
                context!!.startActivity(intent)
            } else if(holder.productTitle.text.toString() == "Toys"){
                val intent= Intent(context, ToysData::class.java)
                context!!.startActivity(intent)
            } else if(holder.productTitle.text.toString() == "Furniture"){
                val intent= Intent(context, FurnitureData::class.java)
                context!!.startActivity(intent)
            }
        }
    }
    override fun getItemCount(): Int {
        return titleList.size
    }
    class AdminDashboardViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val productImage:ImageView= itemView.findViewById(R.id.dashbord_recycler_item_image)
        val productTitle:TextView= itemView.findViewById(R.id.dashbord_recycler_item_title)
    }
}