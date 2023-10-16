package com.example.bthome.fragments

import DatabaseHelper
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bthome.R
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import com.example.bthome.fragments.LoginFragment.Companion.loginName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class RegisterFragment : Fragment() {

    private lateinit var nameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var phoneNumberEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var tvText: TextView
    private lateinit var registerButton: Button

    private lateinit var databaseHelper: DbHelperRegister

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        nameEditText = view.findViewById(R.id.nameEditText)
        passwordEditText = view.findViewById(R.id.passwordEditText)
        confirmPasswordEditText = view.findViewById(R.id.confirmPasswordEditText)
        phoneNumberEditText = view.findViewById(R.id.phoneNumberEditText)
        emailEditText = view.findViewById(R.id.emailEditText)
        registerButton = view.findViewById(R.id.registerButton)
        tvText = view.findViewById(R.id.tvRegister)

//        registerButton.setOnClickListener {
//            if (validateInput()) {
//                saveUserToDatabase()
//            }
//        }

        databaseHelper = DbHelperRegister(requireContext())

        registerButton.setOnClickListener {
            if (validateInput()) {
                val name = nameEditText.text.toString()
                val password = passwordEditText.text.toString()
                val phoneNumber = phoneNumberEditText.text.toString()
                val email = emailEditText.text.toString()

                val user = User(name, password, phoneNumber, email)

                val result = databaseHelper.addUser(user)
                if (result != -1L) {
                    // User data was successfully inserted into the database
                    val dbHelper = DatabaseHelper(requireContext())
                    val initialData = dbHelper.getAllResponseData()
                    loginName = name
                    Handler(Looper.myLooper()!!).postDelayed({
                        if(initialData.isEmpty()){
//                findNavController().navigate(R.id.action_splashScreenFragment_to_addBleDeviceFragment)
//                findNavController().navigate(R.id.action_splashScreenFragment_to_informationFragment)
                            findNavController().navigate(R.id.action_registerFragment_to_informationFragment)
//                findNavController().navigate(R.id.action_splashScreenFragment_to_mainFragment)
                        } else {
//                findNavController().navigate(R.id.action_splashScreenFragment_to_mainFragment)
                            findNavController().navigate(R.id.action_registerFragment_to_addBleDeviceFragment)
//                findNavController().navigate(R.id.action_splashScreenFragment_to_addBleDeviceFragment)
//                findNavController().navigate(R.id.action_splashScreenFragment_to_informationFragment)
                        }
                    },2000)
                    Toast.makeText(requireContext(), "User registered successfully", Toast.LENGTH_SHORT).show()
                } else {
                    // Error occurred while inserting data
                    Toast.makeText(requireContext(), "Error registering user", Toast.LENGTH_SHORT).show()
                }
            }
        }
tvText.setOnClickListener {
    findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
}

        return view
    }

    private fun validateInput(): Boolean {
        var isValid = true

        val name = nameEditText.text.toString()
        val password = passwordEditText.text.toString()
        val confirmPassword = confirmPasswordEditText.text.toString()
        val phoneNumber = phoneNumberEditText.text.toString()
        val email = emailEditText.text.toString()

        if (name.isEmpty()) {
            nameEditText.error = "Name is required"
            isValid = false
        }

        if (password.isEmpty()) {
            passwordEditText.error = "Password is required"
            isValid = false
        }

        if (confirmPassword.isEmpty()) {
            confirmPasswordEditText.error = "Confirm Password is required"
            isValid = false
        }

        if (password != confirmPassword) {
            confirmPasswordEditText.error = "Passwords do not match"
            isValid = false
        }

        if (phoneNumber.isEmpty()) {
            phoneNumberEditText.error = "Phone Number is required"
            isValid = false
        }

        if (email.isEmpty()) {
            emailEditText.error = "Email is required"
            isValid = false
        }

        return isValid
    }

    private fun saveUserToDatabase() {
        val name = nameEditText.text.toString()
        val password = passwordEditText.text.toString()
        val phoneNumber = phoneNumberEditText.text.toString()
        val email = emailEditText.text.toString()



    }
}
