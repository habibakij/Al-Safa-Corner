package com.nayeemakij.bajitpuronlineshop.UserPanel

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.nayeemakij.bajitpuronlineshop.Model.CustomerInfo
import com.nayeemakij.bajitpuronlineshop.R
import kotlinx.android.synthetic.main.activity_order_review.*
import kotlinx.android.synthetic.main.custom_toolbar.view.*

class OrderReview : AppCompatActivity() {
    private lateinit var appBar: RelativeLayout
    private var deliveryPrize:Int= 50
    private lateinit var mAuth: FirebaseAuth
    private lateinit var reference: DatabaseReference
    private var list:MutableList<CustomerInfo> = mutableListOf()
    private var count= 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_review)

        appBar= findViewById(R.id.include_order_review_toolbar)
        appBar.custom_toolbar_back.setOnClickListener(){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        appBar.custom_toolbar_title.text= "Order Review"
        reference= FirebaseDatabase.getInstance().reference
        card_delivery_info.visibility= View.INVISIBLE
        btn_submit_delivery_address.setOnClickListener {
            count++
            if (edt_review_delivery_name.text.isEmpty()){
                edt_review_delivery_name.error= "Name"
            } else if(edt_review_delivery_phone.text.isEmpty()){
                edt_review_delivery_phone.error= "Enter Phone Number"
            } else if (edt_review_delivery_address.text.isEmpty()){
                edt_review_delivery_address.error= "Delivery Address"
            } else{
                card_delivery_info.visibility= View.VISIBLE
                review_delivery_name.text= edt_review_delivery_name.text.toString()
                review_delivery_address.text= edt_review_delivery_address.text.toString()
                review_delivery_phone.text= edt_review_delivery_phone.text.toString()
                edt_review_delivery_name.setText("")
                edt_review_delivery_phone.setText("")
                edt_review_delivery_address.setText("")
            }
        }

        val name:String?= intent.getStringExtra("reviewProductName")
        val prize:String?= intent.getStringExtra("reviewProductPrize")
        val quantity:String?= intent.getStringExtra("reviewProductQuantity")
        val prizeConvert:Int?= prize?.toInt()?.plus(deliveryPrize)
        review_product_name.text= name
        review_product_prize.text= prize
        review_product_total_prize.text= prizeConvert.toString()
        btn_delivery_confirm.setOnClickListener {
            if (count > 0) {
                val dName = review_delivery_name.text.toString()
                val dPhone = review_delivery_phone.text.toString()
                val dAddress = review_delivery_address.text.toString()
                val pName = review_product_name.text.toString()
                val pPrize = review_product_prize.text.toString()
                val pQuantity = quantity.toString()
                val customerInfo = CustomerInfo(dName, dPhone, dAddress, pName, pPrize, pQuantity)
                reference.child("ProductDetails").child("CustomerOrder").push().setValue(customerInfo)
                    .addOnCompleteListener {
                        Toast.makeText(this, "Your record successfully", Toast.LENGTH_LONG).show()
                    }
                val intent = Intent(this, OrderConfirm::class.java)
                startActivity(intent)
            } else{
                Toast.makeText(this, "Enter Delivery Address", Toast.LENGTH_LONG).show()
            }
        }
    }
}