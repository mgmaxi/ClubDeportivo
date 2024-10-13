package com.example.proyectoclubdeportivo

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper (context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "vencimientos.db"
        private const val DATABASE_VERSION = 1

        private const val TABLE_NAME = "data_items"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NOMBRE = "nombre"
        private const val COLUMN_DNI = "dni"
        private const val COLUMN_FECHA_VENCIMIENTO = "fecha_vencimiento"
        private const val COLUMN_IMPORTE = "importe"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = ("CREATE TABLE $TABLE_NAME ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$COLUMN_NOMBRE TEXT,"
                + "$COLUMN_DNI TEXT,"
                + "$COLUMN_FECHA_VENCIMIENTO TEXT,"
                + "$COLUMN_IMPORTE REAL"
                + ")")
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}