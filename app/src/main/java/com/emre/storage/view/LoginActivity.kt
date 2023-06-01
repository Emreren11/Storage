package com.emre.storage.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.emre.storage.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = Firebase.auth

        val currentUser = auth.currentUser
        currentUser?.let {
            val intentToMain = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intentToMain)
            finish()
        }

    }

    fun signUp(view: View) {

        val email = binding.emailText.text.toString()
        val pass = binding.passText.text.toString()

        if (email.isNotEmpty() && pass.isNotEmpty()) {
            auth.createUserWithEmailAndPassword(email, pass).addOnSuccessListener {

                val intentToMain = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intentToMain)
                finish()
            }.addOnFailureListener {
                Toast.makeText(this@LoginActivity, it.localizedMessage, Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(this@LoginActivity, "Enter email and password!", Toast.LENGTH_LONG).show()
        }
    }

    fun signIn(view: View) {
        val email = binding.emailText.text.toString()
        val pass = binding.passText.text.toString()

        if (email.isNotEmpty() && pass.isNotEmpty()) {
            auth.signInWithEmailAndPassword(email, pass).addOnSuccessListener {

                val intentToMain = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intentToMain)
                finish()
            }.addOnFailureListener {
                Toast.makeText(this@LoginActivity, it.localizedMessage, Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(this@LoginActivity, "Enter email and password!", Toast.LENGTH_LONG).show()
        }

    }

}