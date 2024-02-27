package com.agriculture.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.agriculture.myapplication.databinding.ActivityActionBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ActionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityActionBinding
    private lateinit var database:DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityActionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        val productName:String? = intent.getStringExtra("productName")
        
        
        binding.updateButton.setOnClickListener { 
            val price = binding.newPrice.text.toString().toDouble()
            val quantity = binding.newQuantity.text.toString().toDouble()
            
            updateProduct(price,quantity,productName)
        }

        binding.deleteBtn.setOnClickListener {
            val productN = binding.toDeleteProduct.text.toString()
            deleteProduct(productN)
        }

        
    }

    private fun deleteProduct(productN: String) {
        database = FirebaseDatabase.getInstance().getReference("Product")

        database.child(productN).removeValue().addOnSuccessListener {
            Toast.makeText(baseContext, "Data has been deleted", Toast.LENGTH_LONG).show()
        }.addOnFailureListener{
            Toast.makeText(baseContext, "The data has not been deleted", Toast.LENGTH_LONG).show()
        }
    }

    private fun updateProduct(price: Double = 0.0, quantity: Double = 0.0, productName: String?) {
        database = FirebaseDatabase.getInstance().getReference("Product")
        if (price != 0.0 && quantity != 0.0) {
            val product = mapOf(
                "price" to price,
                "quantity" to quantity
            )
            whatWillUpdated(productName, product)
        }

        if (price != 0.0 && quantity == 0.0) {
            val product = mapOf(
                "price" to price,
            )
            whatWillUpdated(productName, product)
        }
        if (price == 0.0 && quantity != 0.0) {
            val product = mapOf(
                "quantity" to quantity
            )
            whatWillUpdated(productName, product)
        }

    }

    private fun whatWillUpdated(productName: String?, product: Map<String, Double>) {
        database = FirebaseDatabase.getInstance().getReference("Product")
        database.child(productName!!).updateChildren(product).addOnSuccessListener {
            Toast.makeText(baseContext, "Data has been updated", Toast.LENGTH_LONG).show()
        }.addOnFailureListener{
            Toast.makeText(baseContext, "The data has not been updated", Toast.LENGTH_LONG).show()
        }
    }

}