package com.yuedong.youbutie_merchant_android.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author 俊鹏
 */
public class DbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "youbutie.db";
    private static final int DB_VERSION = 1;
    //    public static final String TABLE_ARAE = "table_area";
    public static final String TABLE_SERVICE_INFO = "table_serviceinfo";
    //    public static final String TABLE_SERACH_RECORD = "table_search_record";
    public static final String TABLE_CAR = "table_car";
//    public static final String TABLE_CAR_SERIES = "table_series";

    //    private static String CREATE_AREA_TABLE_SQL = "CREATE TABLE " + TABLE_ARAE //
//            + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, areaId TEXT,"//
//            + "name TEXT,cityId TEXT,cityName TEXT,city_longitude DOUBLE,city_latitude DOUBLE);";
    private static String CREATE_SERVICEINFO_TABLE_SQL = "CREATE TABLE " + TABLE_SERVICE_INFO //
            + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, serviceinfoId TEXT,"//
            + "name TEXT,icon TEXT);";
    //    private static String CREATE_SEARCH_RECORD_TABLE_SQL = "CREATE TABLE " + TABLE_SERACH_RECORD //
//            + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, searchContent TEXT,"//
//            + "createTime TEXT,type TEXT);";
    private static String CREATE_CAR_TABLE_SQL = "CREATE TABLE " + TABLE_CAR //
            + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, carId TEXT,name TEXT,letter TEXT,series TEXT,icon TEXT);";

//    private static String CREATE_CAR_SERIES_TABLE_SQL = "CREATE TABLE " + TABLE_CAR_SERIES //
//            + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, seriesId TEXT,carId TEXT,carName TEXT,name TEXT);";

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        db.execSQL(CREATE_AREA_TABLE_SQL);
        db.execSQL(CREATE_SERVICEINFO_TABLE_SQL);
//        db.execSQL(CREATE_SEARCH_RECORD_TABLE_SQL);
        db.execSQL(CREATE_CAR_TABLE_SQL);
//        db.execSQL(CREATE_CAR_SERIES_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
