package com.example.clubdeportivo

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Payment : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        val rbCash = findViewById<RadioButton>(R.id.rbCash)
        val rbCard = findViewById<RadioButton>(R.id.rbCard)
        val btnSubmit = findViewById<Button>(R.id.btnSubmint)

        val rgOptions: RadioGroup = findViewById(R.id.rgOptions)
        val rgCuotas: RadioGroup = findViewById(R.id.rgCuotas)
        val discountTextView: TextView = findViewById(R.id.discountTextView)

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
    }

}
