package com.omek.nutrifish.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.omek.nutrifish.R
import com.omek.nutrifish.adapter.FishAdapter

class ListActivity2 : AppCompatActivity() {
    private lateinit var rvLaut: RecyclerView
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var btnBack: ImageButton
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list2)

        rvLaut = findViewById(R.id.rv_fish)
        progressBar = findViewById(R.id.progress_bar)
        btnBack = findViewById(R.id.back_btn)
        firebaseFirestore = FirebaseFirestore.getInstance()
        showLoading(true)
        val docRef = firebaseFirestore.collection("Counter").document("total")
        docRef.get().addOnSuccessListener {
            if (it != null){
                val totalFish = it.get("tawar") as Long
                val itemCount = totalFish.toInt()
                rvLaut.adapter = FishAdapter("tawar", itemCount)
                showLoading(false)
            }else{
                Log.d("ListActivity", "Document not found")
            }
        }.addOnFailureListener {
            Log.d("ListActivity","error: ",it)
        }
        rvLaut.layoutManager = LinearLayoutManager(this)
        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun showLoading(state:Boolean) {
        if (state)
        {
            progressBar.visibility = View.VISIBLE
        }
        else
        {
            progressBar.visibility = View.GONE
        }
    }
}