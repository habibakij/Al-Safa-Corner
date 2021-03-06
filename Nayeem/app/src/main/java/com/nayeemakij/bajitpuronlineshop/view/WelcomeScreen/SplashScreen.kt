package com.nayeemakij.bajitpuronlineshop.view.WelcomeScreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.nayeemakij.bajitpuronlineshop.R
import com.nayeemakij.bajitpuronlineshop.view.AdminPanel.AdminDashboard
import com.nayeemakij.bajitpuronlineshop.view.UserPanel.MainActivity
import com.nayeemakij.bajitpuronlineshop.view.UserPanel.Registration.UserLogIn
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreen : AppCompatActivity() {
    companion object {
        val TAG = UserLogIn::class.java.simpleName as String
    }
    private lateinit var mAuth: FirebaseAuth
    private lateinit var animationTop: Animation
    private lateinit var animationBottom: Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        mAuth= FirebaseAuth.getInstance()
        val user = mAuth.currentUser
        animationTop= AnimationUtils.loadAnimation(this, R.anim.top)
        animationBottom= AnimationUtils.loadAnimation(this, R.anim.bottom)

        splash_imageview.animation= animationTop
        textview_1.animation= animationBottom
        textview_2.animation= animationBottom

        val splashTime= 2000
        Handler().postDelayed({

            if (user==null){
                Log.d(TAG, "Email: $user")
                val intent = Intent(this, UserLogIn::class.java)
                startActivity(intent)
                finish()
            } else {
                Log.d(TAG, "Email: $user")
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, splashTime.toLong())
    }
}