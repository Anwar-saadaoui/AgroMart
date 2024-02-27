package com.agriculture.myapplication.model

import android.net.Uri

data class Product(var productName: String = "", var price: Double = 0.0, var quantity: Double = 0.0, var imageUrl: String = "") {
    // No-argument constructor required by Firebase
    constructor() : this("", 0.0, 0.0, "")
}
