package com.example.myclubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SignIn : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        val dbHelper = DataBaseHelper(this)

        val btnSignIn = findViewById<Button>(R.id.btnSignin)

        val signInEditTextUsername = findViewById<EditText>(R.id.signInEditTextUsername)
        val signInEditTextPassword = findViewById<EditText>(R.id.signInEditTextPassword)

        fun showToast(message: String) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }

        btnSignIn.setOnClickListener {
            val username = signInEditTextUsername.text.toString().trim()
            val password = signInEditTextPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                showToast("Los campos no pueden estar vacíos.")
                return@setOnClickListener
            }

            if (dbHelper.userExist(username, password)) {
                showToast("El nombre de usuario no está disponible.")
            } else {
                dbHelper.registerUser(username, password)
                startActivity(Intent(this, Login::class.java))
                showToast("Registro exitoso")
            }
        }
    }
}
