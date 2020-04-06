package com.will.bluetoothprinterdemo.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.will.bluetoothprinterdemo.vo.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductSqliteUtil {
    private static final String TAG = "OrderProductDatabaseUtil";

    /**
     * Database Name
     */
    private static final String DATABASE_NAME = "order_product_data";

    /**
     * Database Version
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Table Name
     */
    private static final String DATABASE_TABLE = "tb_orderproduct";

    /**
     * Table columns
     */
    public static final String KEY_ID = "prodID";
    public static final String KEY_ORDERID = "orderID";
    public static final String KEY_NAME = "pName";
    public static final String KEY_COLOR = "pColor";
    public static final String KEY_NUMBERS = "pNum";
    public static final String KEY_PRICE = "price";

    /**
     * Database creation sql statement
     */
    private static final String CREATE_TABLE =
            "create table " + DATABASE_TABLE + " ("
                    + KEY_ID + " integer primary key autoincrement, "
                    + KEY_ORDERID + " text not null, "
                    + KEY_NAME + " text, "
                    + KEY_COLOR + " text, "
                    + KEY_NUMBERS + " integer, "
                    + KEY_PRICE + " real);";
    /**
     * Context
     */
    private final Context mCtx;
    private ProductSqliteUtil.DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

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
        }

        /**
         * onUpgrade method is called when database version changes.
         */
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion);
        }
    }

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     *
     * @param ctx the Context within which to work
     */
    public ProductSqliteUtil(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * This method is used for creating/opening connection
     *
     * @return instance of DatabaseUtil
     * @throws SQLException
     */
    public ProductSqliteUtil open() throws SQLException {
        mDbHelper = new ProductSqliteUtil.DatabaseHelper(mCtx);
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
     * @param orderID
     * @param name
     * @param color
     * @param number
     * @param price
     * @return
     */
    public long insert(String orderID, String name, String color, int number, double price) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ORDERID, orderID);
        initialValues.put(KEY_NAME, name);
        initialValues.put(KEY_COLOR, color);
        initialValues.put(KEY_NUMBERS, number);
        initialValues.put(KEY_PRICE, price);
        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    /**
     * This method will delete record.
     *
     * @param
     * @return boolean
     */
    public boolean delete(int productID) {
        return mDb.delete(DATABASE_TABLE, KEY_ID + "=" + productID, null) > 0;
    }

    /**
     * This method will deleteAll record.
     *
     * @return
     */
    public boolean deleteAll() {
        return mDb.delete(DATABASE_TABLE, " 1 ", null) > 0;
    }

    /**
     * This method will return Cursor holding all the records.
     *
     * @return Cursor
     */
    public Cursor fetchAll() {
        return mDb.query(DATABASE_TABLE, new String[]{KEY_ID, KEY_ORDERID, KEY_NAME, KEY_COLOR, KEY_NUMBERS, KEY_PRICE
        }, null, null, null, null, null);
    }

    /**
     * This method will return Cursor holding the specific record.
     *
     * @param productID
     * @return Cursor
     * @throws SQLException
     */
    public Cursor fetch(int productID) throws SQLException {
        Cursor mCursor =
                mDb.query(true, DATABASE_TABLE, new String[]{KEY_ID, KEY_ORDERID, KEY_NAME, KEY_COLOR, KEY_NUMBERS, KEY_PRICE}, KEY_ID + "=" + productID, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public List<Product> fetchByOrderID(String orderID) throws SQLException {
        Cursor mCursor =
                mDb.query(true, DATABASE_TABLE, new String[]{KEY_ID, KEY_ORDERID, KEY_NAME, KEY_COLOR, KEY_NUMBERS, KEY_PRICE}, KEY_ORDERID + "=" + orderID, null,
                        null, null, null, null);
        List<Product> products = new ArrayList<>();
        while (mCursor.moveToNext()) {
            Product product = new Product();
            product.setOrderId(mCursor.getString(1));
            product.setName(mCursor.getString(2));
            product.setColor(mCursor.getString(3));
            product.setNumbers(mCursor.getInt(4));
            product.setPrice(mCursor.getDouble(5));
            products.add(product);
        }
        return products;
    }

    /**
     * This method will update record.
     *
     * @return boolean
     */
    public boolean update(int productID, String orderID, String name, String color, int number, double price) {
        ContentValues args = new ContentValues();
        args.put(KEY_NAME, name);
        args.put(KEY_COLOR, color);
        args.put(KEY_NUMBERS, number);
        args.put(KEY_PRICE, price);
        return mDb.update(DATABASE_TABLE, args, KEY_ID + "=" + productID, null) > 0;
    }
}
