package com.emre.storage.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.emre.storage.databinding.FragmentHomeBinding
import com.emre.storage.model.Products
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var productArray: ArrayList<Products>
    private lateinit var userEmail: String
    private lateinit var productRef: DocumentReference
    private lateinit var storageRef: DocumentReference
    private lateinit var spinnerArray : ArrayList<String>

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
        userEmail = auth.currentUser!!.email.toString()
        spinnerArray = ArrayList()

        // Main doc reference
        productRef = firestore.collection(userEmail).document("products")
        // Main collection reference
        storageRef = firestore.collection(userEmail).document("storage")

        getDataToSpinner()



    }
    private fun newProduct() {
        val productName = binding.productNameText.text.toString().split(" ").joinToString(" ") { it.replaceFirstChar { it.uppercaseChar() } }
        val price = binding.priceText.text.toString()

        if (productName.isNotEmpty() && price.isNotEmpty()) {
            val product = hashMapOf(
                "pName" to productName,
                "price" to price
            )

            // SubColletion and Doc
            productRef.collection("product").document(productName).set(product).addOnSuccessListener {
                binding.productNameText.setText("")
                binding.priceText.setText("")

                Toast.makeText(requireContext(), "The product has been successfully added / updated.", Toast.LENGTH_SHORT).show()

                // Update spinner
                getDataToSpinner()
            }

        } else {
            Toast.makeText(requireContext(), "Enter product name and price", Toast.LENGTH_LONG).show()
        }


    }

    private fun getDataToSpinner() {
        productRef.collection("product").get().addOnSuccessListener {doc ->
            val documents = doc.documents

            spinnerArray.clear()
            productArray.clear()

            for (document in documents) {
                val docPName = document.get("pName").toString()
                val docPrice = document.get("price").toString()
                val product = Products(docPName, docPrice)

                productArray.add(product)

                spinnerArray.add(docPName)
            }

            val spinner: Spinner = binding.spinner

            val spinAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, spinnerArray)
            spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = spinAdapter

            if (spinnerArray.isEmpty()) {
                binding.stockAddBtn.isEnabled = false
                binding.stockDeleteBtn.isEnabled = false
            } else {
                binding.stockAddBtn.isEnabled = true
                binding.stockDeleteBtn.isEnabled = true
            }
        }

    }
}