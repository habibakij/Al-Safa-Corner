package com.nayeemakij.bajitpuronlineshop.view.UserPanel.Fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cc.cloudist.acplibrary.ACProgressConstant
import cc.cloudist.acplibrary.ACProgressFlower
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.*
import com.nayeemakij.bajitpuronlineshop.Model.ProductInfo
import com.nayeemakij.bajitpuronlineshop.R
import com.nayeemakij.bajitpuronlineshop.view.UserPanel.ProductCard
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recycler_item.view.*


class FoodFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var databaseReference: DatabaseReference
    lateinit var animation:Animation
    lateinit var dialog:ACProgressFlower

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_food, container, false)

        setDialog()
        animation = AnimationUtils.loadAnimation(context, R.anim.right_enter)
        viewManager = LinearLayoutManager(context)
        recyclerView = root.findViewById<RecyclerView>(R.id.foods_recycler_view).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
        }
        recyclerView.animation= animation
        databaseReference = FirebaseDatabase.getInstance().reference.child("ProductDetails").child("Foods")
        showFoodData()

        return root
    }

    private fun showFoodData(){
        val options: FirebaseRecyclerOptions<ProductInfo> =
            FirebaseRecyclerOptions.Builder<ProductInfo>().setQuery(databaseReference, ProductInfo::class.java).build()

        val adapter = object : FirebaseRecyclerAdapter<ProductInfo, FoodViewHolder>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
                return FoodViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recycler_item, parent, false))
            }
            override fun onBindViewHolder(holder: FoodViewHolder, position: Int, model: ProductInfo) {
                val adapterReference:DatabaseReference = getRef(position).ref

                adapterReference.addValueEventListener(object : ValueEventListener{
                    override fun onCancelled(p0: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()){
                            dialog.dismiss()
                            holder.productName.text= snapshot.child("name").value.toString()
                            holder.productDescription.text= snapshot.child("description").value.toString()
                            holder.productPrize.text= snapshot.child("prize").value.toString()
                            holder.productSize.text= snapshot.child("size").value.toString()
                            val url:String= snapshot.child("image").value.toString()
                            Picasso.get()
                                .load(url)
                                .error(R.drawable.ic_baseline_image)
                                .into(holder.productImage)
                            holder.layout.setOnClickListener {
                                val intent= Intent(context, ProductCard::class.java)
                                intent.putExtra("passItemName", snapshot.child("name").value.toString())
                                Log.i("STORE_FOOD_DATA: ", snapshot.child("name").value.toString())
                                intent.putExtra("passItemPrize", snapshot.child("prize").value.toString())
                                Log.i("STORE_FOOD_DATA: ", snapshot.child("prize").value.toString())
                                intent.putExtra("passItemDescription", snapshot.child("description").value.toString())
                                Log.i("STORE_FOOD_DATA: ", snapshot.child("description").value.toString())
                                intent.putExtra("CATEGORY_NAME", "Foods")
                                startActivity(intent)
                            }
                        }else{
                            dialog.dismiss()
                            Toast.makeText(context, "Data not exists: $snapshot", Toast.LENGTH_LONG).show()
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

    class FoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val layout: LinearLayout = itemView.recylar_item_view
        val productImage: ImageView = itemView.item_product_image
        val productName: TextView = itemView.item_product_name
        val productSize: TextView = itemView.item_product_size
        val productPrize: TextView = itemView.item_product_prize
        val productDescription: TextView = itemView.item_product_description
    }

    private fun setDialog (){
        dialog = ACProgressFlower.Builder(context)
            .direction(ACProgressConstant.DIRECT_CLOCKWISE)
            .themeColor(Color.RED)
            .fadeColor(Color.BLACK)
            .build()
        dialog.show()
    }

}