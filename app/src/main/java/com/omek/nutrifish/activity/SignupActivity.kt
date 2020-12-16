package com.omek.nutrifish.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.omek.nutrifish.R

class SignupActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var nameEdt: EditText
    private lateinit var emailEdt: EditText
    private lateinit var passwordEdt: EditText
    private lateinit var signupBtn: ImageButton
    private lateinit var loginBtn: Button
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        nameEdt = findViewById(R.id.name_edit_text)
        emailEdt = findViewById(R.id.email_edit_text)
        passwordEdt = findViewById(R.id.password_edit_text)
        signupBtn = findViewById(R.id.signup_btn)
        loginBtn = findViewById(R.id.login_btn)
        firebaseAuth = FirebaseAuth.getInstance()

        signupBtn.setOnClickListener(this)
        loginBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.signup_btn ->{
                val nameString = nameEdt.text.toString()
                val emailString = emailEdt.text.toString()
                val passwordString = passwordEdt.text.toString()
                when {
                    TextUtils.isEmpty(nameString) -> {
                        nameEdt.error = "Tolong Nama Diisi"
                    }
                    TextUtils.isEmpty(emailString) -> {
                        emailEdt.error = "Tolong Nama Diisi"
                    }
                    TextUtils.isEmpty(passwordString) -> {
                        passwordEdt.error = "Tolong Nama Diisi"
                    }
                    else -> {
                        firebaseAuth.createUserWithEmailAndPassword(emailString, passwordString).addOnCompleteListener {
                            if (it.isSuccessful){
                                val currentUser = firebaseAuth.currentUser
                                val profileUpdate = UserProfileChangeRequest.Builder().setDisplayName(nameString).build()
                                currentUser!!.updateProfile(profileUpdate).addOnCompleteListener {
                                    if (it.isSuccessful){
                                        Log.d("SigunupActivity","Name updated successfully")
                                    }
                                }
                                currentUser.sendEmailVerification().addOnSuccessListener {
                                    Toast.makeText(this, "Email verifikasi telah dikirim", Toast.LENGTH_SHORT).show()
                                }.addOnFailureListener {
                                    Log.d("SignupActivity","onFailure: Email not sent")
                                }

                                val userId = firebaseAuth.currentUser!!.uid
                                val documentReference: DocumentReference = FirebaseFirestore.getInstance().collection("Users").document(userId)
                                val user: MutableMap<String, Any> = HashMap()
                                user["name"] = nameString
                                user["email"] = emailString
                                documentReference.set(user).addOnCompleteListener {
                                    Log.d("SignupActivity","onSuccess: User created $userId")
                                }.addOnFailureListener { e ->
                                    Log.d("SignupActivity", e.toString())
                                }

                                firebaseAuth.signOut()
                                val goLogin = Intent(this, LoginActivity::class.java)
                                startActivity(goLogin)
                                finish()
                            }else{
                                Toast.makeText(this,"Error! " + it.exception!!.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
            R.id.login_btn ->{
                val goLogin = Intent(this, LoginActivity::class.java)
                startActivity(goLogin)
                finish()
            }
        }
    }
}