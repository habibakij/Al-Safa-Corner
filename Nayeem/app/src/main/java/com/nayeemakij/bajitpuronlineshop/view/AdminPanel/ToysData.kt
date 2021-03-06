package com.nayeemakij.bajitpuronlineshop.view.AdminPanel

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.RelativeLayout
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.nayeemakij.bajitpuronlineshop.R
import com.nayeemakij.bajitpuronlineshop.Model.ProductInfo
import com.nayeemakij.bajitpuronlineshop.view.UserPanel.MainActivity
import kotlinx.android.synthetic.main.activity_stationary_data.*
import kotlinx.android.synthetic.main.activity_toys_data.*
import kotlinx.android.synthetic.main.custom_toolbar.view.*
import java.io.IOException

class ToysData : AppCompatActivity() {
    private lateinit var appBar: RelativeLayout
    private lateinit var id:String
    private lateinit var databaseReference: DatabaseReference
    private lateinit var info: ProductInfo
    private var PICK_IMAGE_REQUEST= 11
    private lateinit var imagePathUri: Uri
    private lateinit var storageRef: StorageReference
    private lateinit var name: String
    private lateinit var prize: String
    private lateinit var size: String
    private lateinit var description: String
    private var checkClickImageView= 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_toys_data)

        appBar= findViewById(R.id.include_toys_toolbar)
        appBar.custom_toolbar_back.setOnClickListener(){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        appBar.custom_toolbar_title.text= getString(R.string.menu_toy)
        storageRef= FirebaseStorage.getInstance().reference
        databaseReference = FirebaseDatabase.getInstance().reference

        upload_product_image_4.setOnClickListener {
            checkClickImageView++
            chooseImage()
        }

        upload_toys_data.setOnClickListener {
            if (checkClickImageView>0) {
                uploadProductData()
            } else{
                Toast.makeText(this, "Choose Product Image", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun chooseImage(){
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    private fun getFileExtension(uri: Uri?): String? {
        val contentResolver = contentResolver
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri!!))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null){
            imagePathUri= data.data!!
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imagePathUri)
                upload_product_image_4.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun uploadProductData(){
        id = upload_product_id_4.text.toString()
        name = upload_product_name_4.text.toString()
        prize = upload_product_prize_4.text.toString()
        size = upload_product_size_4.text.toString()
        description = upload_product_description_4.text.toString()

        if (id.isEmpty()) {
            upload_product_id_4.error = "Enter Id"
            upload_product_id_4.requestFocus()
        } else if (name.isEmpty()) {
            upload_product_name_4.error = "Enter Name"
            upload_product_name_4.requestFocus()
        } else if (prize.isEmpty()) {
            upload_product_prize_4.error = "Enter Prize"
            upload_product_prize_4.requestFocus()
        } else if (size.isEmpty()) {
            upload_product_size_4.error = "Enter Size"
            upload_product_size_4.requestFocus()
        } else if (description.isEmpty()) {
            upload_product_description_4.error = "Enter Description"
            upload_product_description_4.requestFocus()
        } else if (description.length<10) {
            upload_product_description_4.error = "Description at least 10 char long"
            upload_product_description_4.requestFocus()
        } else {
            uploadDataFirebase()
        }
    }

    private fun uploadDataFirebase(){
        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Uploading...")
        progressDialog.setMessage("Please wait a few second for uploading.")
        progressDialog.show()
        val storageReference: StorageReference = storageRef.child("ToyImage").child(System.currentTimeMillis().toString() + "." + getFileExtension(imagePathUri))

        storageReference.putFile(imagePathUri).addOnSuccessListener { taskSnapshot ->
            val downloadUrl: Task<Uri> = taskSnapshot.storage.downloadUrl
            while (!downloadUrl.isSuccessful) { }
            val imageUri: Uri = downloadUrl.result!!
            Log.i("ImageUri", imageUri.toString())
            info = ProductInfo(imageUri.toString(), id, name, prize, size, description)
            databaseReference.child("ProductDetails").child("Toys").push().setValue(info)
                .addOnCompleteListener {
                    progressDialog.dismiss()
                    Toast.makeText(this, "upload successfully", Toast.LENGTH_LONG).show()
                    upload_product_id_4.setText("")
                    upload_product_name_4.setText("")
                    upload_product_prize_4.setText("")
                    upload_product_size_4.setText("")
                    upload_product_description_4.setText("")
                    checkClickImageView= 0
                    val intent= Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
        }.addOnFailureListener{exception ->
            progressDialog.dismiss()
            Log.i("Exception_is: ", exception.message.toString())
            Toast.makeText(this, "Error_is: "+exception.message.toString(),Toast.LENGTH_LONG).show()
        }
    }

    override fun onBackPressed() {

    }
}