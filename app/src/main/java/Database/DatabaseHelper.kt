package Database

import Data.ResponseData
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "response_database.db1"
        private const val DATABASE_VERSION = 1

        private const val TABLE_NAME = "response_table"
        private const val COLUMN_ID = "_id"
        private const val COLUMN_TOPIC = "topic"
        private const val COLUMN_MESSAGE = "message"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = "CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_TOPIC TEXT, " +
                "$COLUMN_MESSAGE TEXT UNIQUE" +
                ");"
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // Inside your DatabaseHelper class

    @SuppressLint("Range")
    fun getAllResponseData(): List<ResponseData> {
        val responseDataList = mutableListOf<ResponseData>()
        val db = readableDatabase
        val cursor = db.query(
            "response_table",
            arrayOf("topic", "message"),
            null,
            null,
            null,
            null,
            null
        )
        while (cursor.moveToNext()) {
            val topic = cursor.getString(cursor.getColumnIndex("topic"))
            val message = cursor.getString(cursor.getColumnIndex("message"))
            val responseData = ResponseData(topic, message)
            responseDataList.add(responseData)
        }
        cursor.close()
        return responseDataList
    }
    fun updateResponseData(message: String,responseData: ResponseData) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TOPIC, responseData.topic)
            put(COLUMN_MESSAGE, responseData.message)
        }
        db.update(TABLE_NAME, values, "$COLUMN_MESSAGE = ?", arrayOf(message))
    }

    fun deleteResponseData(message: String) {
        val db = writableDatabase
//        db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.delete(TABLE_NAME, "$COLUMN_MESSAGE = ?", arrayOf(message))
    }
}
