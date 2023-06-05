package com.emre.storage.view

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.Toast
import com.emre.storage.R
import com.emre.storage.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private var email = ""
    private var pass = ""
    private var isVisible = false
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = Firebase.auth
        firestore = Firebase.firestore
        sharedPreferences = getSharedPreferences("com.emre.storage.view", MODE_PRIVATE)



        val currentUser = auth.currentUser
        currentUser?.let {
            val language = sharedPreferences.getString("language", "")
            val intentToMain = Intent(this@LoginActivity, MainActivity::class.java)
            intentToMain.putExtra("language", language)
            startActivity(intentToMain)
            finish()
        }

        val intentFromMain = intent
        val emailFromSingOut = intentFromMain.getStringExtra("signOut")

        emailFromSingOut?.let {
            binding.emailText.setText(it)
        }

    }

    fun signUp(view: View) {

        email = binding.emailText.text.toString()
        pass = binding.passText.text.toString()
        val radioTr = binding.radioTurkish.isChecked
        val language: String
        if (radioTr){
            language = "Türkçe"
        } else {
            language = "English"
        }

        if (email.isNotEmpty() && pass.isNotEmpty()) {
            auth.createUserWithEmailAndPassword(email, pass).addOnSuccessListener {

                val user = hashMapOf(
                    "language" to language
                )
                firestore.collection(email).document("storage").set(user)
                firestore.collection(email).document("products").set(user)

                sharedPreferences.edit().putString("language", language).apply()

                val intentToMain = Intent(this@LoginActivity, MainActivity::class.java)
                intentToMain.putExtra("language", language)
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
        email = binding.emailText.text.toString()
        pass = binding.passText.text.toString()
        val radioTr = binding.radioTurkish.isChecked
        val language: String
        if (radioTr){
            language = "Türkçe"
        } else {
            language = "English"
        }

        if (email.isNotEmpty() && pass.isNotEmpty()) {
            auth.signInWithEmailAndPassword(email, pass).addOnSuccessListener {

                val user = hashMapOf(
                    "language" to language
                )
                firestore.collection(email).document("storage").set(user)
                firestore.collection(email).document("products").set(user)

                firestore.collection(email).document("storage").get().addOnSuccessListener {

                    val langFromFirebase = it.get("language").toString()
                    sharedPreferences.edit().putString("language", langFromFirebase).apply()

                    val intentToMain = Intent(this@LoginActivity, MainActivity::class.java)
                    intentToMain.putExtra("language", langFromFirebase)
                    startActivity(intentToMain)
                    finish()
                }


            }.addOnFailureListener {
                Toast.makeText(this@LoginActivity, it.localizedMessage, Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(this@LoginActivity, "Enter email and password!", Toast.LENGTH_LONG).show()
        }

    }

    fun showPass(view: View) {

        if (!isVisible) {
            binding.passText.inputType = InputType.TYPE_CLASS_TEXT
            binding.imageView.setImageResource(R.drawable.hide1)
            isVisible = true
        } else {
            binding.passText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            binding.imageView.setImageResource(R.drawable.show)
            isVisible = false
        }
    }

}