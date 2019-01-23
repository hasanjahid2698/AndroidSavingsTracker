package com.example.ubuntu.newnew;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Transaction_details.db";
    public static final String TABLE_NAME = "Transaction_Table";
    public static final String ID_COLUMN_0 = "ID";
    public static final String OPTION_COLUMN_1= "OPTION";
    public static final String DATE_COLUMN_2 = "DATE";
    public static final String CATEGORY_COLUMN_3 = "CATEGORY";
    public static final String AMOUNT_COLUMN_4 = "AMOUNT";

    public static String selectedDate;
    //
    Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
    }

    public DatabaseHelper(Context context, String date) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
        selectedDate = date;
    }

    public void setSelectedDate(String date){
        selectedDate=date;
    }



    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table "+TABLE_NAME+" ( ID INTEGER PRIMARY KEY AUTOINCREMENT,OPTION INTEGER ,DATE TEXT, CATEGORY TEXT, AMOUNT REAL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists "+TABLE_NAME);
        onCreate(sqLiteDatabase);

    }

    public boolean insertData(Integer option, String date, String category, Double amount){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(OPTION_COLUMN_1,option);
        contentValues.put(DATE_COLUMN_2,date);
        contentValues.put(CATEGORY_COLUMN_3,category);
        contentValues.put(AMOUNT_COLUMN_4,amount);

        long result = sqLiteDatabase.insert(TABLE_NAME,null,contentValues);
        if(result == -1) return false;
        else return true;
    }

    public Cursor getAlldata(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from "+TABLE_NAME,null);
        return cursor;
    }

    public ArrayList <ListCollection> listfromdb(){
//    public List <ListCollection> listfromdb() {

        SQLiteDatabase db = this.getReadableDatabase();

//        List<ListCollection> results = new ArrayList<ListCollection>();
        ArrayList <ListCollection> results = new ArrayList<ListCollection>();

        Cursor crs = db.rawQuery("select * from "+TABLE_NAME, null);
        while (crs.moveToNext()) {
            ListCollection item = new ListCollection();
            item.setId(crs.getInt(crs.getColumnIndex(ID_COLUMN_0)));
            item.setOption(crs.getInt(crs.getColumnIndex(OPTION_COLUMN_1)));
            item.setDate(crs.getString(crs.getColumnIndex(DATE_COLUMN_2)));
            item.setCategory(crs.getString(crs.getColumnIndex(CATEGORY_COLUMN_3)));
            item.setAmount(crs.getDouble(crs.getColumnIndex(AMOUNT_COLUMN_4)));
            results.add(item);
        }

        db.close();
        return results;
    }

    public Integer updateData(Integer id ,Integer option, String date, String category, Double amount){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(OPTION_COLUMN_1,option);
        contentValues.put(DATE_COLUMN_2,date);
        contentValues.put(CATEGORY_COLUMN_3,category);
        contentValues.put(AMOUNT_COLUMN_4,amount);

        return sqLiteDatabase.update(TABLE_NAME,contentValues,"id = ?",new String[] { Integer.toString(id) });

    }

    public Integer deleteData(Integer id){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.delete(TABLE_NAME, "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public ArrayList <ListCollection> listfromdbondate() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<ListCollection> results = new ArrayList<ListCollection>();

//        Cursor crs = db.rawQuery("select * from "+TABLE_NAME, null);

        Cursor crs = db.query(TABLE_NAME,null,DATE_COLUMN_2+" LIKE ?", new String [] {"%"+selectedDate+"%"},null,null,null);
        while (crs.moveToNext()) {
            ListCollection item = new ListCollection();
            item.setId(crs.getInt(crs.getColumnIndex(ID_COLUMN_0)));
            item.setOption(crs.getInt(crs.getColumnIndex(OPTION_COLUMN_1)));
            item.setDate(crs.getString(crs.getColumnIndex(DATE_COLUMN_2)));
            item.setCategory(crs.getString(crs.getColumnIndex(CATEGORY_COLUMN_3)));
            item.setAmount(crs.getDouble(crs.getColumnIndex(AMOUNT_COLUMN_4)));
            results.add(item);
        }

        db.close();
        return results;
    }


    public ArrayList <ListCollection> listfromdbondate(String selectedYear) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<ListCollection> results = new ArrayList<ListCollection>();

//        Cursor crs = db.rawQuery("select * from "+TABLE_NAME, null);

        Cursor crs = db.query(TABLE_NAME,null,DATE_COLUMN_2+" LIKE ?", new String [] {"%"+selectedYear+"%"},null,null,null);
        while (crs.moveToNext()) {
            ListCollection item = new ListCollection();
            item.setId(crs.getInt(crs.getColumnIndex(ID_COLUMN_0)));
            item.setOption(crs.getInt(crs.getColumnIndex(OPTION_COLUMN_1)));
            item.setDate(crs.getString(crs.getColumnIndex(DATE_COLUMN_2)));
            item.setCategory(crs.getString(crs.getColumnIndex(CATEGORY_COLUMN_3)));
            item.setAmount(crs.getDouble(crs.getColumnIndex(AMOUNT_COLUMN_4)));
            results.add(item);
        }

        db.close();
        return results;
    }

}

