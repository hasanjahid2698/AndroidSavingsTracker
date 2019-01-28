package com.example.ubuntu.newnew;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.util.Log;
import android.widget.Toast;

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

    Context context;


    private static final String TABLE_USER_NAME="User_details";
    private static final String ID="Id";
    private static final String USERNAME="Username";
    private static final String Father_NAME="fathername";
    private static final String Phonenumber="phonenumber";
    private static final String Password="Password";

    private String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER_NAME+ "("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + USERNAME + " VARCHAR(255) NOT NULL,"+ Father_NAME + " VARCHAR(255) NOT NULL," + Phonenumber + " INTEGER NOT NULL,"
            + Password + " VARCHAR(255) NOT NULL UNIQUE" + ")";



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

        try {

            sqLiteDatabase.execSQL("create table "+TABLE_NAME+" ( ID INTEGER PRIMARY KEY AUTOINCREMENT,OPTION INTEGER ,DATE TEXT, CATEGORY TEXT, AMOUNT REAL)");
            sqLiteDatabase.execSQL(CREATE_USER_TABLE);
        }catch (Exception e){
            Toast.makeText(context, "Error is occured", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists "+TABLE_NAME);
        sqLiteDatabase.execSQL("drop table if exists "+TABLE_USER_NAME);
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


    public long insertData(UserDetails userDetails)
    {
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(USERNAME,userDetails.getUsername());
        contentValues.put(Father_NAME,userDetails.getFatherName());
        contentValues.put(Phonenumber,userDetails.getPhoneNumber());
        contentValues.put(Password,userDetails.getPassword());
        long rowID= sqLiteDatabase.insert(TABLE_USER_NAME,null,contentValues);
        return rowID;
    }


    public Boolean findpassword(String password)
    {
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("SELECT * FROM "+ TABLE_USER_NAME,null);
        Boolean result = false;

        if(cursor.getCount()==0)
        {
            Toast.makeText(context,"No username is available ",Toast.LENGTH_SHORT).show();
        }

        else
        {
            while ((cursor.moveToNext()))
            {
                String pass=cursor.getString(4);
                if(pass.equals(password))
                {
                    result =true;
                    break;
                }

            }

        }
        return  result;


    }

    public boolean newPassword(String newPassword)
    {

        String id;
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("SELECT * FROM "+ TABLE_USER_NAME,null);

        ContentValues contentValues=new ContentValues();
        contentValues.put(Password,newPassword);

        sqLiteDatabase.update(TABLE_USER_NAME,contentValues,"id = ?",new String[] { Integer.toString(1) });
        return  true;


    }

    public Boolean findfathername(String father)
    {
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("SELECT * FROM "+ TABLE_USER_NAME,null);

        Boolean result =false;
                while ((cursor.moveToNext()))
                {
                    String fathername=cursor.getString(2);
                    if(fathername.equals(father))
                    {
                        result =true;
                        break;
                    }

                }

        return  result;

    }

    public Boolean haveAccount(){
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("SELECT * FROM "+ TABLE_USER_NAME,null);
        Boolean result ;

        if(cursor.getCount()==0)
        {
            result = false;
        }
        else result = true;

        return result;
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

