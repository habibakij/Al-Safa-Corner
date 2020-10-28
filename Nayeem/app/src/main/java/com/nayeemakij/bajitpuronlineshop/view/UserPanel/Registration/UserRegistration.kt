package com.nayeemakij.bajitpuronlineshop.view.UserPanel.Registration

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cc.cloudist.acplibrary.ACProgressConstant
import cc.cloudist.acplibrary.ACProgressFlower
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.nayeemakij.bajitpuronlineshop.R
import com.nayeemakij.bajitpuronlineshop.view.UserPanel.MainActivity
import kotlinx.android.synthetic.main.activity_user_registration.*


class UserRegistration: AppCompatActivity() {
    companion object {
        val TAG = UserLogIn::class.java.simpleName as String
    }
    private var mAuth: FirebaseAuth? = null
    lateinit var dialog: ACProgressFlower

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_registration)

        setDialog()
        mAuth = FirebaseAuth.getInstance()
        btn_sign_up.setOnClickListener(){
            validity()
        }
        txt_already_account.setOnClickListener(){
            val intent= Intent(this, UserLogIn::class.java)
            startActivity(intent)
        }
    }

    private fun validity (){
        if (edt_sign_up_email.text.isEmpty()){
            edt_sign_up_email.error= "Enter Email"
            edt_sign_up_email.requestFocus()
            return
        } else if (!Patterns.EMAIL_ADDRESS.matcher(edt_sign_up_email.text).matches()){
            edt_sign_up_email.error= "Enter valid Email Address"
            edt_sign_up_email.requestFocus()
            return
        } else if (edt_sign_up_password.text.length<6){
            edt_sign_up_password.error= "At least 6 char long"
            edt_sign_up_password.requestFocus()
            return
        } else if (edt_sign_up_cnfirm_password.text.length<6){
            edt_sign_up_cnfirm_password.error= "At least 6 char long"
            edt_sign_up_cnfirm_password.requestFocus()
            return
        } else if (!edt_sign_up_password.text.toString().equals(edt_sign_up_cnfirm_password.text.toString())){
            edt_sign_up_cnfirm_password.error= "Confirm Password are not match"
            edt_sign_up_cnfirm_password.requestFocus()
            return
        } else{
            signUpFun()
        }
    }

    private fun signUpFun(){
        dialog.show()
        val email:String= edt_sign_up_email.text.toString()
        val password:String= edt_sign_up_password.text.toString()

        mAuth!!.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, OnCompleteListener<AuthResult?> { task ->
                if (task.isSuccessful) {
                    dialog.dismiss()
                    Log.d(TAG, "CreateUser:success")
                    val user: FirebaseUser?= mAuth!!.currentUser
                    Log.d(TAG, user.toString())
                    val intent= Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(this@UserRegistration, "CreateUser:success", Toast.LENGTH_SHORT).show()
                } else {
                    dialog.dismiss()
                    Log.w(TAG, "CreateUser:failure", task.exception)
                    Toast.makeText(this@UserRegistration, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun setDialog (){
        dialog = ACProgressFlower.Builder(this)
            .direction(ACProgressConstant.DIRECT_CLOCKWISE)
            .themeColor(Color.GREEN)
            .fadeColor(Color.WHITE)
            .build()
    }

    override fun onBackPressed() {
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth!!.currentUser
        if (currentUser!= null){
            val intent= Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

}