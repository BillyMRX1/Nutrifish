package com.omek.nutrifish.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.omek.nutrifish.R
import com.omek.nutrifish.activity.FishDetailActivity
import com.omek.nutrifish.activity.FishDetailActivity.Companion.EXTRA_DETAIL
import com.omek.nutrifish.activity.FishDetailActivity.Companion.EXTRA_GIZI
import com.omek.nutrifish.activity.FishDetailActivity.Companion.EXTRA_IMG
import com.omek.nutrifish.activity.FishDetailActivity.Companion.EXTRA_JENIS
import com.omek.nutrifish.activity.FishDetailActivity.Companion.EXTRA_NAMA

class FishAdapter(private val jenis: String, private val itemCount: Int): RecyclerView.Adapter<FishAdapter.ViewHolder>(){
    private lateinit var firebaseStorage: StorageReference
    private lateinit var firebaseFirestore: FirebaseFirestore

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context).inflate(R.layout.item_cardview_fish, parent, false)
        firebaseFirestore = FirebaseFirestore.getInstance()
        firebaseStorage = FirebaseStorage.getInstance().reference
        return ViewHolder(layoutInflater)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.apply {
            val fishPic = findViewById<ImageView>(R.id.fish_pic)
            val fishName = findViewById<TextView>(R.id.fish_name)
            val fishDetail = findViewById<TextView>(R.id.description)
            val positionString = position.toString()
            val documentId = jenis+"_"+positionString
            val storageId = jenis+"_"+positionString+".jpg"
            val docRef = firebaseFirestore.collection("Laut").document(documentId)
            docRef.get().addOnSuccessListener {
                if (it != null){
                    val textFishName = it.get("nama") as String
                    val textDetail = it.get("detail") as String
                    val textGizi = it.get("gizi") as String
                    val textJenis = it.get("jenis") as String
                    fishName.text = textFishName
                    fishDetail.text = textDetail
                    val imgPath = "Fish/$jenis/$storageId"
                    val imgRef: StorageReference = firebaseStorage.child(imgPath)

                    imgRef.downloadUrl.addOnSuccessListener {
                        Glide.with(this).load(it).into(fishPic)
                        val itemFish = findViewById<CardView>(R.id.item_fish)
                        itemFish.setOnClickListener {
                            val goDetail = Intent(context, FishDetailActivity::class.java)
                            goDetail.putExtra(EXTRA_NAMA, textFishName)
                            goDetail.putExtra(EXTRA_DETAIL, textDetail)
                            goDetail.putExtra(EXTRA_GIZI, textGizi)
                            goDetail.putExtra(EXTRA_JENIS, textJenis)
                            goDetail.putExtra(EXTRA_IMG, imgPath)
                            context.startActivity(goDetail)
                        }
                    }
                } else{
                    Log.d("FishAdapter","Document not found")
                }
            }.addOnFailureListener {
                Log.d("FishAdapter","error: ",it)
            }
        }
    }

    override fun getItemCount(): Int {
        return itemCount
    }

    inner class ViewHolder(items: View): RecyclerView.ViewHolder(items)
}