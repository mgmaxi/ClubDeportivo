package com.example.myclubdeportivo

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class MainMenu : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val username = sharedPreferences.getString("username", "")
        val userTextView = findViewById<TextView>(R.id.UserTextView)

        userTextView.text = "Bienvenido, $username!"

        val btnExit = findViewById<ImageButton>(R.id.btnExit)
        val btnAddMemberContainer = findViewById<CardView>(R.id.btnAddPartnerContainer)
        val btnClassListContainer = findViewById<CardView>(R.id.btnClassListContainer)
        val btnPaymentsContainer = findViewById<CardView>(R.id.btnPaymentsContainer)
        val btnExpirationsContainer = findViewById<CardView>(R.id.btnExpirationsContainer)
        val btnMembershipCard = findViewById<CardView>(R.id.btnMembershipCardContainer)
        val btnListSocContainer = findViewById<CardView>(R.id.btnListSocContainer)

        btnExit.setOnClickListener {
            finishAffinity()
        }
        btnAddMemberContainer.setOnClickListener {
            val intent = Intent(this, InscribirSocio::class.java)
            startActivity(intent)
        }

        btnListSocContainer.setOnClickListener {
            val intent = Intent(this, ListadoSocios::class.java)
            startActivity(intent)
        }

        btnClassListContainer.setOnClickListener {
            val intent = Intent(this, ListadoClases::class.java)
            startActivity(intent)
        }
        btnPaymentsContainer.setOnClickListener {
            val intent = Intent(this, Payment::class.java)
            startActivity(intent)
        }
        btnExpirationsContainer.setOnClickListener {
            val intent = Intent(this, Vencimientos::class.java)
            startActivity(intent)
        }
        btnMembershipCard.setOnClickListener {
            val intent = Intent(this, CarnetBuscar::class.java)
            startActivity(intent)
        }
        btnMembershipCard.setOnClickListener {
            val intent = Intent(this, ListadoSocios::class.java)
            startActivity(intent)
        }


    }
}