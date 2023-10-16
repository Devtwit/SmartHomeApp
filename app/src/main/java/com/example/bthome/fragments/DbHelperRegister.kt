package com.example.bthome.fragments

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbHelperRegister(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "UserDatabase.db"
        private const val DATABASE_VERSION = 1

        // Define the table and its columns
        private const val TABLE_NAME = "users"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_PASSWORD = "password"
        private const val COLUMN_PHONE_NUMBER = "phone_number"
        private const val COLUMN_EMAIL = "email"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableSQL = "CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_NAME TEXT," +
                "$COLUMN_PASSWORD TEXT," +
                "$COLUMN_PHONE_NUMBER TEXT UNIQUE," +
                "$COLUMN_EMAIL TEXT UNIQUE)"
        db.execSQL(createTableSQL)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addUser(user: User): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_NAME, user.name)
        values.put(COLUMN_PASSWORD, user.password)
        values.put(COLUMN_PHONE_NUMBER, user.phoneNumber)
        values.put(COLUMN_EMAIL, user.email)
        return db.insert(TABLE_NAME, null, values)
    }


    fun checkUserData(name: String, phoneNumber: String, email: String, password: String): Boolean {
        val db = this.readableDatabase
        val selection = "$COLUMN_NAME = ? AND $COLUMN_PHONE_NUMBER = ? AND $COLUMN_EMAIL = ? AND $COLUMN_PASSWORD = ?"
        val selectionArgs = arrayOf(name, phoneNumber, email, password)

        val cursor = db.query(
            TABLE_NAME,
            arrayOf(COLUMN_ID),
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        val userExists = cursor.count > 0
//        cursor.close()
//        db.close()

        return userExists
    }


    fun checkUserData(ds: String, password: String): Boolean {
        val db = this.readableDatabase
        val selection = "($COLUMN_NAME = ? OR $COLUMN_PHONE_NUMBER = ? OR $COLUMN_EMAIL = ?) AND $COLUMN_PASSWORD = ?"
        val selectionArgs = arrayOf(ds, ds, ds, password)

        val cursor = db.query(
            TABLE_NAME,
            arrayOf(COLUMN_ID),
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        val userExists = cursor.count > 0
//        cursor.close()
//        db.close()

        return userExists
    }
}
