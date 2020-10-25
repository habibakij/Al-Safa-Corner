package com.nayeemakij.bajitpuronlineshop.UserPanel

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.nayeemakij.bajitpuronlineshop.R
import kotlinx.android.synthetic.main.custom_toolbar.view.*

class Profile : AppCompatActivity() {
    private lateinit var appBar: RelativeLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        appBar= findViewById(R.id.include_profile_toolbar)
        appBar.custom_toolbar_back.setOnClickListener(){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        appBar.custom_toolbar_title.text= "Profile"
    }
}