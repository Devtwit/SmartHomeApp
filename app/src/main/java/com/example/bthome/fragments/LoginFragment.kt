package com.example.bthome.fragments

import DatabaseHelper
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import com.example.bthome.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {
    private lateinit var dbHelper: DbHelperRegister

    companion object{
        var loginName= "Hello"
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        val nameEditText = view.findViewById<EditText>(R.id.etUsername)
//        val phoneNumberEditText = view.findViewById<EditText>(R.id.etPhoneNumber)
//        val emailEditText = view.findViewById<EditText>(R.id.etEmailId)
        val passwordEditText = view.findViewById<EditText>(R.id.etPassword)
        val tvRegister = view.findViewById<TextView>(R.id.tvRegister)

        val loginButton = view.findViewById<Button>(R.id.btnLogin)
        dbHelper = DbHelperRegister(requireContext())
        loginButton.setOnClickListener {
            val name = nameEditText.text.toString()
//            val phoneNumber = phoneNumberEditText.text.toString()
//            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Perform database operations in a background coroutine
            GlobalScope.launch(Dispatchers.IO) {
//                val userExists = dbHelper.checkUserData(name, phoneNumber, email, password)
                val userExists = dbHelper.checkUserData(name,password)

                launch(Dispatchers.Main) {
                    if (userExists) {
                        loginName = name
                        // User data is present in the database
                        Toast.makeText(requireContext(), "Login successful", Toast.LENGTH_SHORT).show()
                        val dbHelper = DatabaseHelper(requireContext())
                        val initialData = dbHelper.getAllResponseData()
                        Handler(Looper.myLooper()!!).postDelayed({
                            if(initialData.isEmpty()){
//                findNavController().navigate(R.id.action_splashScreenFragment_to_addBleDeviceFragment)
//                findNavController().navigate(R.id.action_splashScreenFragment_to_informationFragment)
                                findNavController().navigate(R.id.action_loginFragment_to_informationFragment)
//                findNavController().navigate(R.id.action_splashScreenFragment_to_mainFragment)
                            } else {

//                findNavController().navigate(R.id.action_splashScreenFragment_to_mainFragment)
                                findNavController().navigate(R.id.action_loginFragment_to_addBleDeviceFragment)
//                findNavController().navigate(R.id.action_splashScreenFragment_to_addBleDeviceFragment)
//                findNavController().navigate(R.id.action_splashScreenFragment_to_informationFragment)
                            }
                        },2000)
                        // Navigate to the home screen here
                    } else {
                        // User data is not found in the database
                        Toast.makeText(requireContext(), "Invalid user data", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
         tvRegister.setOnClickListener {
             findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
         }

        return view
    }

    private  fun checkUserData(name: String, phoneNumber: String, email: String, password: String): Boolean {
       return true
    }
}
