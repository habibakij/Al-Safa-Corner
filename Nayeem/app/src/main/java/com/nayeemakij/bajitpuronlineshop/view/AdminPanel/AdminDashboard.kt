package com.nayeemakij.bajitpuronlineshop.view.AdminPanel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.GridLayoutManager
import com.nayeemakij.bajitpuronlineshop.vewmodel.Adapter.AdminDashbordAdapter
import com.nayeemakij.bajitpuronlineshop.R
import com.nayeemakij.bajitpuronlineshop.view.UserPanel.MainActivity
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