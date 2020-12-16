package com.omek.nutrifish

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.omek.nutrifish.activity.ListActivity
import com.omek.nutrifish.activity.ListActivity2
import com.omek.nutrifish.activity.ProfileActivity
import de.hdodenhof.circleimageview.CircleImageView

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var tvName: TextView
    private lateinit var profilePic: CircleImageView
    private lateinit var btnFishLaut: ImageButton
    private lateinit var btnFishTawar: ImageButton
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseStorage: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvName = findViewById(R.id.name)
        profilePic = findViewById(R.id.profile_pic)
        btnFishLaut = findViewById(R.id.fishlaut_btn)
        btnFishTawar = findViewById(R.id.fishtawar_btn)
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseStorage = FirebaseStorage.getInstance().reference

        if (firebaseAuth.currentUser?.photoUrl != null){
            Glide.with(this).load(firebaseAuth.currentUser!!.photoUrl).into(profilePic)
        }

        tvName.text = firebaseAuth.currentUser!!.displayName
        profilePic.setOnClickListener(this)
        btnFishLaut.setOnClickListener(this)
        btnFishTawar.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.profile_pic -> {
                val goProf = Intent(this, ProfileActivity::class.java)
                startActivity(goProf)
            }
            R.id.fishlaut_btn -> {
                val goFish = Intent(this, ListActivity::class.java)
                startActivity(goFish)
            }
            R.id.fishtawar_btn -> {
                val goFish = Intent(this, ListActivity2::class.java)
                startActivity(goFish)
            }
        }
    }

    override fun onRestart() {
        super.onRestart()
        finish()
        startActivity(intent)
    }
}