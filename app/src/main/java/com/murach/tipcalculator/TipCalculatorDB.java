package com.murach.tipcalculator;


import android.content.ContentValues;
import android.content.Context;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by MyComp on 12/1/2016.
 */

public class TipCalculatorDB  extends SQLiteOpenHelper {

    //database constants
    public static final String DB_NAME = "tipcalculator.db";
    public static final int DB_VERSION = 1;

    //list table constants
    public static final String TIP_CALCULATOR_TABLE = "bill";

    public static final String TABLE_ID = "_id";
    public static final int TABLE_ID_COL = 0;


    public static final String BILL_DATE = "bill_date";
    public static final int BILL_DATE_COL = 1;


    public static final String BILL_AMOUNT = "bill_amount";
    public static final int BILL_AMOUNT_COL = 2;


    public static final String TIP_PERCENT = "tip_percent";
    public static final int TIP_PERCENT_COL = 3;

    public TipCalculatorDB(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String query =
                "CREATE TABLE " + TIP_CALCULATOR_TABLE + " (" +
                        TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        BILL_DATE + " INTEGER NOT NULL, " +
                        BILL_AMOUNT + " REAL NOT NULL, " +
                        TIP_PERCENT + " REAL NOT NULL, " +
                        ");";
        db.execSQL(query);




    String insert = "INSERT INTO " + TIP_CALCULATOR_TABLE + " VALUES (1, 2, 40.60, 0.30)";
    db.execSQL(insert);
    insert = "INSERT INTO " + TIP_CALCULATOR_TABLE  + " VALUES (2, 0, 100.40, 0.20)";
    db.execSQL(insert);

}
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// Drop older table if existed
        db.execSQL("DROP TABLE IF EXIST " + TIP_CALCULATOR_TABLE);
// Creating tables again
        onCreate(db);
    }


    public void deleteBill() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TIP_CALCULATOR_TABLE + " WHERE " + BILL_AMOUNT);

    }


    public float getTips(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_ID, new String[]{"AVERAGE(" + TIP_PERCENT + ")"}, null,
                null, null, null, null);
        cursor.moveToFirst();
        float average = cursor.getFloat(0);
        db.close();
        return average;

    }

    public Tip getLastTips() {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TIP_CALCULATOR_TABLE + " WHERE (" + TABLE_ID + "" +
                " = (SELECT MAX (" + TABLE_ID + ") " +
                "FROM " + TIP_CALCULATOR_TABLE + "))", null);
        if (cursor.getCount() <= 0)
            return null;

        cursor.moveToFirst();
        Tip tip = cursorTip(cursor);
        db.close();
        return tip;

    }

    public void addTip(Tip tip) {
        ContentValues values = new ContentValues();
        values.put(TABLE_ID, tip.getId());
        values.put(BILL_DATE, tip.getDateMillis());
        values.put(BILL_AMOUNT, tip.getBillAmount());
        values.put(TIP_PERCENT, tip.getTipPercent());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TIP_CALCULATOR_TABLE, null, values);
        db.close();
    }

    public ArrayList<Tip> getTips(int i) {
        ArrayList<Tip> tips = new ArrayList<Tip>();
        SQLiteDatabase db = getReadableDatabase();
if (i == 1) {
    String query = "SELECT * FROM " + TIP_CALCULATOR_TABLE + ";";


    //cursor point to a location in result
    Cursor cursor = db.rawQuery("SELECT * FROM " + TIP_CALCULATOR_TABLE + " WHERE 1", null);
    //move to first row in result
    cursor.moveToFirst();


    //position after the last row means the end of the results
    while (!cursor.isAfterLast()) {
        int id = cursor.getInt(TABLE_ID_COL);
        int billDate = cursor.getInt(BILL_DATE_COL);
        float billAmount = cursor.getFloat(BILL_AMOUNT_COL);
        float tipPercent = cursor.getFloat(TIP_PERCENT_COL);

        Tip tip = new Tip(id, billDate, billAmount, tipPercent);
        cursor.moveToNext();
        tips.add(tip);
        i++;
        cursor.moveToNext();

    }

    db.close();


}else {
}  return tips;
    }


    private Tip cursorTip(Cursor cursor) {
    Tip tip = new Tip();
        tip.setId(cursor.getInt(0));
        tip.setDateMillis(cursor.getInt(1));
        tip.setBillAmount(cursor.getFloat(2));
        tip.setTipPercent(cursor.getFloat(3));
        return tip;

    }

}