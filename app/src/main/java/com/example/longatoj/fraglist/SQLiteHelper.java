package com.example.longatoj.fraglist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Jordan on 10/19/2016.
 */

public class SQLiteHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "items.db";
    public static final int DATABASE_VERSION = 8;
    public static final String TABLE = "ToDoListItems";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_PRIORITY = "priority";
    public static final String COLUMN_DATETOCOMPLETE = "completion_date";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_STATUS = "status";
    public static final String[] ALL_COLUMNS = {COLUMN_ID, COLUMN_DATETOCOMPLETE, COLUMN_PRIORITY, COLUMN_DESCRIPTION,
                                                    COLUMN_STATUS};
    private static final String DATABASE_CREATE = "create table "
            + TABLE + "( _id integer primary key autoincrement, "
            + COLUMN_DESCRIPTION + " text,"
            + COLUMN_PRIORITY + " text,"
            + COLUMN_DATETOCOMPLETE + " date,"
            + COLUMN_STATUS + " integer"
            + ");";

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("SQLHELPER", "Possibly dropping table");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }
}
