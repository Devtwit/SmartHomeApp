import Data.ResponseData
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.example.bthome.fragments.AddBleDeviceFragment.Companion.apkContext
import java.io.ByteArrayOutputStream

class DatabaseHelper(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val createLocationTable =
            "CREATE TABLE $TABLE_LOCATION ($COLUMN_LOCATION_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMN_LOCATION_NAME TEXT, $COLUMN_LOCATION_ADDRESS TEXT UNIQUE, $COLUMN_IMAGE BLOB)"

        val createDeviceTable =
            "CREATE TABLE $TABLE_DEVICE ($COLUMN_DEVICE_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMN_LOCATION_ID INTEGER, $COLUMN_DEVICE_NAME TEXT, " +
                    "$COLUMN_STATUS TEXT, $COLUMN_ACK TEXT, $COLUMN_ERROR_MESSAGE TEXT, " +
                    "FOREIGN KEY($COLUMN_LOCATION_ID) REFERENCES $TABLE_LOCATION($COLUMN_LOCATION_ID))"

        db.execSQL(createLocationTable)
        db.execSQL(createDeviceTable)
    }

    fun insertDataWithImage(location: String, devices: Map<String, Map<String, Any>>, imageBitmap: Bitmap) {
        val db = this.writableDatabase
        db.beginTransaction()
        try {
            // Insert or retrieve the location ID
            val locationData = location.split(" ")
            val name = locationData[0]
            val address = locationData[0]

            // Convert the Bitmap image to a byte array
            val imageByteArray = convertBitmapToByteArray(imageBitmap)

            // Insert or retrieve the location ID
            var customName = getLocationNameByAddress(address)

            if (customName.isNullOrBlank()) {
                customName = "BT-Beacon_room1"
            }
            val locationId = insertLocationWithImage(db, customName!!, address, imageByteArray)

            // Insert devices for the location
            for ((deviceName, deviceData) in devices) {
                val status = deviceData["status"] as? String
                val ack = deviceData["ack"] as? String
                val errorMessage = deviceData["err_msg"] as? String

                val contentValues = ContentValues()
                contentValues.put(COLUMN_LOCATION_ID, locationId)
                contentValues.put(COLUMN_DEVICE_NAME, deviceName)
                contentValues.put(COLUMN_STATUS, status)
                contentValues.put(COLUMN_ACK, ack)
                contentValues.put(COLUMN_ERROR_MESSAGE, errorMessage)

                db.insert(TABLE_DEVICE, null, contentValues)
            }

            db.setTransactionSuccessful()
        } catch (e: Exception) {
            Log.e(TAG, "Error inserting data.", e)
        } finally {
            db.endTransaction()
            db.close()
        }
    }
    private fun convertBitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    private fun insertLocationWithImage(db: SQLiteDatabase, name: String, address: String, image: ByteArray): Long {
        val contentValues = ContentValues()
        contentValues.put(COLUMN_LOCATION_NAME, name)
        contentValues.put(COLUMN_LOCATION_ADDRESS, address)
        contentValues.put(COLUMN_IMAGE, image)

        val existingLocationId = db.query(
            TABLE_LOCATION,
            arrayOf(COLUMN_LOCATION_ID),
            "$COLUMN_LOCATION_NAME=? AND $COLUMN_LOCATION_ADDRESS=?",
            arrayOf(name, address),
            null,
            null,
            null
        )

        return if (existingLocationId.moveToFirst()) {
            // Location already exists, return its ID
            val locationId = existingLocationId.getLong(existingLocationId.getColumnIndex(COLUMN_LOCATION_ID))
            existingLocationId.close()
            locationId
        } else {
            // Location doesn't exist, insert it and return the new ID
            val newLocationId = db.insert(TABLE_LOCATION, null, contentValues)
            existingLocationId.close()
            newLocationId
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_DEVICE")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_LOCATION")
        onCreate(db)
    }

    fun insertData(location: String, devices: Map<String, Map<String, Any>>) {
        val db = this.writableDatabase
        db.beginTransaction()
        try {
            // Insert or retrieve the location ID
//            val locationId = insertLocation(db, location)
            Log.d(TAG,"$location")
            val locationData = location.split(" ")
            val name = locationData[0]
            val address = locationData[0]
            Log.d(TAG," : location $location name :  $name  address : $address" )
            // Insert or retrieve the location ID
            var customName= getLocationNameByAddress(address)
            Log.d(TAG," : location $location name :  $name  address : $address customName : $customName"  )
            if(customName.isNullOrBlank()){
                customName="BT-Beacon_room1"
            }
            val locationId = insertLocation(db, customName!!, address)
            // Insert devices for the location
            for ((deviceName, deviceData) in devices) {
                val status = deviceData["status"] as? String
                val ack = deviceData["ack"] as? String
                val errorMessage = deviceData["err_msg"] as? String

                val contentValues = ContentValues()
                contentValues.put(COLUMN_LOCATION_ID, locationId)
                contentValues.put(COLUMN_DEVICE_NAME, deviceName)
                contentValues.put(COLUMN_STATUS, status)
                contentValues.put(COLUMN_ACK, ack)
                contentValues.put(COLUMN_ERROR_MESSAGE, errorMessage)

                db.insert(TABLE_DEVICE, null, contentValues)
            }

            db.setTransactionSuccessful()
        } catch (e: Exception) {
            Log.e(TAG, "Error inserting data.", e)
        } finally {
            db.endTransaction()
            db.close()
        }
    }
    fun getLocationNameByAddress(address: String): String? {
        val db = this.readableDatabase
        val query = "SELECT $COLUMN_LOCATION_NAME FROM $TABLE_LOCATION WHERE $COLUMN_LOCATION_ADDRESS = ?"
        val selectionArgs = arrayOf(address)
        var locationName: String? = null

        val cursor = db.rawQuery(query, selectionArgs)

        if (cursor.moveToFirst()) {
            locationName = cursor.getString(cursor.getColumnIndex(COLUMN_LOCATION_NAME))
        }

//        cursor.close()
//        db.close()

        return locationName
    }

    @SuppressLint("Range")
    fun getAllResponseData(): List<ResponseData> {
        val responseDataList = mutableListOf<ResponseData>()

        val selectQuery = "SELECT * FROM $TABLE_LOCATION"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        cursor.use {
            while (cursor.moveToNext()) {
                val locationId = cursor.getLong(cursor.getColumnIndex(COLUMN_LOCATION_ID))
                val locationName = cursor.getString(cursor.getColumnIndex(COLUMN_LOCATION_NAME))
                val locationAddress = cursor.getString(cursor.getColumnIndex(COLUMN_LOCATION_ADDRESS))
              Log.d(TAG,locationName)
              Log.d(TAG,locationAddress)
                val devices = getDevicesForLocation(db, locationId)

                val responseData = ResponseData(locationName, devices,locationAddress)
                responseDataList.add(responseData)
            }
        }

        return responseDataList
    }
    fun updateLocationImageByAddress(address: String, imageResource: Int): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        val imageBitmap = BitmapFactory.decodeResource(apkContext.resources, imageResource)
        contentValues.put(COLUMN_IMAGE, convertBitmapToByteArray(imageBitmap))

        val updatedRows = db.update(TABLE_LOCATION, contentValues, "$COLUMN_LOCATION_ADDRESS=?", arrayOf(address))

//        db.close()

        return updatedRows > 0
    }
    fun isImageNullOrMissing(address: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT $COLUMN_IMAGE FROM $TABLE_LOCATION WHERE $COLUMN_LOCATION_ADDRESS = ?"
        val selectionArgs = arrayOf(address)

        val cursor = db.rawQuery(query, selectionArgs)
        val isNullOrMissing = !cursor.moveToFirst() || cursor.isNull(cursor.getColumnIndex(COLUMN_IMAGE))

//        cursor.close()
//        db.close()

        return isNullOrMissing
    }


    @SuppressLint("Range")
    private fun getDevicesForLocation(db: SQLiteDatabase, locationId: Long): Map<String, Map<String, Any>> {
        val devices = mutableMapOf<String, Map<String, Any>>()

        val selectQuery = "SELECT * FROM $TABLE_DEVICE WHERE $COLUMN_LOCATION_ID=$locationId"
        val cursor = db.rawQuery(selectQuery, null)

        cursor.use {
            while (cursor.moveToNext()) {
                val deviceName = cursor.getString(cursor.getColumnIndex(COLUMN_DEVICE_NAME))
                val status = cursor.getString(cursor.getColumnIndex(COLUMN_STATUS))
                val ack = cursor.getString(cursor.getColumnIndex(COLUMN_ACK))
                val errorMessage = cursor.getString(cursor.getColumnIndex(COLUMN_ERROR_MESSAGE))

                val deviceData = mapOf(
                    "status" to status,
                    "ack" to ack,
                    "err_msg" to errorMessage
                )

                devices[deviceName] = deviceData
            }
        }

        return devices
    }

    @SuppressLint("Range")
    private fun insertLocation(db: SQLiteDatabase, name: String, address: String): Long {

        val contentValues = ContentValues()
        contentValues.put(COLUMN_LOCATION_NAME, name)
        contentValues.put(COLUMN_LOCATION_ADDRESS, address)

        val existingLocationId = db.query(
            TABLE_LOCATION,
            arrayOf(COLUMN_LOCATION_ID),
            "$COLUMN_LOCATION_NAME=? AND $COLUMN_LOCATION_ADDRESS=?",
            arrayOf(name, address),
            null,
            null,
            null
        )

        return if (existingLocationId.moveToFirst()) {
            // Location already exists, return its ID
            val locationId = existingLocationId.getLong(existingLocationId.getColumnIndex(COLUMN_LOCATION_ID))
            existingLocationId.close()
            locationId
        } else {
            // Location doesn't exist, insert it and return the new ID
            val newLocationId = db.insert(TABLE_LOCATION, null, contentValues)
            existingLocationId.close()
            newLocationId
        }
    }



    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "BT_HOME.db"
        private const val TABLE_LOCATION = "BT_LOCATION"
        private const val TABLE_DEVICE = "BT_DEVICE"
        private const val COLUMN_LOCATION_ID = "location_id"
        private const val COLUMN_LOCATION_NAME = "location_name"
        private const val COLUMN_LOCATION_ADDRESS = "location_address"
        private const val COLUMN_DEVICE_ID = "device_id"
        private const val COLUMN_DEVICE_NAME = "device_name"
        private const val COLUMN_STATUS = "status"
        private const val COLUMN_ACK = "ack"
        private const val COLUMN_ERROR_MESSAGE = "error_message"
        private val COLUMN_IMAGE = "image"
        val TAG = DatabaseHelper::class.java.simpleName
    }
    fun updateLocationName(oldName: String, newName: String): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_LOCATION_NAME, newName)
        Log.d(TAG,oldName)
        val updatedRows = db.update(TABLE_LOCATION, contentValues, "$COLUMN_LOCATION_ADDRESS=?", arrayOf(oldName))
//        db.close()

        return updatedRows > 0
    }

    fun updateLocationImage(address: String, newImageBitmap: Bitmap): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_IMAGE, convertBitmapToByteArray(newImageBitmap))

        val updatedRows = db.update(TABLE_LOCATION, contentValues, "$COLUMN_LOCATION_ADDRESS=?", arrayOf(address))

        db.close()

        return updatedRows > 0
    }

    fun getImageByAddress(address: String): Bitmap? {
        val db = this.readableDatabase
        val query = "SELECT $COLUMN_IMAGE FROM $TABLE_LOCATION WHERE $COLUMN_LOCATION_ADDRESS = ?"
        val selectionArgs = arrayOf(address)
        var imageBitmap: Bitmap? = null

        val cursor = db.rawQuery(query, selectionArgs)

        if (cursor.moveToFirst()) {
            val imageByteArray = cursor.getBlob(cursor.getColumnIndex(COLUMN_IMAGE))
            if (imageByteArray.equals(null)) {
                imageBitmap = null
            } else {
                imageBitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.size)
            }
        }
//        cursor.close()
//        db.close()

        return imageBitmap
    }


    fun deleteLocation(oldName: String): Boolean {
        val db = this.writableDatabase
        val deletedRows = db.delete(TABLE_LOCATION, "$COLUMN_LOCATION_ADDRESS=?", arrayOf(oldName))
//        db.close()

        return deletedRows > 0
    }
}
