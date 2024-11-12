package com.example.myclubdeportivo

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myclubdeportivo.adapters.CourseAdapter
import com.example.myclubdeportivo.model.Course

class ListadoClases : AppCompatActivity() {
    private lateinit var dbHelper: DataBaseHelper
    private lateinit var listViewCourses: ListView
    private lateinit var btnInscribir: Button
    private lateinit var txtNroSocio: EditText
    private lateinit var checkBoxPhysicalFitness: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listado_clases)

        dbHelper = DataBaseHelper(this)
        listViewCourses = findViewById(R.id.lvCoursesList)
        btnInscribir = findViewById(R.id.btnInscriptionCourse)
        txtNroSocio = findViewById(R.id.txtNroSocio)
        checkBoxPhysicalFitness = findViewById(R.id.physicalFitness)

        loadCourses()

        btnInscribir.setOnClickListener {
            validateAndRegister()
        }
    }

    private fun loadCourses() {
        val courses: List<Course> = dbHelper.getAllCourses()

        val adapter = CourseAdapter(this, courses)
        listViewCourses.adapter = adapter
    }

    private fun validateAndRegister() {
        val documentNumber = txtNroSocio.text.toString()

        if (documentNumber.isEmpty()) {
            Toast.makeText(this, "Por favor ingresa el número de documento", Toast.LENGTH_SHORT).show()
            return
        }

        val memberId = dbHelper.getMemberIdByDocumentNumber(documentNumber)

        if (memberId == null) {
            Toast.makeText(this, "No se encontró un socio con ese número de documento $memberId $documentNumber", Toast.LENGTH_SHORT).show()
            return
        }


        if (!checkBoxPhysicalFitness.isChecked) {
            Toast.makeText(this, "Debes confirmar que el apto físico ha sido entregado", Toast.LENGTH_SHORT).show()
            return
        }

        val selectedCourses = (listViewCourses.adapter as CourseAdapter).getSelectedCourses()

        if (selectedCourses.isEmpty()) {
            Toast.makeText(this, "Debes seleccionar al menos una clase para inscribirte", Toast.LENGTH_SHORT).show()
            return
        }

        for (course in selectedCourses) {
            if (!dbHelper.isMemberAlreadyEnrolledInCourse(memberId, course.id)) {
                dbHelper.registerMemberCourse(memberId, course.id)
                Toast.makeText(this, "Inscripción exitosa en la clase ${course.name}", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Ya estás inscrito en la clase ${course.name}", Toast.LENGTH_SHORT).show()
            }
        }

    }
}
