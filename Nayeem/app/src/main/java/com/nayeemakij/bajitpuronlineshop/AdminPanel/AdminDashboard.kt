package com.nayeemakij.bajitpuronlineshop.AdminPanel

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.widget.GridLayout
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.nayeemakij.bajitpuronlineshop.Adapter.AdminDashbordAdapter
import com.nayeemakij.bajitpuronlineshop.R
import com.nayeemakij.bajitpuronlineshop.UserPanel.MainActivity
import com.nayeemakij.bajitpuronlineshop.UserPanel.Registration.UserLogIn
import kotlinx.android.synthetic.main.activity_admin_dashbord.*
class AdminDashboard : AppCompatActivity() {
    private var doubleBackToExitPressedOnce = false
    private var imageList = listOf<Int>(
        R.drawable.library_pic, R.drawable.food_pic, R.drawable.stationary_pic,
        R.drawable.toys_pic, R.drawable.furniture_pic
    )
    private var productList =
        mutableListOf<String>("Library", "Foods", "Stationary", "Toys", "Furniture")
    lateinit var adminAdapter: AdminDashbordAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashbord)

        title = "Admin Dashboard"
        admin_dashborad_recyclerview.layoutManager = GridLayoutManager(this, 2)
        admin_dashborad_recyclerview.hasFixedSize()
        adminAdapter = AdminDashbordAdapter(this, productList, imageList)
        admin_dashborad_recyclerview.adapter = adminAdapter
    }

    override fun onBackPressed() {
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.dashboard_menue, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.itemId
        if (id == R.id.action_order) {
            val intent = Intent(this, ShowCustomerOrder::class.java)
            startActivity(intent)
        } else if (id == R.id.action_product) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}