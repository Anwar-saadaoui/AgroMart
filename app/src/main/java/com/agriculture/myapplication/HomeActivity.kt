package com.agriculture.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.agriculture.myapplication.Utils.Utils
import com.agriculture.myapplication.databinding.ActivityHomeBinding
import com.agriculture.myapplication.model.Product
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeActivity : AppCompatActivity() {
    private lateinit var binding:ActivityHomeBinding
    private lateinit var listProduct:MutableList<Product>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.productList.layoutManager = GridLayoutManager(baseContext,2)
        // Get a reference to your database
        val myRef = FirebaseDatabase.getInstance().getReference("Product")
        listProduct = mutableListOf<Product>()

        val adapter = RecyclerAdapter(listProduct, object:RecyclerAdapter.addOnItemClickedListener{
            override fun ItemClicked(product: Product) {
                TODO("Not yet implemented")
            }
        })
            binding.productList.adapter = adapter

        // Read from the database
        myRef.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                listProduct.clear()
                for (snapshot in dataSnapshot.children) {
                    if (snapshot.exists()){

                        val product = snapshot.getValue(Product::class.java)
                        if (product != null) {
                            listProduct.add(product)
                            Log.d("product", "onDataChange: $listProduct")
                        }
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Utils.showToast(baseContext, "Failed to read value")
            }
        })
    }
}