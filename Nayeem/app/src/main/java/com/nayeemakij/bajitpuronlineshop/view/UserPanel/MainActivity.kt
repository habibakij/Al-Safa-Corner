package com.nayeemakij.bajitpuronlineshop.view.UserPanel

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.Window
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.nayeemakij.bajitpuronlineshop.view.AdminPanel.AdminDashboard
import com.nayeemakij.bajitpuronlineshop.R
import com.nayeemakij.bajitpuronlineshop.view.UserPanel.Fragment.*
import com.nayeemakij.bajitpuronlineshop.view.UserPanel.Registration.UserLogIn
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.user_logout.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var mAuth: FirebaseAuth
    private var adminEmail: String? = "njnayeem7@gmail.com"
    companion object {
        val TAG: String = MainActivity::class.java.simpleName
    }
    private lateinit var adminCheck:FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()
        adminCheck= mAuth.currentUser!!
        Log.d("currentUser", adminCheck.email.toString())
        loadLibraryFragment()
        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.setTitleTextColor(resources.getColor(R.color.colorWhite))
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val fragment_container:FrameLayout = findViewById(R.id.fragment_container)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.itemId
        if (id == R.id.action_admin) {
            if (adminCheck.email.toString() == adminEmail) {
                val intent = Intent(this, AdminDashboard::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Sorry you'r not admin", Toast.LENGTH_LONG).show()
            }
        } else if (id == R.id.action_log_out) {
          alertDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val  foodFragment: Fragment
        val  libraryFragment: Fragment
        val  stationaryFragment: Fragment
        val  toyFragment: Fragment
        val  furnitureFragment: Fragment
        val id: Int = item.itemId
        if (id == R.id.nav_library) {
            title="Library"
            libraryFragment= LibraryFragment()
            val fragmentManager:FragmentManager= supportFragmentManager
            val fragmentTransaction: FragmentTransaction= fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_container, libraryFragment)
            fragmentTransaction.commit()
        } else if (id == R.id.nav_foods) {
            title= "Food's"
            foodFragment= FoodFragment()
            val fragmentManager:FragmentManager= supportFragmentManager
            val fragmentTransaction: FragmentTransaction= fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_container, foodFragment)
            fragmentTransaction.commit()
        } else if (id == R.id.nav_stationary){
            title= "Stationary"
            stationaryFragment= StationaryFragment()
            val fragmentManager:FragmentManager= supportFragmentManager
            val fragmentTransaction: FragmentTransaction= fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_container, stationaryFragment)
            fragmentTransaction.commit()
        } else if (id == R.id.nav_toy){
            title= "Toy's"
            toyFragment= ToyFragment()
            val fragmentManager:FragmentManager= supportFragmentManager
            val fragmentTransaction: FragmentTransaction= fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_container, toyFragment)
            fragmentTransaction.commit()
        } else if (id == R.id.nav_furniture){
            title= "Furniture"
            furnitureFragment= FurnitureFragment()
            val fragmentManager:FragmentManager= supportFragmentManager
            val fragmentTransaction: FragmentTransaction= fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_container, furnitureFragment)
            fragmentTransaction.commit()
        }
        else if (id == R.id.nav_share) {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            startActivity(Intent.createChooser(intent, "Share With"))
        } else if (id == R.id.nav_about_us){
            Toast.makeText(this, "Coming Soon", Toast.LENGTH_LONG).show()
        } else if (id == R.id.nav_rate_us){
            Toast.makeText(this, "Coming Soon", Toast.LENGTH_LONG).show()
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true;
    }

    private fun loadLibraryFragment () {
        val libraryFragment: Fragment
        libraryFragment= LibraryFragment()
        val fragmentManager:FragmentManager= supportFragmentManager
        val fragmentTransaction: FragmentTransaction= fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, libraryFragment)
        fragmentTransaction.commit()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun alertDialog(){
        val dialog = Dialog(this@MainActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.user_logout)
        window.setBackgroundDrawable(getDrawable(R.drawable.dialog_bacground))
        val animation: Animation = AnimationUtils.loadAnimation(applicationContext, R.anim.dialog_fade)
        //window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.setCancelable(false)
        dialog.setTitle("Title")
        dialog.img_dialog_logout.animation= animation
        dialog.btn_logout_yes.setOnClickListener(){
            val sharedPreferences = getSharedPreferences(
                "STORE_REGISTRATION_DATA",
                Context.MODE_PRIVATE
            )
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this@MainActivity, UserLogIn::class.java)
            startActivity(intent)
            finish()
        }
        dialog.btn_logout_cancel.setOnClickListener(){
            dialog.dismiss()
        }
        dialog.show()
    }
}