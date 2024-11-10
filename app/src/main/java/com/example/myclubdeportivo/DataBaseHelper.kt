package com.example.myclubdeportivo

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.myclubdeportivo.model.Member

class DataBaseHelper (context: Context):
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION)
{

      companion object {
        // Config DB
        private val DATABASE_NAME = "ClubDeportivo.db"
        private val DATABASE_VERSION = 4

        // User table
        private val TABLE_USERS = "Users"
        private val COLUMN_ID = "id"
        private val COLUMN_USERNAME = "username"
        private val COLUMN_PASSWORD = "password"

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
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createUsersTable = ("CREATE TABLE $TABLE_USERS ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$COLUMN_USERNAME TEXT, "
                + "$COLUMN_PASSWORD TEXT)")

        val createMembersTable = ("CREATE TABLE $TABLE_MEMBERS (" +
                "$MEMBER_COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$MEMBER_COLUMN_FIRST_NAME TEXT NOT NULL," +
                "$MEMBER_COLUMN_LAST_NAME TEXT NOT NULL," +
                "$MEMBER_COLUMN_DOCUMENT_TYPE TEXT NOT NULL," +
                "$MEMBER_COLUMN_DOCUMENT_NUMBER TEXT NOT NULL," +
                "$MEMBER_COLUMN_ADDRESS TEXT NOT NULL," +
                "$MEMBER_COLUMN_PHONE TEXT NOT NULL," +
                "$MEMBER_COLUMN_IS_MEMBER TEXT NOT NULL)")

        db.execSQL(createUsersTable)
        db.execSQL(createMembersTable)

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_MEMBERS")
        onCreate(db)
    }

    fun registerUser(username: String, password: String): Long {
        if (!userExist(username, password)){
            val db = this.writableDatabase
            val contentValues = ContentValues()
            contentValues.put(COLUMN_USERNAME, username)
            contentValues.put(COLUMN_PASSWORD, password)

            val success = db.insert(TABLE_USERS, null, contentValues)
            db.close()
            return success
        } else {
            return -1
        }
    }

    fun userExist(username: String, password: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE $COLUMN_USERNAME = ? AND $COLUMN_PASSWORD = ?"
        val cursor = db.rawQuery(query, arrayOf(username, password))
        val validateUser = cursor.count > 0
        cursor.close()
        db.close()
        return validateUser
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

}

