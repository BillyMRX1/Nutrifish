package com.omek.nutrifish.activity

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.omek.nutrifish.R
import de.hdodenhof.circleimageview.CircleImageView

class ProfileActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var tvName: TextView
    private lateinit var tvEmail: TextView
    private lateinit var profilePic: CircleImageView
    private lateinit var btnLogout: Button
    private lateinit var btnBack: ImageButton
    private lateinit var btnChangepic: Button
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseStorage: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        tvName = findViewById(R.id.name)
        tvEmail = findViewById(R.id.email)
        btnLogout = findViewById(R.id.logout_btn)
        btnChangepic = findViewById(R.id.changepic_btn)
        profilePic = findViewById(R.id.profile_pic)
        btnBack = findViewById(R.id.back_btn)
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseStorage = FirebaseStorage.getInstance().reference

        if (firebaseAuth.currentUser?.photoUrl != null){
            Glide.with(this).load(firebaseAuth.currentUser!!.photoUrl).into(profilePic)
        }

        tvName.text = firebaseAuth.currentUser!!.displayName
        tvEmail.text = firebaseAuth.currentUser!!.email
        btnChangepic.setOnClickListener(this)
        btnLogout.setOnClickListener(this)
        btnBack.setOnClickListener(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100) {
            if (resultCode == Activity.RESULT_OK) {
                val imageUri: Uri? = data?.data
                if (imageUri != null) {
                    uploadImageToFirebase(imageUri)
                }
            }
        }
    }

    private fun uploadImageToFirebase(imageUri: Uri) {
        val fileRef: StorageReference = firebaseStorage.child("Users/" + firebaseAuth.currentUser!!.uid + "/profile.jpg")
        fileRef.putFile(imageUri).addOnSuccessListener {
            fileRef.downloadUrl.addOnSuccessListener {
                val profileUpdate = UserProfileChangeRequest.Builder().setPhotoUri(it).build()
                firebaseAuth.currentUser!!.updateProfile(profileUpdate).addOnCompleteListener {
                    if (it.isSuccessful){
                        Log.d("ProfileActivity", "ProfilePic updated successfully")
                    }
                }
                Glide.with(this).load(it).into(profilePic)
                Toast.makeText(this, "Profile Pic berhasil diganti", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Gagal upload gambar", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.logout_btn -> {
                firebaseAuth.signOut()
                val sharedPreference: SharedPreferences = getSharedPreferences(
                    "PREFERENCE",
                    MODE_PRIVATE
                )
                val editor: SharedPreferences.Editor = sharedPreference.edit()
                editor.clear()
                editor.apply()
                val goLogin = Intent(this, LoginActivity::class.java)
                startActivity(goLogin)
                finishAffinity()
            }
            R.id.changepic_btn -> {
                val openGallery = Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )
                startActivityForResult(openGallery, 100)
            }
            R.id.back_btn -> {
                onBackPressed()
            }
        }
    }
}