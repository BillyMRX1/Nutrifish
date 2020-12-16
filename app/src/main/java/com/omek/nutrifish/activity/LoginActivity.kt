package com.omek.nutrifish.activity

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.omek.nutrifish.MainActivity
import com.omek.nutrifish.R

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var emailEdt: EditText
    private lateinit var passwordEdt: EditText
    private lateinit var btnSignup: Button
    private lateinit var btnLogin: ImageButton
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        emailEdt = findViewById(R.id.email_edit_text)
        passwordEdt = findViewById(R.id.password_edit_text)
        btnSignup = findViewById(R.id.signup_btn)
        btnLogin = findViewById(R.id.login_btn)
        firebaseAuth = FirebaseAuth.getInstance()
        btnSignup.setOnClickListener(this)
        btnLogin.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.signup_btn ->{
                val goSignup = Intent(this@LoginActivity, SignupActivity::class.java)
                startActivity(goSignup)
            }
            R.id.login_btn ->{
                val emailString = emailEdt.text.toString()
                val passwordString = passwordEdt.text.toString()
                when {
                    TextUtils.isEmpty(emailString) -> {
                        emailEdt.error = "Tolong Nama Diisi"
                    }
                    TextUtils.isEmpty(passwordString) -> {
                        passwordEdt.error = "Tolong Nama Diisi"
                    }
                    else -> {
                        firebaseAuth.signInWithEmailAndPassword(emailString, passwordString).addOnCompleteListener {
                            if (it.isSuccessful){
                                if (firebaseAuth.currentUser!!.isEmailVerified){
                                    Toast.makeText(this,"Login sukses", Toast.LENGTH_SHORT).show()
                                    val sharedPreference: SharedPreferences = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                                    val editor: SharedPreferences.Editor = sharedPreference.edit()
                                    editor.putString("Login","Yes")
                                    editor.apply()
                                    val goMain = Intent(this@LoginActivity, MainActivity::class.java)
                                    startActivity(goMain)
                                    finish()
                                }else{
                                    firebaseAuth.signOut()
                                    Toast.makeText(this,"Email belum diverifikasi", Toast.LENGTH_SHORT).show()
                                }
                            } else{
                                Toast.makeText(this, "Error! " + it.exception!!.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
    }
}