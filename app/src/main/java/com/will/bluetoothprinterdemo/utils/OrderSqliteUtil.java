package com.will.bluetoothprinterdemo.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.will.bluetoothprinterdemo.vo.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * sqlite 数据库操作类
 */
public class OrderSqliteUtil {

    private static final String TAG = "OrderDatabaseUtil";

    /**
     * Database Name
     */
    private static final String DATABASE_NAME = "order_data";

    /**
     * Database Version
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Table Name
     */
    private static final String DATABASE_TABLE = "tb_order";

    /**
     * Table columns
     */
    public static final String KEY_ID = "_id";
    public static final String KEY_ORDERID = "orderID";
    public static final String KEY_CONAME = "consumerName";
    public static final String KEY_CONPHONE = "consumerPhone";
    public static final String KEY_PRONUM = "productNum";
    public static final String KEY_SALARY = "salary";
    public static final String KEY_PAY = "hasPay";
    public static final String KEY_TIME = "time";
    public static final String KEY_PRINT = "isPrint";
    /**
     * Database creation sql statement
     */
    private static final String CREATE_TABLE =
            "create table " + DATABASE_TABLE + " ("
                    + KEY_ID + " integer primary key autoincrement, "
                    + KEY_ORDERID + " text not null, "
                    + KEY_CONAME + " text not null,"
                    + KEY_CONPHONE + " text, "
                    + KEY_PRONUM + " integer not null, "
                    + KEY_SALARY + " real, "
                    + KEY_PAY + " real, "
                    + KEY_TIME + " text not null, "
                    + KEY_PRINT + " integer not null );";
    /**
     * Context
     */
    private final Context mCtx;
    private DatabaseHelper mDbHelper;
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
    public OrderSqliteUtil(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * This method is used for creating/opening connection
     *
     * @return instance of DatabaseUtil
     * @throws SQLException
     */
    public OrderSqliteUtil open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
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
     * @return long
     */
    public long insert(String orderID, String consumerName, String consumerPhone, int productNum, double salary, double pay, String time, int isPrint) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ORDERID, orderID);
        initialValues.put(KEY_CONAME, consumerName);
        initialValues.put(KEY_CONPHONE, consumerPhone);
        initialValues.put(KEY_PRONUM, productNum);
        initialValues.put(KEY_SALARY, salary);
        initialValues.put(KEY_PAY, pay);
        initialValues.put(KEY_TIME, time);
        initialValues.put(KEY_PRINT, isPrint);
        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    /**
     * This method will delete record.
     *
     * @param
     * @return boolean
     */
    public boolean delete(String orderId) {
        return mDb.delete(DATABASE_TABLE, KEY_ORDERID + "=" + orderId, null) > 0;
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
        return mDb.query(DATABASE_TABLE, new String[]{KEY_ORDERID, KEY_CONAME, KEY_CONPHONE, KEY_PRONUM, KEY_SALARY,
                KEY_PAY, KEY_TIME, KEY_PRINT}, null, null, null, null, null);
    }

    /**
     * This method will return Cursor holding the specific record.
     *
     * @param orderID
     * @return Cursor
     * @throws SQLException
     */
    public Cursor fetch(int orderID) throws SQLException {
        Cursor mCursor =
                mDb.query(true, DATABASE_TABLE, new String[]{KEY_ID, KEY_ORDERID, KEY_CONAME, KEY_CONPHONE, KEY_PRONUM,
                                KEY_SALARY, KEY_PAY, KEY_TIME, KEY_PRINT}, KEY_ORDERID + "=" + orderID, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    /**
     * This method will return Cursor holding the list<order> record.
     *
     * @param isPrint
     * @return
     * @throws SQLException
     */
    public List<Order> fetchByisPrint(int isPrint) throws SQLException {
        Cursor mCursor =
                mDb.query(true, DATABASE_TABLE,
                        new String[]{KEY_ID, KEY_ORDERID, KEY_CONAME, KEY_CONPHONE, KEY_PRONUM, KEY_SALARY, KEY_PAY, KEY_TIME, KEY_PRINT},
                        KEY_PRINT + "=" + isPrint, null,
                        null, null, KEY_ID, null);
        List<Order> orders = new ArrayList<>();
        //对游标 Cursor 的遍历
        while (mCursor.moveToNext()) {
            Order order = new Order();
            order.setOrderID(mCursor.getString(1));
            order.setConsumerName(mCursor.getString(2));
            order.setConsumerPhone(mCursor.getString(3));
            order.setProductNum(mCursor.getInt(4));
            order.setSalary(mCursor.getDouble(5));
            order.setHasPay(mCursor.getDouble(6));
            order.setTime(mCursor.getString(7));
            order.setIsPrint(mCursor.getInt(8));

            orders.add(order);
        }
        mCursor.close();
        return orders;
    }

    /**
     * This method will update record.
     *
     * @return boolean
     */
    public boolean update(String orderID, String consumerName, String consumerPhone, int productNum, double salary, double pay, String time, int isPrint) {
        ContentValues args = new ContentValues();
        args.put(KEY_CONAME, consumerName);
        args.put(KEY_CONPHONE, consumerPhone);
        args.put(KEY_PRONUM, productNum);
        args.put(KEY_SALARY, salary);
        args.put(KEY_PAY, pay);
        args.put(KEY_TIME, time);
        args.put(KEY_PRINT, isPrint);
        return mDb.update(DATABASE_TABLE, args, KEY_ORDERID + "=" + orderID, null) > 0;
    }
}
