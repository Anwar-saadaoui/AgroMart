package com.agriculture.myapplication

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.agriculture.myapplication.Utils.Utils

class DashFragment : DialogFragment() {


    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dash, container, false)

        val admin = view.findViewById<EditText>(R.id.admin)
        val adminCode = view.findViewById<EditText>(R.id.admin_code)
        val btn = view.findViewById<Button>(R.id.admin_btn)

        btn.setOnClickListener {
            val adminText = admin.text.toString()
            val adminCodeText = adminCode.text.toString()
            if (!TextUtils.isEmpty(adminText) && !TextUtils.isEmpty(adminCodeText)){
                if (adminText == "admin" && adminCodeText == "1111"){
                    startActivity(Intent(context, DashBoard::class.java))
                }else
                    Utils.showToast(requireContext(), "wrong admin")
            }else
                Utils.showToast(requireContext(), "necessary admin and code admin")
        }



        return view
    }

}