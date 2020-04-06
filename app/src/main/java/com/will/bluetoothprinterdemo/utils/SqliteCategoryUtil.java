package com.will.bluetoothprinterdemo.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.will.bluetoothprinterdemo.vo.Category;

import java.util.ArrayList;
import java.util.List;

public class SqliteCategoryUtil {
    private static final String TAG = "CategoryDatabaseUtil";

    /**
     * Database Name
     */
    private static final String DATABASE_NAME = "data_category";

    /**
     * Database Version
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Table Name
     */
    private static final String DATABASE_TABLE = "tb_category";

    /**
     * Table columns
     */
    public static final String KEY_ID = "cID";
    public static final String KEY_NAME = "cName";



    /**
     * Database creation sql statement
     */
    private static final String CREATE_TABLE =
            "create table " + DATABASE_TABLE + " ("
                    + KEY_ID + " integer primary key autoincrement, "
                    + KEY_NAME + " text not null);";

    /**
     * Context
     */
    private final Context mCtx;
    private SqliteCategoryUtil.DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private final static String[] category = {"枕头", "小米枕","荞麦枕","枕套","床垫","被子","压缩枕","凉枕","夏凉被"};
    /**
     * Inner private class. Database Helper class for creating and updating database.
     */
    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        /**
         * onCreate method is called for the 1st time when database doesn't exists.
         */
        @Override
        public void onCreate(SQLiteDatabase db) {
//            Log.i(TAG, "Creating DataBase: " + CREATE_TABLE);
            db.execSQL(CREATE_TABLE);

            for (int i = 0; i < category.length; i++) {
                db.execSQL("insert into " + DATABASE_TABLE + "(" + KEY_NAME + ") values ('" + new String(category[i]) + "')");
            }
        }

        /**
         * onUpgrade method is called when database version changes.
         */
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
//                    + newVersion);
        }
    }

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     *
     * @param ctx the Context within which to work
     */
    public SqliteCategoryUtil(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * This method is used for creating/opening connection
     *
     * @return instance of DatabaseUtil
     * @throws SQLException
     */
    public SqliteCategoryUtil open() throws SQLException {
        mDbHelper = new SqliteCategoryUtil.DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    /**
     * This method is used for closing the connection.
     */
    public void close() {
        mDbHelper.close();
    }

    /**
     * This method is used to create/insert new record record.
     *
     * @param name
     * @return
     */
    public long insert(String name) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAME, name);
        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    public List<Category> fetchAll() throws SQLException {
        Cursor mCursor =
                mDb.query(true, DATABASE_TABLE, new String[]{KEY_ID, KEY_NAME}, null, null,
                        null, null, null, null);
        List<Category> categories = new ArrayList<>();
        while (mCursor.moveToNext()) {
            Category category = new Category();
            category.setcID(mCursor.getInt(0));
            category.setcName(mCursor.getString(1));
            categories.add(category);
        }
        return categories;
    }
}
