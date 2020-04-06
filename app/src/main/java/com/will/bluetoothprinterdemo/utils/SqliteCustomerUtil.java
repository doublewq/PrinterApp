package com.will.bluetoothprinterdemo.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.will.bluetoothprinterdemo.vo.Customer;

import java.util.ArrayList;
import java.util.List;

public class SqliteCustomerUtil {
    private static final String TAG = "CustomerDatabaseUtil";

    /**
     * Database Name
     */
    private static final String DATABASE_NAME = "data_customer";

    /**
     * Database Version
     */
    private static final int DATABASE_VERSION = 5;

    /**
     * Table Name
     */
    private static final String DATABASE_TABLE = "tb_customer";

    /**
     * Table columns
     */
    public static final String KEY_ID = "cID";
    public static final String KEY_NAME = "cName";
    public static final String KEY_PHONE = "cPhone";
    public static final String KEY_BEIZHU = "cBeizhu";
    public static final String KEY_KUOZHAN = "pKuozhan";

    /**
     * Database creation sql statement
     */
    private static final String CREATE_TABLE =
            "create table " + DATABASE_TABLE + " ("
                    + KEY_ID + " integer primary key autoincrement, "
                    + KEY_NAME + " text not null, "
                    + KEY_PHONE + " text, "
                    + KEY_BEIZHU + " text, "
                    + KEY_KUOZHAN + " text );";

    /**
     * Context
     */
    private final Context mCtx;
    private SqliteCustomerUtil.DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }


        final String[] list = {"汉城店", "大悟店", "谷城店", "竹溪店", "竹山店", "十堰店", "房县店", "岐山店", "镇原店",
                "陇县店", "铜川店", "山阳店", "咸阳一店", "咸阳二店", "城固一店", "城固二店", "商洛店", "长安店",
                "杨建群一店", "杨建群二店", "米脂衣世界", "三桥衣世界", "十里铺衣世界", "邓文斌衣世界", "贵阳荔波店", "勉县店(林希)",
                "四川开江点(林希)", "贵州玉屏店(林希)", "衣佳汇二店(谭敏刚)", "柞水店(谭敏刚)", "陕西衣都汇1店", "陕西衣都汇2店", "陕西衣都汇3店", "陕西衣都汇5店"};//要填充的数据

        /**
         * onCreate method is called for the 1st time when database doesn't exists.
         */
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE);
            for (int i = 0; i < list.length; i++){
                String sql = "insert into "+DATABASE_TABLE + " ("+KEY_NAME +", "+KEY_PHONE+", "+KEY_BEIZHU+", "+KEY_KUOZHAN+") values ('"+new String(list[i]) +"','','','')";
                db.execSQL(sql);
            }

//            for (int i = 0; i < productLists.length; i++) {
//                for (int j = 0; j < productLists[i].length; j++) {  //
//                    String sql = "insert into " + DATABASE_TABLE + " (" + KEY_NAME + "," + KEY_COLOR + "," + KEY_PRICE + "," + KEY_SELLNUM + "," + KEY_CID + ")values ('" + new String(productLists[i][j]) + "','" + new String("配色") + "',20.5,0," + (i + 1) + ")";
//                    System.out.println(sql);
//                    db.execSQL(sql);
//                }
//            }
        }

        /**
         * onUpgrade method is called when database version changes.
         */
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
//                    + newVersion);
            db.execSQL("drop table if exists "+DATABASE_TABLE);
            onCreate(db);
        }
    }
    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     *
     * @param ctx the Context within which to work
     */
    public SqliteCustomerUtil(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * This method is used for creating/opening connection
     *
     * @return instance of DatabaseUtil
     * @throws SQLException
     */
    public SqliteCustomerUtil open() throws SQLException {
        mDbHelper = new SqliteCustomerUtil.DatabaseHelper(mCtx);
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
     * @param
     * @return
     */
    public long insert(String name, String phone,String beizhu) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAME, name);
        initialValues.put(KEY_PHONE, phone);
        initialValues.put(KEY_BEIZHU, beizhu);
        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    public List<Customer> fetchAll() throws SQLException {
        Cursor mCursor =
                mDb.query(true, DATABASE_TABLE, new String[]{KEY_ID, KEY_NAME, KEY_PHONE,KEY_BEIZHU,KEY_KUOZHAN}, null, null,
                        null, null, null, null);
        List<Customer> customers = new ArrayList<>();
        while (mCursor.moveToNext()) {
            Customer customer = new Customer();
            customer.setId(mCursor.getInt(0));
            customer.setName(mCursor.getString(1));
            customer.setPhone(mCursor.getString(2));
            customer.setBeizhu(mCursor.getString(3));
            customer.setKuozhan(mCursor.getString(4));
            customers.add(customer);
        }
        return customers;
    }

    public Customer fetchByCustomeName(String name) throws SQLException {
        Cursor mCursor =
                mDb.query(true, DATABASE_TABLE, new String[]{KEY_ID, KEY_NAME,KEY_PHONE,KEY_BEIZHU,KEY_KUOZHAN}, KEY_NAME + "=" + name, null,
                        null, null, null, null);
        List<Customer> customers = new ArrayList<>();
        while (mCursor.moveToNext()) {
            Customer product = new Customer();
            Customer customer = new Customer();
            customer.setId(mCursor.getInt(0));
            customer.setName(mCursor.getString(1));
            customer.setPhone(mCursor.getString(2));
            customer.setBeizhu(mCursor.getString(3));
            customer.setKuozhan(mCursor.getString(4));
            customers.add(customer);
        }
        return customers.get(0);
    }
    public boolean update(int cId,String name,String phone,String beizhu) {
        ContentValues args = new ContentValues();
        args.put(KEY_NAME, name);
        args.put(KEY_PHONE,phone);
        args.put(KEY_BEIZHU,beizhu);
        return mDb.update(DATABASE_TABLE, args, KEY_ID + "=" + cId, null) > 0;
    }
}
