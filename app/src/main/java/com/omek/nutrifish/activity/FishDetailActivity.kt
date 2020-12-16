package com.omek.nutrifish.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.omek.nutrifish.R

class FishDetailActivity : AppCompatActivity() {
    private lateinit var tvNama: TextView
    private lateinit var tvJenis: TextView
    private lateinit var tvDetail: TextView
    private lateinit var tvGizi: TextView
    private lateinit var fishPic: ImageView
    private lateinit var firebaseStorage: StorageReference

    companion object {
        const val EXTRA_NAMA = "extra_nama"
        const val EXTRA_DETAIL = "extra_detail"
        const val EXTRA_GIZI = "extra_gizi"
        const val EXTRA_JENIS = "extra_jenis"
        const val EXTRA_IMG = "extra_img"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fish_detail)

        tvNama = findViewById(R.id.nama)
        tvJenis = findViewById(R.id.jenis)
        tvDetail = findViewById(R.id.detail)
        tvGizi = findViewById(R.id.gizi)
        fishPic = findViewById(R.id.fish_pic)
        firebaseStorage = FirebaseStorage.getInstance().reference

        val fishName = "Ikan "+intent.getStringExtra(EXTRA_NAMA)
        tvNama.text = fishName
        tvJenis.text = intent.getStringExtra(EXTRA_JENIS)
        tvDetail.text = intent.getStringExtra(EXTRA_DETAIL)
        tvGizi.text = intent.getStringExtra(EXTRA_GIZI)
        val imgPath = intent.getStringExtra(EXTRA_IMG)
        val imgRef: StorageReference = imgPath.let { firebaseStorage.child(it!!) }

        imgRef.downloadUrl.addOnSuccessListener {
            Glide.with(this).load(it).into(fishPic)
        }
    }
}