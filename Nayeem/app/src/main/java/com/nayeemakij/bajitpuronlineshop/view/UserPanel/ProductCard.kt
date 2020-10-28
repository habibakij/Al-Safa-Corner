package com.nayeemakij.bajitpuronlineshop.view.UserPanel

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cc.cloudist.acplibrary.ACProgressConstant
import cc.cloudist.acplibrary.ACProgressPie
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.*
import com.nayeemakij.bajitpuronlineshop.Model.ProductInfo
import com.nayeemakij.bajitpuronlineshop.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_product_card.*
import kotlinx.android.synthetic.main.custom_toolbar.view.*
import kotlinx.android.synthetic.main.related_recycler_item.view.*


class ProductCard : AppCompatActivity() {
    private lateinit var appBar: RelativeLayout
    private var count:Int= 1
    private lateinit var recyclerView: RecyclerView
    private lateinit var databaseReference: DatabaseReference
    lateinit var dialog: ACProgressPie

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_card)

        appBar= findViewById(R.id.include_product_card_toolbar)
        appBar.custom_toolbar_back.setOnClickListener(){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        appBar.custom_toolbar_title.text= "Product Details"
        val getIntentParentCategory:String?= intent.getStringExtra("CATEGORY_NAME")
        databaseReference = FirebaseDatabase.getInstance().reference.child("ProductDetails").child(getIntentParentCategory!!)
        recyclerView= findViewById(R.id.recycler_related_item)
        val horizontalLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = horizontalLayoutManager

        setDialog()
        val getItemName:String?= intent.getStringExtra("passItemName")
        Log.i("passItemName: ", getItemName.toString())
        val getItemPrize:String?= intent.getStringExtra("passItemPrize")
        Log.i("passItemPrize: ", getItemPrize.toString())
        val getItemDescription:String?= intent.getStringExtra("passItemDescription")
        Log.i("passItemDescription: ", getItemDescription.toString())
        val getItemImage:String?= intent.getStringExtra("passImageUri")
        Log.i("passItemImage: ", getItemImage.toString())

        card_product_name.text= getItemName.toString()
        card_product_prize.text= getItemPrize.toString()
        card_product_description.text= getItemDescription.toString()
        Picasso.get().load(getItemImage).into(card_product_image)
        val setPrize:String= card_product_prize.text.toString()
        card_product_item_increment.setOnClickListener {
            count++
            card_product_quantity.text= count.toString()
            val calculatePrize:Int= card_product_prize.text.toString().toInt()+setPrize.toInt()
            card_product_prize.text= calculatePrize.toString()
        }
        card_product_item_decrement.setOnClickListener {
            if (1<count){
                count--
                card_product_quantity.text= count.toString()
                val calculatePrize:Int= card_product_prize.text.toString().toInt()-setPrize.toInt()
                card_product_prize.text= calculatePrize.toString()
            }
        }
        card_product_next.setOnClickListener {
            val intent= Intent(this, OrderReview::class.java)
            intent.putExtra("reviewProductName", card_product_name.text.toString())
            intent.putExtra("reviewProductPrize", card_product_prize.text.toString())
            intent.putExtra("reviewProductQuantity", card_product_quantity.text.toString())
            startActivity(intent)
        }
        showRecyclerRelatedData()
    }

    private fun showRecyclerRelatedData(){
        val options: FirebaseRecyclerOptions<ProductInfo> =
            FirebaseRecyclerOptions.Builder<ProductInfo>().setQuery(databaseReference, ProductInfo::class.java).build()

        val adapter = object : FirebaseRecyclerAdapter<ProductInfo, ProductCardViewHolder>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductCardViewHolder {
                return ProductCardViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.related_recycler_item, parent, false))
            }
            override fun onBindViewHolder(holder: ProductCardViewHolder, position: Int, model: ProductInfo) {
                val adapterReference:DatabaseReference = getRef(position).ref

                adapterReference.addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()){
                            dialog.dismiss()
                            holder.relatedItemName.text= snapshot.child("name").value.toString()
                            holder.relatedItemPrice.text= snapshot.child("prize").value.toString()
                            val url:String= snapshot.child("image").value.toString()
                            Picasso.get()
                                .load(url)
                                .error(R.drawable.ic_baseline_image)
                                .into(holder.relatedItemImage)

                            holder.relatedItemLayout.setOnClickListener {
                                Toast.makeText(this@ProductCard, "coming soon", Toast.LENGTH_LONG).show()
                            }
                        }else{
                            dialog.dismiss()
                            Toast.makeText(this@ProductCard, "Data not exists: $snapshot", Toast.LENGTH_LONG).show()
                        }
                    }
                })
            }

            override fun onDataChanged() {
                dialog.dismiss()
                // If there are no chat messages, show a view that invites the user to add a message.
                //mEmptyListMessage.setVisibility(if (itemCount == 0) View.VISIBLE else View.GONE)
            }
        }
        recyclerView.adapter= adapter
        adapter.startListening()
    }

    class ProductCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val relatedItemLayout: LinearLayout = itemView.layout_related_item
        val relatedItemImage: ImageView = itemView.related_item_image
        val relatedItemName: TextView = itemView.related_item_name
        val relatedItemPrice: TextView = itemView.related_item_price
    }

    private fun setDialog (){
        dialog = ACProgressPie.Builder(this)
            .ringColor(Color.WHITE)
            .pieColor(Color.WHITE)
            .updateType(ACProgressConstant.PIE_AUTO_UPDATE)
            .build()
    }
}