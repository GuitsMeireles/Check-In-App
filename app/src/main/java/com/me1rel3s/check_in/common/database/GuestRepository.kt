package com.me1rel3s.check_in.common.database

import GuestDatabaseHelper
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream

class GuestRepository(context: Context) {

    private val dbHelper = GuestDatabaseHelper(context)

    data class Guest(
        val id: Int,
        val name: String,
        val phone: String,
        val email: String,
        val photo: Bitmap,
        val qrCode: Bitmap,
        val status: String
    )

    fun addGuest(name: String, phone: String, email: String, photo: Bitmap, qrCode: Bitmap, status: String = "not_checked") {
        val db: SQLiteDatabase = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put(GuestDatabaseHelper.COLUMN_NAME, name)
            put(GuestDatabaseHelper.COLUMN_PHONE, phone)
            put(GuestDatabaseHelper.COLUMN_EMAIL, email)
            put(GuestDatabaseHelper.COLUMN_PHOTO, bitmapToByteArray(photo))
            put(GuestDatabaseHelper.COLUMN_QR_CODE, bitmapToByteArray(qrCode))
            put(GuestDatabaseHelper.COLUMN_STATUS, status)
        }

        db.insert(GuestDatabaseHelper.TABLE_GUESTS, null, values)
        db.close()
    }

    fun getAllGuests(): List<Guest> {
        val db: SQLiteDatabase = dbHelper.readableDatabase
        val cursor: Cursor = db.query(
            GuestDatabaseHelper.TABLE_GUESTS,
            null, null, null, null, null, null
        )

        val guests = mutableListOf<Guest>()

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(GuestDatabaseHelper.COLUMN_ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(GuestDatabaseHelper.COLUMN_NAME))
                val phone = cursor.getString(cursor.getColumnIndexOrThrow(GuestDatabaseHelper.COLUMN_PHONE))
                val email = cursor.getString(cursor.getColumnIndexOrThrow(GuestDatabaseHelper.COLUMN_EMAIL))
                val photoBytes = cursor.getBlob(cursor.getColumnIndexOrThrow(GuestDatabaseHelper.COLUMN_PHOTO))
                val qrCodeBytes = cursor.getBlob(cursor.getColumnIndexOrThrow(GuestDatabaseHelper.COLUMN_QR_CODE))
                val status = cursor.getString(cursor.getColumnIndexOrThrow(GuestDatabaseHelper.COLUMN_STATUS))

                val photo = byteArrayToBitmap(photoBytes)
                val qrCode = byteArrayToBitmap(qrCodeBytes)

                guests.add(Guest(id, name, phone, email, photo, qrCode, status))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return guests
    }

    fun findGuestByQrCode(qrCode: String): Int? {
        val db: SQLiteDatabase = dbHelper.readableDatabase
        val cursor: Cursor = db.query(
            GuestDatabaseHelper.TABLE_GUESTS,
            arrayOf(GuestDatabaseHelper.COLUMN_ID),
            "${GuestDatabaseHelper.COLUMN_QR_CODE} = ?",
            arrayOf(qrCode),  // Aqui seria onde você compara o valor do QR Code
            null, null, null
        )

        var guestId: Int? = null
        if (cursor.moveToFirst()) {
            guestId = cursor.getInt(cursor.getColumnIndexOrThrow(GuestDatabaseHelper.COLUMN_ID))
        }

        cursor.close()
        db.close()

        return guestId
    }

    fun checkInGuest(id: Int) {
        val db: SQLiteDatabase = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(GuestDatabaseHelper.COLUMN_STATUS, "checked")
        }
        db.update(GuestDatabaseHelper.TABLE_GUESTS, values, "${GuestDatabaseHelper.COLUMN_ID} = ?", arrayOf(id.toString()))
        db.close()
    }

    private fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    private fun byteArrayToBitmap(bytes: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }
}