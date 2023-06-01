package com.emre.storage.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.emre.storage.R
import com.emre.storage.databinding.ActivityMainBinding
import com.emre.storage.databinding.FragmentHomeBinding
import com.emre.storage.model.Products
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.processNextEventInCurrentThread

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var productArray: ArrayList<Products>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.productAddBtn.setOnClickListener { newProduct() }

        auth = Firebase.auth
        firestore = Firebase.firestore
        productArray = ArrayList()

        println(productArray.size)
        productArray.let {
            for (i in productArray) {
                println(i.name)
            }
        }

    }

    fun newProduct() {
        val productName = binding.productNameText.text.toString()
        val price = binding.priceText.text.toString()

        if (productName.isNotEmpty() && price.isNotEmpty()) {
            val product = Products(productName, price.toInt())

        } else {
            Toast.makeText(requireContext(), "Enter product name and price", Toast.LENGTH_LONG).show()
        }
        println(productArray.size)

    }
}