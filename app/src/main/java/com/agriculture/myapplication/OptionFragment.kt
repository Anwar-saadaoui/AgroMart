package com.agriculture.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import com.agriculture.myapplication.Utils.Utils
import com.agriculture.myapplication.databinding.ActivityDashBoardBinding
import com.agriculture.myapplication.databinding.OptionFragmentBinding
import com.agriculture.myapplication.model.Product
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class OptionFragment : DialogFragment(){

    private lateinit var binding: OptionFragmentBinding
    private lateinit var listProduct:MutableList<Product>
    private lateinit var contextView:View
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = OptionFragmentBinding.inflate(layoutInflater,container,false)

        binding.optionRecyclerView.layoutManager = GridLayoutManager(context,2)
        // Get a reference to your database
        val myRef = FirebaseDatabase.getInstance().getReference("Product")
        listProduct = mutableListOf<Product>()

        val adapter = RecyclerAdapter(listProduct, object :RecyclerAdapter.addOnItemClickedListener{
            override fun ItemClicked(product: Product) {
//                val editPrice = product.price
//                val editQuantity = product.quantity
                val productName = product.productName
                val intent = Intent(context, ActionActivity::class.java)
                intent.apply {
                    putExtra("productName", productName)
//                    putExtra("price",editPrice)
//                    putExtra("quantity",editQuantity)
                }
                startActivity(intent)
            }
        })


        binding.optionRecyclerView.adapter = adapter

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
                context?.let { Utils.showToast(it, "Failed to read value") }
            }
        })


        return binding.root

    }


    override fun onCreateContextMenu(menu: ContextMenu,v: View,menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        activity?.menuInflater?.inflate(R.menu.option_admin_layou, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.edit_action -> {

                true
            }

            R.id.delete_action ->{
                true
            }

            else -> {
                return super.onContextItemSelected(item)
            }
        }
    }


}