package com.example.myclubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.google.android.material.button.MaterialButton

class CarnetBuscar : MenuBar() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carnet_buscar)

        val btnExit = findViewById<ImageButton>(R.id.btnExit)
        val btnInicio = findViewById<Button>(R.id.btnInicio)
        val btnInscribir = findViewById<Button>(R.id.btnInscribir)
        val btnClases = findViewById<Button>(R.id.btnClases)
        val btnPagos = findViewById<Button>(R.id.btnPagos)
        val btnList = findViewById<Button>(R.id.btnList)

        val etDNI = findViewById<EditText>(R.id.dniEditText)
        val btnBuscar = findViewById<MaterialButton>(R.id.btnBuscar)

        fun showToast(message: String) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }

        btnBuscar.setOnClickListener {
            val dbHelper = DataBaseHelper(this)
            val db = dbHelper.readableDatabase

            val ETdni = etDNI.getText().toString()

            if (ETdni.isEmpty()) {
                showToast("Debe ingresar un número de documento.")
            } else {
                val cursor = dbHelper.getMemberByDocumentNumber(ETdni)

                if (cursor.moveToFirst()) {
                    val id = cursor.getString(cursor.getColumnIndexOrThrow("id"))
                    val first = cursor.getString(cursor.getColumnIndexOrThrow("first_name"))
                    val last = cursor.getString(cursor.getColumnIndexOrThrow("last_name"))
                    val doc = cursor.getString(cursor.getColumnIndexOrThrow("document_number"))
                    val intent = Intent(this, CarnetImprimir::class.java)
                    intent.putExtra("ID", id)
                    intent.putExtra("firstName", first)
                    intent.putExtra("lastName", last)
                    intent.putExtra("documentNumber", doc)

                    cursor.close()
                    db.close()
                    startActivity(intent)
                } else {
                    showToast("No se encontró un socio con ese documento.")
                }
            }
        }

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
