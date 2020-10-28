package com.nayeemakij.bajitpuronlineshop.view.UserPanel.Registration

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cc.cloudist.acplibrary.ACProgressConstant
import cc.cloudist.acplibrary.ACProgressFlower
import com.google.firebase.auth.FirebaseAuth
import com.nayeemakij.bajitpuronlineshop.R
import com.nayeemakij.bajitpuronlineshop.view.UserPanel.MainActivity
import kotlinx.android.synthetic.main.activity_user_log_in.*

class UserLogIn : AppCompatActivity() {
    companion object {
        val TAG = UserLogIn::class.java.simpleName as String
    }
    var doubleBackToExitPressedOnce = false
    private var mAuth: FirebaseAuth? = null
    lateinit var dialog: ACProgressFlower

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_log_in)

        setDialog()
        mAuth = FirebaseAuth.getInstance()
        btn_login.setOnClickListener(){
            validation()
        }
        txt_no_account.setOnClickListener(){
            val intent= Intent(this, UserRegistration::class.java)
            startActivity(intent)
        }
    }

    private fun validation(){
        if (edt_email.text.isEmpty()){
            edt_email.error= "Enter Email"
        } else if (edt_password.text.length<6){
            edt_password.error= "At least 6 char long"
        } else if (!Patterns.EMAIL_ADDRESS.matcher(edt_email.text).matches()){
            edt_email.error= "Enter valid Email Address"
        } else{
            loginFun()
        }
    }

    private fun loginFun(){
        dialog.show()
        val email:String= edt_email.text.toString()
        val password:String= edt_password.text.toString()

        mAuth!!.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    dialog.dismiss()
                    Log.d(TAG, "signInWithEmail:success")
                    val user = mAuth!!.currentUser
                    Log.d(TAG, "signInWithEmail: " + user)
                    val intent= Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    dialog.dismiss()
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(this@UserLogIn, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun setDialog (){
        dialog = ACProgressFlower.Builder(this)
            .direction(ACProgressConstant.DIRECT_CLOCKWISE)
            .themeColor(Color.GREEN)
            .fadeColor(Color.WHITE)
            .build()
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }
        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Hit Back again for Exit", Toast.LENGTH_SHORT).show()
        Handler().postDelayed(Runnable {
            doubleBackToExitPressedOnce = false
        }, 2000)
    }
}