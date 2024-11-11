package com.example.myclubdeportivo

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myclubdeportivo.model.Payment
import java.util.Date
import java.util.Locale

class Payment : AppCompatActivity() {
    private lateinit var dbHelper: DataBaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        dbHelper = DataBaseHelper(this)

        val btnExit = findViewById<ImageButton>(R.id.btnExit)
        val btnSubmit = findViewById<Button>(R.id.btnSubmint)
        val rbCash = findViewById<RadioButton>(R.id.rbCash)
        val rbCard = findViewById<RadioButton>(R.id.rbCard)
        val rgOptions: RadioGroup = findViewById(R.id.rgOptions)
        val rgCuotas: RadioGroup = findViewById(R.id.rgCuotas)
        val discountTextView: TextView = findViewById(R.id.discountTextView)
        val dniEditText = findViewById<EditText>(R.id.dniEditText)
        val importeTextView = findViewById<TextView>(R.id.importeTextView)

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

        btnSubmit.setOnClickListener {
            val documentNumber = dniEditText.text.toString()
            if (documentNumber.isEmpty()) {
                Toast.makeText(this, "Por favor ingresa el número de documento", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val memberId = dbHelper.getMemberIdByDocumentNumber(documentNumber)
            val totalPayments = dbHelper.getTotalPaymentsByDNI(dniEditText.toString())
            importeTextView.text = totalPayments.toString()

            if (memberId != null) {

                val amount = if (rbCash.isChecked) {
                    totalPayments * 0.90
                } else {
                    totalPayments
                }

                val installments = when (rgCuotas.checkedRadioButtonId) {
                    R.id.rb1Cuota -> "1"
                    R.id.rb2Cuotas -> "3"
                    R.id.rb3Cuotas -> "6"
                    else -> "1"
                }

                val paymentMethod = if (rbCash.isChecked) "Cash" else "Card"

                fun getCurrentDate(): String {
                    return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                }

                val newPayment = Payment(
                    memberId = memberId.toString(),
                    amount = amount.toString(),
                    date = getCurrentDate(),
                    paymentMethod = paymentMethod,
                    installments = installments
                )
                dbHelper.addPayment(newPayment)

                Toast.makeText(this, "Pago registrado con éxito", Toast.LENGTH_SHORT).show()
            }
        else {
                Toast.makeText(this, "No se encontró un miembro con ese número de documento", Toast.LENGTH_SHORT).show()
        }
    }

    }
}