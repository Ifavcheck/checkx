package com.example.nasir.myparking;
/*
 * Author: Syed Nasir Gohary
 * Date: 2018/07/15
 *Subject: Comp231
 * Project Name: myParking
 * */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {


    //database name and version
    public static final String DATABASE_NAME ="MyParkingDB";
    public static final int DATABASE_VERSION  = 1;

    //Table Names
    public static String ADMINISTRATOR_TABLE = "Administrator";
    public static String CUSTOMER_TABLE = "Customer";
    public static String RESERVATION_TABLE = "Reservation";
    public static String MAINTENANCE_TABLE = "Maintenance";
    public static String FINANCE_TABLE = "Finance";

    //Table Creator Strings
    //Place in Main Activity for execution to exec All Tables before Before inserting

    public static final String ADMINISTRATOR = "CREATE TABLE IF NOT EXISTS" + " ADMINISTRATOR_TABLE " + " (Id INTEGER  NOT NULL UNIQUE , username TEXT NOT NULL UNIQUE "
            + " , password INTEGER NOT NULL, PRIMARY KEY(Id));";

    public static final String CUSTOMER = "CREATE TABLE IF NOT EXISTS " + CUSTOMER_TABLE +
            "( Id INTEGER NOT NULL UNIQUE , username TEXT NOT NULL UNIQUE, password TEXT NOT NULL, " +
            " fName TEXT NOT NULL, lName TEXT NOT NULL, CITY TEXT NOT NULL, postalCode TEXT NOT NULL,  administrator_ID Integer Not null, " +
            " Foreign Key (Administrator_ID) References Administrator(id), Primary Key(Id));" ;

    public static final String RESERVATION = "CREATE TABLE IF NOT EXISTS " + RESERVATION_TABLE +
            " ( Id INTEGER NOT NULL UNIQUE , parkingLotName TEXT NOT NULL, " +
            " parkingLotAddress TEXT NOT NULL, timeFrom TEXT NOT NULL, timeTo TEXT NOT NULL, " +
            "cardType TEXT NOT NULL, cardNumber TEXT NOT NULL, securityCode TEXT NOT NULL, " +
            " Customer_Id INTEGER NOT NULL, PRIMARY KEY(ID), " +
            " FOREIGN KEY(Customer_Id) References Customer(id));";

    public static final String MAINTENANCE = "CREATE TABLE IF NOT EXISTS " + MAINTENANCE_TABLE  + " ( Id INTEGER NOT NULL UNIQUE , username TEXT NOT NULL UNIQUE " +
            ", password TEXT NOT NULL, name TEXT NOT NULL, employeeNumber TEXT NOT NULL, Administrator_Id INTEGER NOT NULL, " +
            "PRIMARY KEY(Id), FOREIGN KEY(Administrator_Id) References Administrator(id));";

    public static final String FINANCE = "CREATE TABLE IF NOT EXISTS " + FINANCE_TABLE + "( Id INTEGER NOT NULL, username TEXT NOT NULL UNIQUE, password NOT NULL " +
            ", name INTEGER NOT NULL, Administrator_Id INTEGER NOT NULL, Customer_Id Integer NOT NULL, " +
            "FOREIGN KEY(Administrator_Id) References Administrator(id), Primary Key(Id), FOREIGN KEY(Customer_Id) References Customer(Id));";


    public DBHelper (Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate (SQLiteDatabase db) {

        db.execSQL(ADMINISTRATOR);
        db.execSQL(CUSTOMER);
        db.execSQL(RESERVATION);
        db.execSQL(MAINTENANCE);
        db.execSQL(FINANCE);


    }

    @Override
    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("drop table if exists "+ ADMINISTRATOR_TABLE);
        db.execSQL("drop table if exists " + CUSTOMER_TABLE);
        db.execSQL("drop table if exists " + RESERVATION_TABLE);
        db.execSQL("drop table if exists " + MAINTENANCE_TABLE);
        db.execSQL("drop table if exists "+ FINANCE_TABLE);


        onCreate(db);
    }


    //when Doing addRow put the table name and the Content Values in the Method Parameters
    public boolean addRow  (String tableName, ContentValues values) throws Exception {
        SQLiteDatabase db = this.getWritableDatabase();
        // Insert the row
        long nr= db.insert(tableName, null, values);

        db.close(); //close database connection
        return nr> -1;
    }


    //checking if Sqlite is accepting the records
    public Cursor getAllData(String tableName){

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor re = db.rawQuery("select * from "+ tableName,null);
        return re;
    }

    // for updating of table
    public boolean updateData(String tableName, ContentValues values, Integer id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(tableName,values,"id=?",new String[] {id.toString()});

        return true;
    }

    //for deleting records
    public Integer deleteData(String tableName, String id){

        SQLiteDatabase database=this.getWritableDatabase();
        return database.delete(tableName,"id= ?", new String[]{id});
    }

    public boolean checkLogin(String tableName, String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        String s;
        Cursor c = db.rawQuery("SELECT * FROM "+tableName+" WHERE " + username + " =? AND" + password + " =?", null);

        if(c.getCount() <= 0) {
            c.close();
            db.close();
            return false;
        } else {
            c.close();
            db.close();
            return true;
        }
    }

    public String SearchExistingAccount(String tableName, String uname){
        SQLiteDatabase db=this.getReadableDatabase();
        String query = "SELECT userName, password FROM "+ tableName + " where userName = ? ";
        Cursor cursor = db.rawQuery(query, new String[] {uname});
        String a, b;
        b = null;
        if(cursor.moveToLast()){
            do{
                a = cursor.getString(0);
                if(a.equals(uname)){
                    b = cursor.getString(1);
                    break;
                }
            }while(cursor.moveToNext());
        }
        return b;
    }
}

