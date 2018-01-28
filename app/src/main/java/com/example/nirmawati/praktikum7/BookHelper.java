package com.example.nirmawati.praktikum7;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Nirmawati on 11/10/2017.
 */

public class BookHelper extends SQLiteOpenHelper {
    //deklarasi nama database dan versi
    final static String DBNAME ="book.db";
    final static int DBVERSION = 1;

    //membuat konstructor
    public BookHelper(Context context) {
        super(context, DBNAME, null, DBVERSION);
    }

    //oncreate method
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query_create="CREATE TABLE book_entries("
        +"_id INTEGER PRIMARY KEY autoincrement,"+
                "title text,"+
                "author text)";
        sqLiteDatabase.execSQL(query_create);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int il) {
        String query_drop = "DROP TABLE IF EXISTS book_entries";
        sqLiteDatabase.execSQL(query_drop);
        onCreate(sqLiteDatabase);

    }
}
