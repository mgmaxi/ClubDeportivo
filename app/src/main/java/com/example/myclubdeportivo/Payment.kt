package com.example.myclubdeportivo

import android.annotation.SuppressLint
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

    @SuppressLint("DefaultLocale")
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
        val dniEditText = findViewById<EditText>(R.id.dniEditText)
        val importeTextView = findViewById<TextView>(R.id.importeTextView)

        btnExit.setOnClickListener {
            finishAffinity()
        }

        rgOptions.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbCash -> {
                    rgCuotas.clearCheck()
                    findViewById<RadioButton>(R.id.rb1Cuota).isChecked = true
                    rgCuotas.visibility = View.GONE
                }
                R.id.rbCard -> {
                    rgCuotas.visibility = View.VISIBLE
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

            if (memberId != null) {
                val totalAmount = dbHelper.getTotalCoursesAmountByMemberId(memberId)

                importeTextView.text = String.format("Importe total a pagar: $%.2f", totalAmount)

                val amountToPay = if (rbCash.isChecked) {
                    totalAmount * 0.90
                } else {
                    totalAmount
                }

                val installments = when (rgCuotas.checkedRadioButtonId) {
                    R.id.rb1Cuota -> 1
                    R.id.rb2Cuotas -> 3
                    R.id.rb3Cuotas -> 6
                    else -> 1
                }

                fun getCurrentDate(): String {
                    return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                }

                val newPayment = Payment(
                    memberId = memberId.toString(),
                    amount = String.format("%.2f", amountToPay),
                    date = getCurrentDate(),
                    paymentMethod = if (rbCash.isChecked) "Cash" else "Card",
                    installments = installments.toString()
                )

                dbHelper.addPayment(newPayment)

                dbHelper.markDebtAsPaid(memberId)

                Toast.makeText(this, "Pago registrado con éxito.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "No se encontró un socio con ese número de documento.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
