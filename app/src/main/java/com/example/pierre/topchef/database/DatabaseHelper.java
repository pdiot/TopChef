package com.example.pierre.topchef.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;

public class DatabaseHelper extends SQLiteOpenHelper{

    private static final String SQL_CREATE_PRODUCTS_TABLE =
            "CREATE TABLE " + DatabaseContract.ProductsTable.TABLE_NAME + " (" +
                    DatabaseContract.ProductsTable._ID + " INTEGER PRIMARY KEY," +
                    DatabaseContract.ProductsTable.COLUMN_NAME_TASTE_ID + " INTEGER," +
                    DatabaseContract.ProductsTable.COLUMN_NAME_NAME_EN + " TEXT," +
                    DatabaseContract.ProductsTable.COLUMN_NAME_NAME_FR + " TEXT," +
                    DatabaseContract.ProductsTable.COLUMN_NAME_DRAWABLE_ID + " INTEGER)"
            ;

    private static final String SQL_CREATE_TASTES_TABLE =
            "CREATE TABLE " + DatabaseContract.TasteTable.TABLE_NAME + " (" +
                    DatabaseContract.TasteTable._ID + " INTEGER PRIMARY KEY," +
                    DatabaseContract.TasteTable.COLUMN_NAME_TASTE_NAME_EN + " TEXT," +
                    DatabaseContract.TasteTable.COLUMN_NAME_TASTE_NAME_FR + " TEXT)"
            ;

    private static final String SQL_CREATE_GOOD_ASSOCIATIONS_TABLE =
            "CREATE TABLE " + DatabaseContract.GoodAssociationsTable.TABLE_NAME + " (" +
                    DatabaseContract.GoodAssociationsTable._ID + " INTEGER PRIMARY KEY," +
                    DatabaseContract.GoodAssociationsTable.COLUMN_NAME_PRODUCT_ID_1 + " INTEGER," +
                    DatabaseContract.GoodAssociationsTable.COLUMN_NAME_PRODUCT_ID_2 + " INTEGER)"
            ;


    private static final String SQL_CREATE_BAD_ASSOCIATIONS_TABLE =
            "CREATE TABLE " + DatabaseContract.BadAssociationsTable.TABLE_NAME + " (" +
                    DatabaseContract.BadAssociationsTable._ID + " INTEGER PRIMARY KEY," +
                    DatabaseContract.BadAssociationsTable.COLUMN_NAME_PRODUCT_ID_1 + " INTEGER," +
                    DatabaseContract.BadAssociationsTable.COLUMN_NAME_PRODUCT_ID_2 + " INTEGER)"
            ;

    private static final String SQL_DELETE_TASTES =
            "DROP TABLE IF EXISTS " + DatabaseContract.TasteTable.TABLE_NAME;
    private static final String SQL_DELETE_PRODUCTS =
            "DROP TABLE IF EXISTS " + DatabaseContract.ProductsTable.TABLE_NAME;
    private static final String SQL_DELETE_GOOD_ASSOCIATIONS =
            "DROP TABLE IF EXISTS " + DatabaseContract.GoodAssociationsTable.TABLE_NAME;
    private static final String SQL_DELETE_BAD_ASSOCIATIONS =
            "DROP TABLE IF EXISTS " + DatabaseContract.BadAssociationsTable.TABLE_NAME;

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "TopChef.db";

    public DatabaseHelper (Context context) {
        super (context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_PRODUCTS_TABLE);
        db.execSQL(SQL_CREATE_TASTES_TABLE);
        db.execSQL(SQL_CREATE_GOOD_ASSOCIATIONS_TABLE);
        db.execSQL(SQL_CREATE_BAD_ASSOCIATIONS_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL(SQL_DELETE_PRODUCTS);
        db.execSQL(SQL_DELETE_TASTES);
        db.execSQL(SQL_DELETE_GOOD_ASSOCIATIONS);
        db.execSQL(SQL_DELETE_BAD_ASSOCIATIONS);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldV, int newV) {
        onUpgrade(db, oldV, newV);
    }

}
