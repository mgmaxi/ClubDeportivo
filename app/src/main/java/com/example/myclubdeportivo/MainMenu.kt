package com.example.myclubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainMenu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)


        val btnExit = findViewById<ImageButton>(R.id.btnExit)
        val btnAddPartnerContainer = findViewById<CardView>(R.id.btnAddPartnerContainer)
        val btnClassListContainer = findViewById<CardView>(R.id.btnClassListContainer)
        val btnPaymentsContainer = findViewById<CardView>(R.id.btnPaymentsContainer)
        val btnExpirationsContainer = findViewById<CardView>(R.id.btnExpirationsContainer)
        val btnMembershipCard = findViewById<CardView>(R.id.btnMembershipCardContainer)
        val btnListSocContainer = findViewById<CardView>(R.id.btnListSocContainer)

        btnExit.setOnClickListener {
            finishAffinity()
        }
        btnAddPartnerContainer.setOnClickListener {
            val intent = Intent(this, InscribirSocio::class.java)
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