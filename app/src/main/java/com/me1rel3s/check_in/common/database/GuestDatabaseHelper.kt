import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class GuestDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 2 // Atualizando para a versão 2
        private const val DATABASE_NAME = "guest.db"
        const val TABLE_GUESTS = "guests"
        const val COLUMN_ID = "_id"
        const val COLUMN_NAME = "name"
        const val COLUMN_PHONE = "phone"
        const val COLUMN_EMAIL = "email"
        const val COLUMN_PHOTO = "photo"
        const val COLUMN_QR_CODE = "qr_code"
        const val COLUMN_STATUS = "status"  // Nova coluna para status
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = ("CREATE TABLE $TABLE_GUESTS ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$COLUMN_NAME TEXT,"
                + "$COLUMN_PHONE TEXT,"
                + "$COLUMN_EMAIL TEXT,"
                + "$COLUMN_PHOTO BLOB,"
                + "$COLUMN_QR_CODE BLOB,"
                + "$COLUMN_STATUS TEXT DEFAULT 'not_checked'" + ")")  // Coluna status com valor padrão
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE $TABLE_GUESTS ADD COLUMN $COLUMN_STATUS TEXT DEFAULT 'not_checked'")
        }
    }
}