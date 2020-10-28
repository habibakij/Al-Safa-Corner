package com.nayeemakij.bajitpuronlineshop.view.AdminPanel

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.nayeemakij.bajitpuronlineshop.Model.ProductInfo
import com.nayeemakij.bajitpuronlineshop.R
import kotlinx.android.synthetic.main.activity_library_data.*
import kotlinx.android.synthetic.main.custom_toolbar.view.*
import java.io.IOException

class LibraryData : AppCompatActivity() {
    private lateinit var appBar: RelativeLayout
    private lateinit var id: String
    private lateinit var databaseReference: DatabaseReference
    private lateinit var info: ProductInfo
    private var PICK_IMAGE_REQUEST = 11
    private lateinit var imagePathUri: Uri
    private lateinit var storageRef: StorageReference
    private lateinit var name: String
    private lateinit var prize: String
    private lateinit var size: String
    private lateinit var description: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_library_data)

        appBar= findViewById(R.id.include_library_toolbar)
        appBar.custom_toolbar_back.setOnClickListener(){
            val intent = Intent(this, AdminDashboard::class.java)
            startActivity(intent)
            finish()
        }
        appBar.custom_toolbar_title.text= getString(R.string.menu_library)
        storageRef = FirebaseStorage.getInstance().reference
        databaseReference = FirebaseDatabase.getInstance().reference

        upload_library_data.setOnClickListener {
            uploadProductData()
        }
        upload_product_image_2.setOnClickListener {
            chooseImage()
        }
    }

    fun chooseImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    fun getFileExtension(uri: Uri?): String? {
        val contentResolver = contentResolver
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri!!))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            imagePathUri = data.data!!
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imagePathUri)
                upload_product_image_2.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun uploadProductData() {
        id = upload_product_id_2.text.toString()
        name = upload_product_name_2.text.toString()
        prize = upload_product_prize_2.text.toString()
        size = upload_product_size_2.text.toString()
        description = upload_product_description_2.text.toString()

        if (id.isEmpty()) {
            upload_product_id_2.error = "Enter Id"
            upload_product_id_2.requestFocus()
        } else if (name.isEmpty()) {
            upload_product_name_2.error = "Enter Name"
            upload_product_name_2.requestFocus()
            return
        } else if (prize.isEmpty()) {
            upload_product_prize_2.error = "Enter Prize"
            upload_product_prize_2.requestFocus()
            return
        } else if (size.isEmpty()) {
            upload_product_size_2.error = "Enter Size"
            upload_product_size_2.requestFocus()
            return
        } else if (description.isEmpty()) {
            upload_product_description_2.error = "Enter Description"
            upload_product_description_2.requestFocus()
            return
        } else {
            uploadDataFirebase()
        }
    }

    private fun uploadDataFirebase() {
        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Uploading...")
        progressDialog.show()

        val storageReference: StorageReference = storageRef.child("LibraryImage")
            .child(System.currentTimeMillis().toString() + "." + getFileExtension(imagePathUri))

        storageReference.putFile(imagePathUri).addOnSuccessListener { taskSnapshot ->
            val downloadUrl: Task<Uri> = taskSnapshot.storage.downloadUrl
            while (!downloadUrl.isSuccessful) {
            }
            val imageUri: Uri = downloadUrl.result!!
            Log.i("ImageUri", imageUri.toString())

            info = ProductInfo(imageUri.toString(), id, name, prize, size, description)

            databaseReference.child("ProductDetails").child("Library").push().setValue(info)
                .addOnCompleteListener {
                    progressDialog.dismiss()
                    Toast.makeText(this, "upload successfully", Toast.LENGTH_LONG).show()
                    upload_product_id_2.setText("")
                    upload_product_name_2.setText("")
                    upload_product_prize_2.setText("")
                    upload_product_size_2.setText("")
                    upload_product_description_2.setText("")
                    //upload_product_image.setImageResource(R.drawable.ic_baseline_image)
                }
        }.addOnFailureListener{exception ->
            Log.i("Exception_is: ", exception.message.toString())
            Toast.makeText(this, "Error_is: "+exception.message.toString(),Toast.LENGTH_LONG).show()
        }
    }

    override fun onBackPressed() {

    }

}