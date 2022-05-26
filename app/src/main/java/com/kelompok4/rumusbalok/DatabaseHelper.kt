package com.kelompok4.rumusbalok

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build.ID
import android.util.Log
import com.kelompok4.rumusbalok.model.Tasks

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE = "CREATE TABLE $TABLE_NAME ($ID_DB INTEGER PRIMARY KEY,$HEIGHT TEXT, $WIDTH TEXT,$TALL TEXT, $COMPLETED TEXT);"
        db?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(DROP_TABLE)
        onCreate(db)
    }
    @SuppressLint("Range")
    fun getTask(_id: Int): Tasks {
        val tasks = Tasks()
        val db = writableDatabase
        val selectQuery = "SELECT  * FROM $TABLE_NAME WHERE $ID = $_id"
        val cursor = db.rawQuery(selectQuery, null)

        cursor?.moveToFirst()
        tasks.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID_DB)))
        tasks.height = cursor.getString(cursor.getColumnIndex(HEIGHT))
        tasks.width = cursor.getString(cursor.getColumnIndex(WIDTH))
        tasks.tall = cursor.getString(cursor.getColumnIndex(TALL))
        tasks.completed = cursor.getString(cursor.getColumnIndex(COMPLETED))
        cursor.close()
        return tasks
    }
    fun addTask(tasks: Tasks): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(HEIGHT, tasks.height)
        values.put(WIDTH, tasks.width)
        values.put(TALL,tasks.tall)
        values.put(COMPLETED, tasks.completed)
        val successUpload = db.insert(TABLE_NAME, null, values)
        db.close()
        Log.v("Inserted", "$successUpload")
        return (Integer.parseInt("$successUpload") != -1)
    }

    companion object {
        private val DB_VERSION = 1
        private val DB_NAME = "data_nilai"
        private val TABLE_NAME = "table_nilai"
        private val ID_DB = "id"
        private val HEIGHT = "Panjang"
        private val WIDTH = "Lebar"
        private val TALL = "Tinggi"
        private val COMPLETED = "Hasil"
    }
}