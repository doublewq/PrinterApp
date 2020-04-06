package com.will.bluetoothprinterdemo.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.will.bluetoothprinterdemo.vo.ProductSingle;

import java.util.ArrayList;
import java.util.List;

public class SqliteProductUtil {
    private static final String TAG = "ProductSingleDatabaseUtil";

    /**
     * Database Name
     */
    private static final String DATABASE_NAME = "data_product_single";

    /**
     * Database Version
     */
    private static final int DATABASE_VERSION = 2;

    /**
     * Table Name
     */
    private static final String DATABASE_TABLE = "tb_productsingle";

    /**
     * Table columns
     */
    public static final String KEY_ID = "pID";
    public static final String KEY_NAME = "pName";
    public static final String KEY_COLOR = "pColor";
    public static final String KEY_PRICE = "pPrice";
    public static final String KEY_IMAGE = "pImage";
    public static final String KEY_SELLNUM = "pSellNum";
    public static final String KEY_CID = "pCID";

    /**
     * Database creation sql statement
     */
    private static final String CREATE_TABLE =
            "create table " + DATABASE_TABLE + " ("
                    + KEY_ID + " integer primary key autoincrement, "
                    + KEY_NAME + " text not null, "
                    + KEY_COLOR + " text, "
                    + KEY_PRICE + " real, "
                    + KEY_IMAGE + " text, "
                    + KEY_SELLNUM + " integer, "
                    + KEY_CID + " integer not null);";

    /**
     * Context
     */
    private final Context mCtx;
    private SqliteProductUtil.DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private final static String[][] productLists = {{"棉小定", "棉中定", "学生决明子", "棉特大", "决明子枕", "决明子小半圆", "麦穗决明子", "泡泡小磁铁", "决明子理疗枕", "普通磁疗枕", "针织水洗枕", "热熔水洗枕", "彩格水洗枕", "婚庆水洗枕", "绗缝磁疗枕", "浪琴决明子", "粗网决明子", "百年好合泡泡枕", "负离子养生枕", "皇家玉石枕", "麦饭石枕", "枕芯包装2", "枕芯包装3"},
            {"花边小米", "棉小米", "精品小米枕", "高档小米枕", "水晶绒小米枕", "花边小米（大号）", "泡沙小米枕"},
            {"花边荞麦", "棉荞麦", "精品荞麦", "小号定型荞麦", "中号定型荞麦", "大号定型荞麦", "儿童粗荞麦", "方枕荞麦", "卡通荞麦枕", "超柔儿童枕", "卡通枕"},
            {"全棉枕套", "磨毛枕套", "枕套（小号）", "枕套（大号）"},
            {"90方格床垫", "1.2方格床垫", "1.5方格床垫", "1.8方格床垫", "90学生床垫", "90兰格褥", "90白毛床垫", "1.2白毛床垫", "1.5白毛床垫", "1.8白毛床垫", "90羊羔毛床垫", "1.2羊羔毛床垫", "1.5羊羔毛床垫", "1.8羊羔毛床垫", "1.5水晶绒床垫", "1.8水晶绒床垫"},
            {"1.5钻石绒被", "1.8钻石绒被", "2m钻石绒被", "1.5亲肤绒被", "1.8亲肤绒被", "2m亲肤绒被", "1.5充绒被", "1.8充绒被", "2m充绒被", "1.5中印", "1.8大印", "1.5棉加丝", "1.8棉加丝", "2m水洗绣花被", "2m大红绣花", "1.5海藻棉", "2m海藻棉", "被子包装4", "被子包装5"},
            {"小双面", "大双面", "经典养生枕", "羽丝绒枕", "50芯", "60芯", "水鸟枕"},
            {"竹子学生枕", "小麻将", "竹子大号", "中号麻将枕", "大号麻将枕", "精品大麻将", "蕾丝大麻将", "蕾丝滕枕", "麻将花边枕", "蝴蝶透气麻将枕", "特大号麻将枕", "麻将护颈枕", "麻将颈椎枕", "麻将糖果枕", "节节高麻将枕", "胡桃香枕", "印尼绣花滕枕", "绣花滕枕", "麻将紫花边"},
            {"1.5钻石绒夏被", "1.8钻石绒夏被", "2米钻石绒夏被", "1.5水洗棉夏被", "1.8水洗棉夏被", "2米水洗棉夏被", "1.5纳米夏被", "1.8纳米夏被", "2米纳米夏被", "1.5海岛棉夏被", "2米海岛棉夏被", "1.5珠光夏被", "1.8珠光夏被", "2米珠光夏被", "1.5水洗棉绗綉夏被", "2米水洗棉绗綉夏被", "1.5春秋被", "1.8春秋被", "2米春秋被", "夏被包装"}};

//    private final static double[][] priceList = {{7.0,8.0,9.0,11.0,14.0,13.0,16.0,16.0,18.0,18.0,22.0,22.0,22.0,28.0,22.0,25.0,28.0,25.0,28.0,28.0,28.0,2.0,3.0,10.0},
//            {20.5,},
//            {}}

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
            db.execSQL(CREATE_TABLE);
            System.out.println("create success" + CREATE_TABLE);
            for (int i = 0; i < productLists.length; i++) {
                for (int j = 0; j < productLists[i].length; j++) {  //
                    String sql = "insert into " + DATABASE_TABLE + " (" + KEY_NAME + "," + KEY_COLOR + "," + KEY_PRICE + "," + KEY_SELLNUM + "," + KEY_CID + ")values ('" + new String(productLists[i][j]) + "','" + new String("配色") + "',20.5,0," + (i + 1) + ")";
                    System.out.println(sql);
                    db.execSQL(sql);
                }
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
    public SqliteProductUtil(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * This method is used for creating/opening connection
     *
     * @return instance of DatabaseUtil
     * @throws SQLException
     */
    public SqliteProductUtil open() throws SQLException {
        mDbHelper = new SqliteProductUtil.DatabaseHelper(mCtx);
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
    public long insert(int kind, String name, String price) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_CID, kind);
        initialValues.put(KEY_NAME, name);
        initialValues.put(KEY_PRICE, price);
        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    public ProductSingle fetchByPID(int pID) throws SQLException {
        Cursor mCursor =
                mDb.query(true, DATABASE_TABLE, new String[]{KEY_ID, KEY_NAME, KEY_COLOR, KEY_PRICE, KEY_IMAGE, KEY_SELLNUM, KEY_CID}, KEY_ID + "=" + pID, null,
                        null, null, null, null);
        List<ProductSingle> products = new ArrayList<>();
        while (mCursor.moveToNext()) {
            ProductSingle product = new ProductSingle();
            product.setpID(mCursor.getInt(0));
            product.setpName(mCursor.getString(1));
            product.setpColor(mCursor.getString(2));
            product.setpPrice(mCursor.getDouble(3));
            product.setpImage(mCursor.getString(4));
            product.setpSellNum(mCursor.getInt(5));
            product.setP_CID(mCursor.getInt(6));
            products.add(product);
        }
        return products.get(0);
    }

    // 根据 类别ID取产品
    public List<ProductSingle> fetchByCID(int cID) throws SQLException {
        Cursor mCursor =
                mDb.query(true, DATABASE_TABLE, new String[]{KEY_ID, KEY_NAME, KEY_COLOR, KEY_PRICE, KEY_IMAGE, KEY_SELLNUM, KEY_CID}, KEY_CID + "=" + cID, null,
                        null, null, null, null);
        List<ProductSingle> products = new ArrayList<>();
        while (mCursor.moveToNext()) {
            ProductSingle product = new ProductSingle();
            product.setpID(mCursor.getInt(0));
            product.setpName(mCursor.getString(1));
            product.setpColor(mCursor.getString(2));
            product.setpPrice(mCursor.getDouble(3));
            product.setpImage(mCursor.getString(4));
            product.setpSellNum(mCursor.getInt(5));
            product.setP_CID(mCursor.getInt(6));
            products.add(product);
        }
        return products;
    }

    public List<ProductSingle> fetchAll() throws SQLException {
        Cursor mCursor =
                mDb.query(true, DATABASE_TABLE, new String[]{KEY_ID, KEY_NAME, KEY_COLOR, KEY_PRICE, KEY_IMAGE, KEY_SELLNUM, KEY_CID}, null, null,
                        null, null, null, null);
        List<ProductSingle> products = new ArrayList<>();
        while (mCursor.moveToNext()) {
            ProductSingle product = new ProductSingle();
            product.setpID(mCursor.getInt(0));
            product.setpName(mCursor.getString(1));
            product.setpColor(mCursor.getString(2));
            product.setpPrice(mCursor.getDouble(3));
            product.setpImage(mCursor.getString(4));
            product.setpSellNum(mCursor.getInt(5));
            product.setP_CID(mCursor.getInt(6));
            products.add(product);
        }
        return products;
    }

    public boolean deleteEmpty() {

        return mDb.delete(DATABASE_TABLE, KEY_NAME+"= ?", new String[]{""}) > 0;
    }

    public boolean updateSellNum(int pID, int num) {
        ContentValues args = new ContentValues();
        args.put(KEY_SELLNUM, num);
        return mDb.update(DATABASE_TABLE, args, KEY_ID + "=" + pID, null) > 0;
    }

    public boolean updatePrice(int pID, double price) {
        ContentValues args = new ContentValues();
        args.put(KEY_PRICE, price);
        return mDb.update(DATABASE_TABLE, args, KEY_ID + "=" + pID, null) > 0;
    }
}
