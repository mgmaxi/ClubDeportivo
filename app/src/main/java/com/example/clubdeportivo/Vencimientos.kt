package com.example.clubdeportivo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class Vencimientos : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vencimientos)

        val databaseHelper = DatabaseHelper(this)
        }
    }
