package com.example.wavesoffood

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class UsersDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "foodies.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "allusers"
        private const val  COLUMN_ID = "id"
        private const val  COLUMN_NAME = "name"
        private const val  COLUMN_MOBILE = "mobile"
        private const val  COLUMN_ADDRESS = "address"
        private const val  COLUMN_EMAIL = "email"
        private const val  COLUMN_PASSWORD = "password"


    }

//    oncreate and onupgread inbuilt chhe je implement karva padse , red line per hover thi

    override fun onCreate(db: SQLiteDatabase?) {

        val createTableQuery = "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY,$COLUMN_NAME TEXT, $COLUMN_MOBILE TEXT, $COLUMN_ADDRESS TEXT, $COLUMN_EMAIL TEXT, $COLUMN_PASSWORD TEXT)"
        db?.execSQL(createTableQuery)

    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTableQuery)
        onCreate(db)
    }

    fun insertUser(user:Users){
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME,user.name)
            put(COLUMN_MOBILE,user.mobile)
            put(COLUMN_ADDRESS,user.address)
            put(COLUMN_EMAIL,user.email)
            put(COLUMN_PASSWORD,user.password)
        }
        db.insert(TABLE_NAME,null,values)
        db.close()

    }

    fun getAllUsers(): List<Users>{
        val userList = mutableListOf<Users>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query,null)

        while (cursor.moveToNext()){
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
            val mobile = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MOBILE))
            val address = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS))
            val email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL))
            val password = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD))

            val  user = Users(id, name, mobile, address, email, password)
            userList.add(user)
        }
        cursor.close()
        db.close()
        return userList
    }

    fun getUserByEmailAndPassword(email: String, password: String): Users? {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE email = ? AND password = ?"
        val cursor = db.rawQuery(query, arrayOf(email, password))

        return if (cursor.moveToFirst()) {
            val user = Users(
                cursor.getInt(0),  // ID
                cursor.getString(1),  // Name
                cursor.getString(2),  // Mobile
                cursor.getString(3),  // Address
                cursor.getString(4),  // Email
                cursor.getString(5)   // Password
            )
            cursor.close()
            user
        } else {
            cursor.close()
            null
        }
    }

    fun updateUser(user:Users){
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME,user.name)
            put(COLUMN_MOBILE,user.mobile)
            put(COLUMN_ADDRESS,user.address)
            put(COLUMN_EMAIL,user.email)
            put(COLUMN_PASSWORD,user.password)
        }
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(user.id.toString())
        db.update(TABLE_NAME,values,whereClause,whereArgs)
        db.close()
    }

    fun getUserById(userId : Int):Users{
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = $userId"
        val cursor = db.rawQuery(query,null)
        cursor.moveToFirst()

        val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
        val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
        val mobile = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MOBILE))
        val address = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS))
        val email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS))
        val password = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD))

        cursor.close()
        db.close()
        return Users(id, name, mobile, address, email, password)
    }



}