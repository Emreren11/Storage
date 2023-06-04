package com.emre.storage.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.emre.storage.databinding.FragmentHomeBinding
import com.emre.storage.view.MainActivity
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
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
    private lateinit var userEmail: String
    private lateinit var productRef: DocumentReference
    private lateinit var storageRef: DocumentReference
    private lateinit var spinnerArray : ArrayList<String>
    private lateinit var spinnerPrdName: String
    private lateinit var spinnerCurrencyArray: ArrayList<String>
    lateinit var currency: String

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
        binding.stockAddBtn.setOnClickListener { newStock() }
        binding.stockDeleteBtn.setOnClickListener { deleteStock() }


        auth = Firebase.auth
        firestore = Firebase.firestore
        userEmail = auth.currentUser!!.email.toString()
        spinnerArray = ArrayList()
        spinnerCurrencyArray = arrayListOf("$", "€", "TL")


        // Main product doc reference
        productRef = firestore.collection(userEmail).document("products")
        // Main storage doc reference
        storageRef = firestore.collection(userEmail).document("storage")

        // Get products to spinner
        getDataToSpinner()


        // Action for product spinner choice
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                spinnerPrdName = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                spinnerPrdName = spinnerArray[0]
            }
        }

        // Action for currency spinnew choice
        binding.spinnerCurreny.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                currency = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                currency = "$"
            }

        }



    }
    // Adding new product
    private fun newProduct() {
        val productName = binding.productNameText.text.toString().split(" ").joinToString(" ") { it.replaceFirstChar { it.uppercaseChar() } }
        val price = binding.priceText.text.toString()

        if (productName.isNotEmpty() && price.isNotEmpty()) {
            val product = hashMapOf(
                "pName" to productName,
                "price" to price,
                "currency" to currency
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



    // Adding product to storage
    private fun newStock() {
        val color = binding.colorText.text.toString().split(" ").joinToString(" ") { it.replaceFirstChar { it.uppercaseChar() } }
        val stock = binding.stockText.text.toString()

        if (color.isNotEmpty() && stock.isNotEmpty()) {
            val docName = "$color $spinnerPrdName"
            var lastStockInfo: Int
            val stockInfo = getStockInfo(docName)
            stockInfo.addOnSuccessListener {
                lastStockInfo = it + stock.toInt()

                val dataForStorage = hashMapOf(
                    "pName" to spinnerPrdName,
                    "color" to color,
                    "stock" to lastStockInfo.toString()
                )

                storageRef.collection("storage").document(docName).set(dataForStorage).addOnSuccessListener {
                    binding.colorText.setText("")
                    binding.stockText.setText("")
                    Toast.makeText(requireContext(), "Stock has been successfully added to storage", Toast.LENGTH_SHORT).show()

                }
            }

        } else {
            Toast.makeText(requireContext(), "Enter color and stock", Toast.LENGTH_SHORT).show()
        }


    }

    // Delete stock
    private fun deleteStock() {
        val color = binding.colorText.text.toString().split(" ").joinToString(" ") { it.replaceFirstChar { it.uppercaseChar() } }
        val stock = binding.stockText.text.toString()

        if (color.isNotEmpty() && stock.isNotEmpty()) {

            val docName = "$color $spinnerPrdName"

            val stockControl = getStockInfo(docName)

            stockControl.addOnSuccessListener { result ->

                println(result)
                var resultStock = result

                if (resultStock == 0) {
                    Toast.makeText(requireContext(), "Your stock quantity already 0", Toast.LENGTH_LONG).show()
                } else {
                    resultStock -= stock.toInt()
                    if (resultStock < 0) {
                        Toast.makeText(requireContext(), "Your stock quantity is not sufficient", Toast.LENGTH_SHORT).show()
                    } else {
                        val dataForStorage = hashMapOf(
                            "pName" to spinnerPrdName,
                            "color" to color,
                            "stock" to resultStock.toString()
                        )
                        storageRef.collection("storage").document(docName).set(dataForStorage).addOnSuccessListener {
                            binding.colorText.setText("")
                            binding.stockText.setText("")
                            Toast.makeText(requireContext(), "Stock quantity reduced by $result", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            }

        } else {
            Toast.makeText(requireContext(), "Enter color and stock", Toast.LENGTH_SHORT).show()
        }
    }

    // Get data to spinner
    private fun getDataToSpinner() {
        productRef.collection("product").get().addOnSuccessListener {doc ->
            val documents = doc.documents

            spinnerArray.clear()

            for (document in documents) {
                val docPName = document.get("pName").toString()
                val docPrice = document.get("price").toString()

                spinnerArray.add(docPName)
            }

            // Product Spinner
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

        // Currency Spinner
        val spinCurreny: Spinner = binding.spinnerCurreny
        val spinCryAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item,spinnerCurrencyArray)
        spinCryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinCurreny.adapter = spinCryAdapter

    }

    // If the document already exists, it retrieves the stock information.
    private fun getStockInfo(docName: String): Task<Int>{

        val completionSource = TaskCompletionSource<Int>()

        storageRef.collection("storage").get().addOnSuccessListener {
            val documents = it.documents
            for (document in documents) {
                if (document.id == docName) {
                    val docStock = document.get("stock").toString()
                    val stockFromDoc = docStock.toInt()
                    completionSource.setResult(stockFromDoc)
                    return@addOnSuccessListener
                }
            }
            completionSource.setResult(0)
        }
        return completionSource.task

    }
}