package com.example.myclubdeportivo

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.myclubdeportivo.model.Course
import com.example.myclubdeportivo.model.Payment

class DataBaseHelper (context: Context):
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION)
{

      companion object {
        // Config DB
        private val DATABASE_NAME = "ClubDeportivo.db"
        private val DATABASE_VERSION = 6

        // User table
        private val TABLE_USERS = "Users"
        private val USERS_COLUMN_ID = "id"
        private val USERS_COLUMN_USERNAME = "username"
        private val USERS_COLUMN_PASSWORD = "password"

        // Memeber table
        private val TABLE_MEMBERS = "Members"
        private val MEMBER_COLUMN_ID = "id"
        private val MEMBER_COLUMN_FIRST_NAME = "first_name"
        private val MEMBER_COLUMN_LAST_NAME = "last_name"
        private val MEMBER_COLUMN_DOCUMENT_TYPE = "document_type"
        private val MEMBER_COLUMN_DOCUMENT_NUMBER = "document_number"
        private val MEMBER_COLUMN_ADDRESS = "address"
        private val MEMBER_COLUMN_PHONE = "phone"
        private val MEMBER_COLUMN_IS_MEMBER = "is_member"

        // Payment table
        private val TABLE_PAYMENTS = "Payments"
        private val PAYMENT_COLUMN_ID = "id"
        private val PAYMENT_COLUMN_MEMBER_ID = "member_id"
        private val PAYMENT_COLUMN_AMOUNT = "amount"
        private val PAYMENT_COLUMN_DATE = "date"
        private val PAYMENT_COLUMN_PAYMENT_METHOD = "payment_method"
        private val PAYMENT_COLUMN_INSTALLMENTS = "installments"

        // Course table
        private const val TABLE_COURSES = "Courses"
        private const val COURSE_COLUMN_ID = "id"
        private const val COURSE_COLUMN_NAME = "name"
        private const val COURSE_COLUMN_PRICE = "price"

        // Member-Course relationship table
        private const val TABLE_MEMBER_COURSES = "MemberCourses"
        private const val MEMBER_COURSE_COLUMN_MEMBER_ID = "member_id"
        private const val MEMBER_COURSE_COLUMN_COURSE_ID = "course_id"
        private const val MEMBER_COURSE_COLUMN_IS_PAID = "is_paid"

    }

    override fun onCreate(db: SQLiteDatabase) {
        val createUsersTable = ("CREATE TABLE $TABLE_USERS ("
                + "$USERS_COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$USERS_COLUMN_USERNAME TEXT, "
                + "$USERS_COLUMN_PASSWORD TEXT)")

        val createMembersTable = ("CREATE TABLE $TABLE_MEMBERS (" +
                "$MEMBER_COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$MEMBER_COLUMN_FIRST_NAME TEXT NOT NULL," +
                "$MEMBER_COLUMN_LAST_NAME TEXT NOT NULL," +
                "$MEMBER_COLUMN_DOCUMENT_TYPE TEXT NOT NULL," +
                "$MEMBER_COLUMN_DOCUMENT_NUMBER TEXT NOT NULL," +
                "$MEMBER_COLUMN_ADDRESS TEXT NOT NULL," +
                "$MEMBER_COLUMN_PHONE TEXT NOT NULL," +
                "$MEMBER_COLUMN_IS_MEMBER TEXT NOT NULL)")


        val createPaymentsTable = ("CREATE TABLE $TABLE_PAYMENTS (" +
                "$PAYMENT_COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$PAYMENT_COLUMN_MEMBER_ID TEXT NOT NULL," +
                "$PAYMENT_COLUMN_AMOUNT TEXT NOT NULL," +
                "$PAYMENT_COLUMN_DATE TEXT NOT NULL," +
                "$PAYMENT_COLUMN_PAYMENT_METHOD TEXT NOT NULL," +
                "$PAYMENT_COLUMN_INSTALLMENTS TEXT NOT NULL)")
        val createCoursesTable = ("CREATE TABLE $TABLE_COURSES ($COURSE_COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                " $COURSE_COLUMN_NAME TEXT NOT NULL, $COURSE_COLUMN_PRICE REAL NOT NULL)")

        val createMemberCourseTable = ("""
            CREATE TABLE $TABLE_MEMBER_COURSES (
                $MEMBER_COURSE_COLUMN_MEMBER_ID INTEGER NOT NULL,
                $MEMBER_COURSE_COLUMN_COURSE_ID INTEGER NOT NULL,
                $MEMBER_COURSE_COLUMN_IS_PAID INTEGER DEFAULT 0,
                FOREIGN KEY($MEMBER_COURSE_COLUMN_MEMBER_ID) REFERENCES $TABLE_MEMBERS($MEMBER_COLUMN_ID),
                FOREIGN KEY($MEMBER_COURSE_COLUMN_COURSE_ID) REFERENCES $TABLE_COURSES($COURSE_COLUMN_ID)
            )
        """)

        db.execSQL(createUsersTable)
        db.execSQL(createMembersTable)
        db.execSQL(createPaymentsTable)
        db.execSQL(createCoursesTable)
        db.execSQL(createMemberCourseTable)
        insertDefaultCourses(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_MEMBERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_COURSES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_MEMBER_COURSES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PAYMENTS")
        onCreate(db)
    }

    fun registerUser(username: String, password: String): Long {
        if (!userExist(username, password)){
            val db = this.writableDatabase
            val contentValues = ContentValues()
            contentValues.put(USERS_COLUMN_USERNAME, username)
            contentValues.put(USERS_COLUMN_PASSWORD, password)

            val success = db.insert(TABLE_USERS, null, contentValues)
            db.close()
            return success
        } else {
            return -1
        }
    }

    fun userExist(username: String, password: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE $USERS_COLUMN_USERNAME = ? AND $USERS_COLUMN_PASSWORD = ?"
        val cursor = db.rawQuery(query, arrayOf(username, password))
        val validateUser = cursor.count > 0
        cursor.close()
        db.close()
        return validateUser
    }

    fun getMemberIdByDocumentNumber(documentNumber: String): Int? {
        val db = this.readableDatabase
        var memberId: Int? = null

        val query = "SELECT $MEMBER_COLUMN_ID FROM $TABLE_MEMBERS WHERE $MEMBER_COLUMN_DOCUMENT_NUMBER = ?"
        val cursor = db.rawQuery(query, arrayOf(documentNumber))

        if (cursor.moveToFirst()) {
            memberId = cursor.getInt(cursor.getColumnIndexOrThrow(MEMBER_COLUMN_ID))
        }

        cursor.close()
        db.close()

        return memberId
    }




    fun registerMember(firstName: String, lastName: String, documentType: String, documentNumber: String, address: String, phone: String, isMember: Boolean): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("first_name", firstName)
            put("last_name", lastName)
            put("document_type", documentType)
            put("document_number", documentNumber)
            put("address", address)
            put("phone", phone)
            put("is_member", isMember)
        }
        return db.insert("members", null, values)
    }

    fun addPayment(payment: Payment): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put("member_id", payment.memberId)
            put("amount", payment.amount)
            put("date", payment.date)
            put("payment_method", payment.paymentMethod)
            put("installments", payment.installments)
        }
        return db.insert("Payments", null, contentValues).also {
            db.close()
        }
    }

    fun getTotalCoursesAmountByMemberId(memberId: Int?): Double {
        var totalAmount = 0.0
        val db = this.readableDatabase
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery("""
                SELECT SUM(c.$COURSE_COLUMN_PRICE) 
                FROM $TABLE_MEMBER_COURSES mc 
                JOIN $TABLE_COURSES c ON mc.$MEMBER_COURSE_COLUMN_COURSE_ID = c.$COURSE_COLUMN_ID 
                WHERE mc.$MEMBER_COURSE_COLUMN_MEMBER_ID = ?
            """.trimIndent(), arrayOf(memberId.toString()))

            if (cursor.moveToFirst()) {
                totalAmount = cursor.getDouble(0) ?: 0.0
            }
        } finally {
            cursor?.close()
            db.close()
        }

        return totalAmount
    }

    fun getAllMembersAsString(): List<String> {
        val membersList = mutableListOf<String>()
        val db = this.readableDatabase

        val cursor = db.query(
            TABLE_MEMBERS,
            arrayOf(
                MEMBER_COLUMN_ID,
                MEMBER_COLUMN_FIRST_NAME,
                MEMBER_COLUMN_LAST_NAME,
                MEMBER_COLUMN_DOCUMENT_TYPE,
                MEMBER_COLUMN_DOCUMENT_NUMBER,
                MEMBER_COLUMN_ADDRESS,
                MEMBER_COLUMN_PHONE,
                MEMBER_COLUMN_IS_MEMBER
            ),
            null,
            null,
            null,
            null,
            null
        )

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(0)
                val firstName = cursor.getString(1)
                val lastName = cursor.getString(2)
                val documentType = cursor.getString(3)
                val documentNumber = cursor.getString(4)
                val address = cursor.getString(5)
                val phone = cursor.getString(6)
                val isMember = cursor.getString(7)
                val membership = if(isMember == "1"){
                    "Socio."
                } else {
                    "No socio."
                }

                membersList.add("Nombre: $firstName $lastName | Número de documento: $documentNumber | Dirección: $address | Teléfono: $phone | $membership")

            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return membersList
    }

    fun getAllCourses(): List<Course> {
        val coursesList = mutableListOf<Course>()
        val db = this.readableDatabase

        val cursor = db.query(TABLE_COURSES, arrayOf(COURSE_COLUMN_ID, COURSE_COLUMN_NAME, COURSE_COLUMN_PRICE), null, null, null, null, null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex(COURSE_COLUMN_ID))
                val name = cursor.getString(cursor.getColumnIndex(COURSE_COLUMN_NAME))
                val price = cursor.getInt(cursor.getColumnIndex(COURSE_COLUMN_PRICE))
                coursesList.add(Course(id, name, price))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return coursesList
    }

    fun registerMemberCourse(memberId: Int, courseId: Int): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues().apply {
            put(MEMBER_COURSE_COLUMN_MEMBER_ID, memberId)
            put(MEMBER_COURSE_COLUMN_COURSE_ID, courseId)
        }

        return db.insert(TABLE_MEMBER_COURSES, null, contentValues).also { db.close() }
    }

    fun isMemberAlreadyEnrolledInCourse(memberId: Int?, courseId: Int): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_MEMBER_COURSES WHERE $MEMBER_COURSE_COLUMN_MEMBER_ID = ? AND $MEMBER_COURSE_COLUMN_COURSE_ID = ?"
        val cursor = db.rawQuery(query, arrayOf(memberId.toString(), courseId.toString()))

        val isEnrolled = cursor.count > 0
        cursor.close()
        db.close()

        return isEnrolled
    }



    fun markDebtAsPaid(memberId: Int?) {
        val db = this.writableDatabase

        val contentValues = ContentValues().apply {
            put(MEMBER_COURSE_COLUMN_IS_PAID, 1)
        }

        db.update(TABLE_MEMBER_COURSES, contentValues, "$MEMBER_COURSE_COLUMN_MEMBER_ID = ?", arrayOf(memberId.toString()))
        db.close()
    }

    private fun insertDefaultCourses(db: SQLiteDatabase) {
        val courses = listOf(
            Pair("Yoga", 17000),
            Pair("Pilates", 16500),
            Pair("Zumba", 14500),
            Pair("Futbol", 15000),
            Pair("Natación", 20000),
            Pair("Boxeo", 7500)
        )

        for (course in courses) {
            val contentValues = ContentValues().apply {
                put(COURSE_COLUMN_NAME, course.first)
                put(COURSE_COLUMN_PRICE, course.second)
            }
            db.insert(TABLE_COURSES, null, contentValues)
        }
    }








}

