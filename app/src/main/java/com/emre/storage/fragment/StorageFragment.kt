package com.emre.storage.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.emre.storage.R
import com.emre.storage.adapter.ProductDataAdapter
import com.emre.storage.databinding.FragmentHomeBinding
import com.emre.storage.databinding.FragmentStorageBinding
import com.emre.storage.model.ProductData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class StorageFragment : Fragment() {

    private lateinit var binding: FragmentStorageBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storageRef: DocumentReference
    private lateinit var productRef: DocumentReference
    private lateinit var userEmail: String
    private lateinit var spinnerArray: ArrayList<String>
    private lateinit var currency: String
    private lateinit var recyclerAdapter: ProductDataAdapter
    private lateinit var adapterArray: ArrayList<ProductData>
    private lateinit var productName: String
    private lateinit var price: String
    private lateinit var color: String
    private lateinit var stock: String
    private lateinit var getPrice: HashMap<String, String>
    private lateinit var getCurrency: HashMap<String, String>
    private var totalStock = 0
    private var totalPrice = 0.0
    private lateinit var formattedPrice: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentStorageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize
        auth = Firebase.auth
        firestore = Firebase.firestore
        userEmail = auth.currentUser!!.email!!
        spinnerArray = ArrayList()
        getPrice = hashMapOf()
        getCurrency = hashMapOf()
        adapterArray = ArrayList()

        // Main product doc reference
        storageRef = firestore.collection(userEmail).document("storage")
        // Main storage doc reference
        productRef = firestore.collection(userEmail).document("products")

        // Get products to spinner
        getDataToSpinner()

        // Action for spinner choice
        binding.spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                productName = parent?.getItemAtPosition(position).toString()
                price = getPrice.get(productName)!!
                currency = getCurrency.get(productName)!!
                getData()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                productName = spinnerArray[0]
                price = getPrice.get(productName)!!
                currency = getCurrency.get(productName)!!
                getData()
            }

        }

        // RecyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerAdapter = ProductDataAdapter(adapterArray)
        binding.recyclerView.adapter = recyclerAdapter

    }

    // Get data to spinner
    private fun getDataToSpinner() {

        productRef.collection("product").get().addOnSuccessListener {
            val documents = it.documents

            spinnerArray.clear()
            for (document in documents) {
                spinnerArray.add(document.id)
                getPrice.put(document.id, document.get("price").toString())
                getCurrency.put(document.id, document.get("currency").toString())
            }


            val spinner: Spinner = binding.spinner2
            val spinAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, spinnerArray)
            spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = spinAdapter
        }

    }

    private fun getData() {
        storageRef.collection("storage").get().addOnSuccessListener {
            val documents = it.documents

            totalPrice = 0.0
            totalStock = 0
            adapterArray.clear()
            for (document in documents) {
                println(document.id)
                if (document.get("pName") == productName) {
                    color = document.get("color").toString()
                    stock = document.get("stock").toString()

                    totalStock += stock.toInt()



                    val data = ProductData(productName, price, color, stock, currency)
                    adapterArray.add(data)
                }
                binding.totalStockText.text = "Total Stock: $totalStock"
                totalPrice = totalStock.toDouble() * price.toDouble()
                formattedPrice = String.format("%.2f", totalPrice)
                binding.totalPriceText.text = "Total Price: $formattedPrice $currency"
            }
            recyclerAdapter.notifyDataSetChanged()
        }
    }
}