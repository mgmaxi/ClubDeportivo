package com.example.myclubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ListadoSocios : MenuBar() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listado_socios)

        val btnExit = findViewById<ImageButton>(R.id.btnExit)
        val btnInicio = findViewById<Button>(R.id.btnInicio)
        val btnInscribir = findViewById<Button>(R.id.btnInscribir)
        val btnClases = findViewById<Button>(R.id.btnClases)
        val btnPagos = findViewById<Button>(R.id.btnPagos)
        val btnList = findViewById<Button>(R.id.btnList)

        btnExit.setOnClickListener {
            finishAffinity()
        }
        btnInicio.setOnClickListener {
            val intent = Intent(this, MainMenu::class.java)
            startActivity(intent)
        }
        btnInscribir.setOnClickListener {
            val intent = Intent(this, InscribirSocio::class.java)
            startActivity(intent)
        }
        btnClases.setOnClickListener {
            val intent = Intent(this, ListadoClases::class.java)
            startActivity(intent)
        }
        btnPagos.setOnClickListener {
            val intent = Intent(this, Payment::class.java)
            startActivity(intent)
        }
        btnList.setOnClickListener {
            val intent = Intent(this, Vencimientos::class.java)
            startActivity(intent)
        }
    }
}