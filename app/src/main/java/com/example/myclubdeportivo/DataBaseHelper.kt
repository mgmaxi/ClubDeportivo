package com.example.myclubdeportivo

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.icu.text.SimpleDateFormat
import com.example.myclubdeportivo.model.Payment
import java.util.Date
import java.util.Locale

class DataBaseHelper (context: Context):
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION)
{

      companion object {
        // Config DB
        private val DATABASE_NAME = "ClubDeportivo.db"
        private val DATABASE_VERSION = 5

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

        db.execSQL(createUsersTable)
        db.execSQL(createMembersTable)
        db.execSQL(createPaymentsTable)

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_MEMBERS")
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

    fun getMemberIdByDocumentNumber(documentNumber: String): Long? {
        val db = this.readableDatabase
        var memberId: Long? = null

        val cursor = db.query(
            TABLE_MEMBERS,
            arrayOf(MEMBER_COLUMN_ID),
            "$MEMBER_COLUMN_DOCUMENT_NUMBER = ?",
            arrayOf(documentNumber),
            null,
            null,
            null
        )

        if (cursor.moveToFirst()) {
            memberId = cursor.getLong(0)
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

    fun getTotalPaymentsByDNI(dni: String): Double {
        var totalAmount = 0.0
        val db = this.readableDatabase
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery("SELECT SUM(amount) FROM payments WHERE dni = ?", arrayOf(dni))
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




}

