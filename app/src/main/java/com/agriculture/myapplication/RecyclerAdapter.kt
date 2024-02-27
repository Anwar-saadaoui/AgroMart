package com.agriculture.myapplication

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.agriculture.myapplication.model.Product
import com.squareup.picasso.Picasso

class RecyclerAdapter(private var listProduct:MutableList<Product>, private var listener:addOnItemClickedListener) : RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>() {

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view:View = LayoutInflater.from(parent.context).inflate(R.layout.product_rcv_layout,null,false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listProduct.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val p:Product = listProduct[position]
        Picasso.get().load(p.imageUrl).into(holder.image_rc)
        holder.name_rc.text = p.productName
        holder.quantity_rc.text = p.quantity.toString()
        holder.price_rc.text = p.price.toString()
    }


    inner class MyViewHolder(itemView: View) : ViewHolder(itemView) {
        val image_rc: ImageView = itemView.findViewById(R.id.image_rc)
        val name_rc: TextView = itemView.findViewById(R.id.product_name_rc)
        val price_rc: TextView = itemView.findViewById(R.id.price_rc)
        val quantity_rc: TextView = itemView.findViewById(R.id.quantity_rc)

        init {

            itemView.setOnClickListener{
                val position = adapterPosition
                val product:Product = listProduct[position]
                listener.ItemClicked(product)
            }

        }


    }

    interface addOnItemClickedListener {
        fun ItemClicked(product: Product)
    }



}