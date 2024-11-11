package com.example.myclubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class InscribirSocio : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inscribir_socio)

        val dbHelper = DataBaseHelper(this)

        val btnExit = findViewById<ImageButton>(R.id.btnExit)
        val btnInicio = findViewById<Button>(R.id.btnInicio)
        val btnInscribir = findViewById<Button>(R.id.btnInscribir)
        val btnClases = findViewById<Button>(R.id.btnClases)
        val btnPagos = findViewById<Button>(R.id.btnPagos)
        val btnList = findViewById<Button>(R.id.btnList)
        val btnInscription = findViewById<Button>(R.id.btnInscription)

        val etFirstName = findViewById<TextInputEditText>(R.id.txtName)
        val etLastName = findViewById<TextInputEditText>(R.id.txtSurname)
        val etDocumentType = findViewById<TextInputEditText>(R.id.txtType)
        val etDocumentNumber = findViewById<TextInputEditText>(R.id.txtNumber)
        val etAddress = findViewById<TextInputEditText>(R.id.txtDomicile)
        val etPhone = findViewById<TextInputEditText>(R.id.txtTelephone)
        val rgOptions = findViewById<RadioGroup>(R.id.rgOptions)

        fun showToast(message: String) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }

         fun clearFormFields() {
            etFirstName.text?.clear()
            etLastName.text?.clear()
            etDocumentType.text?.clear()
            etDocumentNumber.text?.clear()
            etAddress.text?.clear()
            etPhone.text?.clear()
            rgOptions.clearCheck()

            etFirstName.clearFocus()
            etLastName.clearFocus()
            etDocumentType.clearFocus()
            etDocumentNumber.clearFocus()
            etAddress.clearFocus()
            etPhone.clearFocus()
            rgOptions.clearFocus()
        }

        fun registerMember() {

            val firstName = etFirstName.text.toString().trim()
            val lastName = etLastName.text.toString().trim()
            val documentType = etDocumentType.text.toString().trim()
            val documentNumber = etDocumentNumber.text.toString().trim()
            val address = etAddress.text.toString().trim()
            val phone = etPhone.text.toString().trim()

            try {

                val isMember = when (rgOptions.checkedRadioButtonId) {
                    R.id.rbMember -> true
                    R.id.rbNoMember -> false
                    else -> {
                        showToast("Debes seleccionar 'Socio' o 'No socio'.")
                        return
                    }
                }

                if (firstName.isEmpty() || lastName.isEmpty() || documentType.isEmpty() || documentNumber.isEmpty() || address.isEmpty() || phone.isEmpty()  ) {
                    showToast("Los campos no pueden estar vacíos.")
                    return
                }

                val result = dbHelper.registerMember(firstName, lastName, documentType, documentNumber, address, phone, isMember)

                if (result > 0) {
                    showToast("$firstName $lastName ha sido registrado con éxito")
                    clearFormFields()
                }
            }
            catch (e: Exception) {
                showToast("Ocurrió un error inesperado: ${e.message}")
            }
        }

    btnInscription.setOnClickListener{
        registerMember()
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