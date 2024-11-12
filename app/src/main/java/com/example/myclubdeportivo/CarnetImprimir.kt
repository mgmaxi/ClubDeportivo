package com.example.myclubdeportivo

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import com.google.android.material.button.MaterialButton
import java.io.IOException
import java.io.OutputStream

class CarnetImprimir :MenuBar() {

    private lateinit var createFileLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carnet_imprimir)

        val btnExit = findViewById<ImageButton>(R.id.btnExit)
        val btnInicio = findViewById<Button>(R.id.btnInicio)
        val btnInscribir = findViewById<Button>(R.id.btnInscribir)
        val btnClases = findViewById<Button>(R.id.btnClases)
        val btnPagos = findViewById<Button>(R.id.btnPagos)
        val btnList = findViewById<Button>(R.id.btnList)

        val btnImprimir = findViewById<MaterialButton>(R.id.btnImprimir)

        val Id = intent.getStringExtra("ID")
        val firstName = intent.getStringExtra("firstName")
        val lastName = intent.getStringExtra("lastName")
        val Number = intent.getStringExtra("documentNumber")

        val MemberId = findViewById<TextView>(R.id.numeroSocioTextView)
        val Member = findViewById<TextView>(R.id.nombreApellidoTextView)
        val Document = findViewById<TextView>(R.id.dniTextView)

        Member.text = "$firstName $lastName"
        Document.text = "Documento: $Number"
        MemberId.text = "Numero de socio: $Id"

        val cardViewToSave: CardView = findViewById(R.id.cvCarnet)

        // Función para convertir la vista (CardView) en un Bitmap
        fun getBitmapFromView(view: View): Bitmap {
            val width = view.width
            val height = view.height
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            view.draw(canvas)
            return bitmap
        }

        // Función para guardar el archivo PDF en el URI seleccionado
        fun savePdfToUri(uri: Uri) {
            val pdfDocument = PdfDocument()

            // Asegurarse de que el documento PDF no esté vacío
            val bitmap = getBitmapFromView(findViewById(R.id.cvCarnet)) // Re-crear el bitmap de la vista
            val pageInfo = PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, 1).create()
            val page = pdfDocument.startPage(pageInfo)

            val canvas: Canvas = page.canvas
            canvas.drawBitmap(bitmap, 0f, 0f, null)
            pdfDocument.finishPage(page)

            try {
                // Obtener el OutputStream para el archivo seleccionado
                val outputStream: OutputStream? = contentResolver.openOutputStream(uri)
                if (outputStream != null) {
                    // Escribir el contenido del PDF en el OutputStream
                    pdfDocument.writeTo(outputStream)
                    outputStream.close()

                    // Mensaje de éxito
                    Toast.makeText(this, "PDF guardado correctamente", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "No se pudo abrir el archivo para escritura", Toast.LENGTH_SHORT).show()
                }
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(this, "Error al guardar el PDF: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                pdfDocument.close()
            }
        }

        // Inicializa el ActivityResultLauncher en onCreate()
        createFileLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri: Uri? = result.data?.data
                uri?.let { savePdfToUri(it) }
            } else {
                Toast.makeText(this, "El usuario canceló la selección", Toast.LENGTH_SHORT).show()
            }
        }

        // Función para guardar el PDF después de que el usuario seleccione la ubicación
        fun saveCardViewAsPdf(view: View) {
            // Crear un bitmap a partir del CardView
            val bitmap = getBitmapFromView(view)

            // Si el Bitmap es muy grande, redimensionarlo
            val scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.width / 2, bitmap.height / 2, false)

            // Crear un documento PDF
            val pdfDocument = PdfDocument()

            // Crear la página en el documento PDF
            val pageInfo = PdfDocument.PageInfo.Builder(scaledBitmap.width, scaledBitmap.height, 1).create()
            val page = pdfDocument.startPage(pageInfo)

            // Obtener el canvas de la página y dibujar el bitmap redimensionado
            val canvas: Canvas = page.canvas
            canvas.drawBitmap(scaledBitmap, 0f, 0f, null)

            // Terminar la página
            pdfDocument.finishPage(page)

            // Usar el Storage Access Framework para permitir al usuario elegir dónde guardar el archivo
            val createDocumentIntent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                type = "application/pdf"
                putExtra(Intent.EXTRA_TITLE, "Carnet de socio - Club deportivo.pdf")
            }

            // Lanzar el Intent para seleccionar la ubicación
            createFileLauncher.launch(createDocumentIntent)
        }


        btnImprimir.setOnClickListener {
            saveCardViewAsPdf(cardViewToSave)
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