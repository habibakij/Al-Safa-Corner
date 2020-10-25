package com.nayeemakij.bajitpuronlineshop.UserPanel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RelativeLayout
import com.nayeemakij.bajitpuronlineshop.R
import kotlinx.android.synthetic.main.activity_order_confirm.*
import kotlinx.android.synthetic.main.custom_toolbar.view.*

class OrderConfirm : AppCompatActivity() {
    private lateinit var appBar: RelativeLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_confirm)

        appBar= findViewById(R.id.include_order_confirm_toolbar)
        appBar.custom_toolbar_back.setOnClickListener(){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        appBar.custom_toolbar_title.text= "Order Confirm"
        confirm_go_to_home.setOnClickListener {
            val intent= Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}