package com.agriculture.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.view.animation.RotateAnimation
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.agriculture.myapplication.Utils.Utils
import com.agriculture.myapplication.databinding.ActivityDashBoardBinding
import com.agriculture.myapplication.model.Product
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Suppress("deprecated")
class DashBoard : AppCompatActivity() {

    private lateinit var binding:ActivityDashBoardBinding
    private lateinit var imageUri:Uri
    private lateinit var imageUriStorage:String
    private lateinit var database:DatabaseReference
    private lateinit var fileName:String
    private val loading = Utils(this)

    private val launcher: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            // Handle the result here
            if (uri != null) {
                // Use the selected image URI
                imageUri = uri
                binding.addImage.setImageURI(uri)
                loading.showProgress()

                val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
                fileName = formatter.format(Date())
//                val storageReference = FirebaseStorage.getInstance().getReference("ProductImg/$fileName")
                val storageRef = FirebaseStorage.getInstance().reference
                val imageRef = storageRef.child("ProductImg/$fileName")
                val uploadTask = imageRef.putFile(imageUri)

                uploadTask.addOnSuccessListener {
                    // Image uploaded successfully
                    // Now, get the download URL
                    Utils.showToast(baseContext, "images added successfully")
                    // hide progress
                    loading.dismissProgress()
                    imageRef.downloadUrl.addOnSuccessListener { uri ->
                        imageUriStorage = uri.toString()
                    }
                }.addOnFailureListener {
                    // hide progress
                    loading.dismissProgress()
                    Utils.showToast(baseContext, "Failed save image")
                    Log.d("save", "${it.message}")
                }

            }
        }


    @SuppressLint("CommitTransaction")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.addProduct.setOnClickListener {

            val name = binding.addProductName.text.toString()
            val price = binding.addPrice.text.toString().toDouble()
            val quantity = binding.addQuantity.text.toString().toDouble()
            database = FirebaseDatabase.getInstance().getReference("Product")
            val product = Product(name, price, quantity,imageUriStorage)
            runOnUiThread {
                loading.showProgress()
            }
            database.child(name).setValue(product).addOnSuccessListener {
                binding.addProductName.text.clear()
                binding.addPrice.text.clear()
                binding.addQuantity.text.clear()
                Toast.makeText(baseContext, "product has been added successful", Toast.LENGTH_LONG).show()
                loading.dismissProgress()
                binding.addImage.setImageResource(R.drawable.uploadimg)
            }.addOnFailureListener{
                Toast.makeText(baseContext, "product doesn't added", Toast.LENGTH_LONG).show()
                loading.dismissProgress()
            }
        }



        //pick an image from the device and insert it in firebase storage
        binding.addImage.setOnClickListener {
            launcher.launch("image/*")
        }


        // programming the setting button
        binding.settingImage.setOnClickListener{
//            val rotate = RotateAnimation(
//                0F, 360F, RotateAnimation.RELATIVE_TO_SELF,
//                .5f, RotateAnimation.RELATIVE_TO_SELF, .5f)
//            rotate.duration = 500
//            binding.settingImage.startAnimation(rotate)

            supportFragmentManager.beginTransaction().replace(R.id.materialCardView,OptionFragment()).commit()

        }
    }

//    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
//        super.onCreateContextMenu(menu, v, menuInfo)
//        menuInflater.inflate(R.menu.option_admin_layou, menu)
//    }


//    override fun onContextItemSelected(item: MenuItem): Boolean {
//
//        return when(item.itemId){
//            R.id.edit_action -> {
//                true
//            }
//
//            R.id.delete_action ->{
//                true
//            }
//
//            else -> {
//                return super.onContextItemSelected(item)
//            }
//        }
//
//    }


}