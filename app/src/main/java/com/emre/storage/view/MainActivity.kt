package com.emre.storage.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.emre.storage.R
import com.emre.storage.databinding.ActivityMainBinding
import com.emre.storage.fragment.HomeFragment
import com.emre.storage.fragment.HomeFragmentDirections
import com.emre.storage.fragment.StorageFragment
import com.emre.storage.fragment.StorageFragmentDirections
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = Firebase.auth
        bottomNav = binding.bottomNavigationView
        navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

        getFragment()

    }

    private fun getFragment() {

        bottomNav.setOnItemSelectedListener {menuItem ->
            val currentFragment = navHostFragment.childFragmentManager.primaryNavigationFragment
            val fragmentName = currentFragment?.javaClass?.simpleName
            println(fragmentName)
            when(menuItem.itemId) {
                R.id.home -> {
                    if (fragmentName == "StorageFragment") {
                        val action = StorageFragmentDirections.actionStorageFragmentToHomeFragment()
                        navController.navigate(action)
                    } else {
                        Toast.makeText(this@MainActivity, "You are already on the Home Page!", Toast.LENGTH_LONG).show()
                    }
                    true
                }
                R.id.storage -> {
                    if (fragmentName == "HomeFragment") {
                        val action = HomeFragmentDirections.actionHomeFragmentToStorageFragment()
                        navController.navigate(action)
                    } else {
                        Toast.makeText(this@MainActivity, "You are already on the Storage Page!", Toast.LENGTH_LONG).show()
                    }
                    true
                }
                else -> {
                    Toast.makeText(this@MainActivity, "Unexpected problem", Toast.LENGTH_LONG).show()
                    true
                }
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.signOut) {
            auth.signOut()
            val intentToLogin = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intentToLogin)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }


}