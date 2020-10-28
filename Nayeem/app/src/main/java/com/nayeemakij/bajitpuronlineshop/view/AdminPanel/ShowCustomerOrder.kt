package com.nayeemakij.bajitpuronlineshop.view.AdminPanel

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cc.cloudist.acplibrary.ACProgressConstant
import cc.cloudist.acplibrary.ACProgressFlower
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.*
import com.nayeemakij.bajitpuronlineshop.Model.CustomerInfo
import com.nayeemakij.bajitpuronlineshop.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.custom_toolbar.view.*
import kotlinx.android.synthetic.main.customer_item.view.*

class ShowCustomerOrder : AppCompatActivity() {
    private lateinit var appBar: RelativeLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var reference:DatabaseReference
    private var list: MutableList<CustomerInfo> = mutableListOf()
    lateinit var dialog: ACProgressFlower

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_customer_order)

        setDialog()
        appBar= findViewById(R.id.include_show_customer_toolbar)
        appBar.custom_toolbar_back.setOnClickListener(){
            val intent = Intent(this, AdminDashboard::class.java)
            startActivity(intent)
            finish()
        }
        appBar.custom_toolbar_title.text= "Customer Order"
        viewManager = LinearLayoutManager(this)
        recyclerView = findViewById<RecyclerView>(R.id.show_all_order).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
        }
        reference= FirebaseDatabase.getInstance().reference.child("ProductDetails").child("CustomerOrder")
        showAllData()
    }

    private fun showAllData(){
        val options: FirebaseRecyclerOptions<CustomerInfo> =
            FirebaseRecyclerOptions.Builder<CustomerInfo>().setQuery(reference, CustomerInfo::class.java).build()

        val adapter = object : FirebaseRecyclerAdapter<CustomerInfo, CustomerViewHolder>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerViewHolder {
                return CustomerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.customer_item, parent, false))
            }
            protected override fun onBindViewHolder(holder: CustomerViewHolder, position: Int, model: CustomerInfo) {
                val adapterReference:DatabaseReference = getRef(position).ref
                adapterReference.addValueEventListener(object : ValueEventListener{
                    override fun onCancelled(databaseError: DatabaseError) {
                        Log.i("Error_database: ", databaseError.message)
                    }
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()){
                            dialog.dismiss()
                            holder.customerAddress.text= dataSnapshot.child("customerAddress").value.toString()
                            holder.customerName.text= dataSnapshot.child("customerName").value.toString()
                            holder.customerPhone.text= dataSnapshot.child("customerPhone").value.toString()
                            holder.customerProductName.text= dataSnapshot.child("productName").value.toString()
                            holder.customerProductPrize.text= dataSnapshot.child("productPrize").value.toString()
                            holder.customerProductQuantity.text= (dataSnapshot.child("productQuantity").value.toString()+"X")
                            val imgUrl:String= dataSnapshot.child("productImage").value.toString()
                            Picasso.get().load(imgUrl).into(holder.customerImage)
                        } else{
                            dialog.dismiss()
                            Toast.makeText(this@ShowCustomerOrder, "Data not found", Toast.LENGTH_LONG).show()
                        }
                    }
                })
            }

            override fun onDataChanged() {
                dialog.dismiss()
                // If there are no chat messages, show a view that invites the user to add a message.
                //mEmptyListMessage.setVisibility(if (itemCount == 0) View.VISIBLE else View.GONE)
            }
        }
        recyclerView.adapter = adapter
        adapter.startListening()
    }

    class CustomerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val customerImage:ImageView= itemView.recycler_customer_item_image
        val customerName:TextView= itemView.recycler_customer_name
        val customerPhone:TextView= itemView.recycler_customer_phone
        val customerAddress:TextView= itemView.recycler_customer_address
        val customerProductName:TextView= itemView.recycler_product_name
        val customerProductPrize:TextView= itemView.recycler_product_prize
        val customerProductQuantity:TextView= itemView.recycler_product_quantity
    }

    override fun onBackPressed() {
    }

    private fun setDialog (){
        dialog = ACProgressFlower.Builder(this)
            .direction(ACProgressConstant.DIRECT_CLOCKWISE)
            .themeColor(Color.RED)
            .fadeColor(Color.BLACK)
            .build()
        dialog.show()
    }
}