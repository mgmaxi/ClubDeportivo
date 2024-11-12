package com.example.myclubdeportivo.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.TextView
import com.example.myclubdeportivo.R
import com.example.myclubdeportivo.model.Course

class CourseAdapter(context: Context, private val courses: List<Course>) : ArrayAdapter<Course>(context, R.layout.list_item_course, courses) {

    private val selectedCourses = BooleanArray(courses.size)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = LayoutInflater.from(context).inflate(R.layout.list_item_course, parent, false)

        val course = courses[position]

        val checkBox = view.findViewById<CheckBox>(R.id.checkBoxCourse)
        val courseName = view.findViewById<TextView>(R.id.textViewCourseName)
        val coursePrice = view.findViewById<TextView>(R.id.textViewCoursePrice)

        courseName.text = course.name
        coursePrice.text = " $ ${course.price}"

        checkBox.isChecked = selectedCourses[position]

        checkBox.setOnCheckedChangeListener { _, isChecked ->
            selectedCourses[position] = isChecked
        }

        return view
    }

    fun getSelectedCourses(): List<Course> {
        return courses.filterIndexed { index, _ -> selectedCourses[index] }
    }
}
