package com.example.myclubdeportivo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Payment : MenuBar() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        val btnExit = findViewById<ImageButton>(R.id.btnExit)

        val rbCash = findViewById<RadioButton>(R.id.rbCash)
        val rbCard = findViewById<RadioButton>(R.id.rbCard)
        val btnSubmit = findViewById<Button>(R.id.btnSubmint)

        val rgOptions: RadioGroup = findViewById(R.id.rgOptions)
        val rgCuotas: RadioGroup = findViewById(R.id.rgCuotas)
        val discountTextView: TextView = findViewById(R.id.discountTextView)

        val btnInicio = findViewById<Button>(R.id.btnInicio)
        val btnInscribir = findViewById<Button>(R.id.btnInscribir)
        val btnClases = findViewById<Button>(R.id.btnClases)
        val btnPagos = findViewById<Button>(R.id.btnPagos)
        val btnList = findViewById<Button>(R.id.btnList)

        btnExit.setOnClickListener {
            finishAffinity()
        }
        rgOptions.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rbCash -> {
                    rgCuotas.clearCheck()
                    findViewById<RadioButton>(R.id.rb1Cuota).isChecked = true
                    rgCuotas.visibility = View.GONE
                    discountTextView.visibility = View.VISIBLE
                }

                R.id.rbCard -> {
                    rgCuotas.visibility = View.VISIBLE
                    discountTextView.visibility = View.GONE
                }
            }
        }
        btnSubmit.setOnClickListener{
            if(rbCash.isChecked) {
                Toast.makeText(this, "Opción: Efectivo", Toast.LENGTH_SHORT).show()
            } else if(rbCard.isChecked){
                Toast.makeText(this, "Opción: Tarjeta", Toast.LENGTH_SHORT).show()
            } else{
                Toast.makeText(this, "Selecciona una forma de pago", Toast.LENGTH_SHORT).show()
            }
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