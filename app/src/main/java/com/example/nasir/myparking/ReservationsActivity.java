package com.example.nasir.myparking;
//Author
//date
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReservationsActivity extends AppCompatActivity {

    EditText custNameET,pkLotName,pkAddress,timeFrom,timeTo,cardNumber,expDate,securityCode;
    String getName,getPkLotName,getkAddress,getFrom,getTo,getCardNumber,getExpireDate,getSecurity;
    Button btnSaves, btnClears,btnView;
     Date RTimeFrom, RTimeTo, result;

    DBHelper myDb;
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservations);

        myDb=new DBHelper(this);

        btnSaves=(Button)findViewById(R.id.btnConfirmRV);
        btnClears=(Button)findViewById(R.id.btnClearRV);
        btnView =(Button)findViewById(R.id.btnAll);

        custNameET= (EditText)findViewById(R.id.customerNameET);


        pkLotName=(EditText)findViewById(R.id.parkingNameET);
        pkLotName.setInputType(InputType.TYPE_CLASS_TEXT);
        pkAddress=(EditText)findViewById(R.id.parkingLotAddressET);
        timeFrom=(EditText)findViewById(R.id.fromET);
        timeTo=(EditText)findViewById(R.id.toET);
        cardNumber=(EditText)findViewById(R.id.cardNumberET);
        expDate=(EditText)findViewById(R.id.expireDateET);
        securityCode=(EditText)findViewById(R.id.securityCodeET);

        RadioButton rdCredit = (RadioButton)findViewById(R.id.rdCredit);
        RadioButton rdDebit =(RadioButton)findViewById(R.id.rdDebit);


        insertingData();
        viewAllRecords();

    }

    public void insertingData(){

        btnSaves.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                Intent intentNext = new Intent(ReservationsActivity.this, ReceiptActivity.class);
                intentNext.putExtra("Name", custNameET.getText().toString());
                intentNext.putExtra("parkingLotName", pkLotName.getText().toString());
                intentNext.putExtra("parkingAddress", pkAddress.getText().toString());

                getName = custNameET.getText().toString();
                getPkLotName=pkLotName.getText().toString();
                getkAddress=pkAddress.getText().toString();
                getFrom=timeFrom.getText().toString();
                getTo=timeTo.getText().toString();
                getCardNumber=cardNumber.getText().toString();
                getExpireDate=expDate.getText().toString();
                getSecurity=securityCode.getText().toString();

                try
                {
                    DateFormat fmt = new SimpleDateFormat("HH:mm");
                    DateFormat mmyy = new SimpleDateFormat("MM/yyyy");
                    mmyy.setLenient(false);
                    result = mmyy.parse(getExpireDate); // <- should not be a valid date!

                    RTimeFrom = fmt.parse(getFrom);
                    RTimeTo = fmt.parse(getTo);



                if (getName.equals("")|| getPkLotName.equals("")|| getkAddress.equals("")|| getFrom.equals("")|| getTo.equals("") || getCardNumber.equals("")|| getExpireDate.equals("")||getSecurity.equals(""))
                {
                    if (cardNumber.getText().toString().length()>16){
                        Toast.makeText(ReservationsActivity.this,"card number Must be 16 digits",Toast.LENGTH_LONG).show();

                    }

                    else if (RTimeFrom.equals(RTimeTo) == true || timeFrom.length() > 4 || timeTo.length() > 4){
                        Toast.makeText(ReservationsActivity.this,"time from must be in '00:00' format ",Toast.LENGTH_LONG).show();
                    }

                    else if (securityCode.getText().toString().length()>3){
                        Toast.makeText(ReservationsActivity.this,"security code must be 3 digits ",Toast.LENGTH_LONG).show();

                    }
                    Toast.makeText(ReservationsActivity.this,"Fields are Required",Toast.LENGTH_LONG).show();
                    return;
                }

                   // boolean isInserted = myDb.insertData(custNameET.getText().toString(),pkLotName.getText().toString(),pkAddress.getText().toString(),RTimeFrom.toString(),RTimeTo.toString(),cardNumber.getText().toString(),result.toString(),securityCode.getText().toString());
                    // boolean isInserted = myDb.insertData("2", "2", "2,","2","2","3","2","2");

                    if (true){

                    startActivity(intentNext);

                    Toast.makeText(ReservationsActivity.this,"Record Added to DB" + RTimeFrom.toString() + RTimeTo.toString() + cardNumber.getText().toString() + result.toString() + securityCode.getText().toString(), Toast.LENGTH_LONG).show();

                }else {
                    Toast.makeText(ReservationsActivity.this,"Record is not added to DB" + RTimeFrom.toString() + RTimeTo.toString() + result.toString() ,Toast.LENGTH_LONG).show();
                }
                }catch(Exception e)
                {
                    Toast.makeText(ReservationsActivity.this,"Something Went Absolutely Wrong, " +
                            "please follow the hints and make sure all fields are filled!!",Toast.LENGTH_LONG).show();
                }


            }

        }
        );
    }

    public void clear_onClick(View view){
        btnClears.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v)
            {
                EditText[] editTexts = {custNameET,pkLotName,pkAddress,timeFrom,timeTo,cardNumber,expDate,securityCode};

                for (EditText et:editTexts) {
                    et.setText("");
                }
            }
        });
    }

    //for checking the database.
    public void viewAllRecords(){

        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                Cursor res= myDb.getAllData(DBHelper.RESERVATION_TABLE);

                if (res.getCount()==0){
                    showMessages("Error","Nothing is found");

                    return;
                }else {

                    StringBuffer buffer = new StringBuffer();
                    while (res.moveToNext()){
                        buffer.append("id :"+res.getString(0)+"\n");
                        buffer.append("\t"+"Customer Name :"+res.getString(1)+"\n");
                        buffer.append("\t"+"Parking Lot Name :"+res.getString(2)+"\n");
                        buffer.append("\t"+"Parking Lot Address : "+res.getString(3)+"\n");
                        buffer.append("\t"+"Timing From : "+res.getString(4)+"\n");
                        buffer.append("\t"+"Timing To : "+res.getString(5)+"\n");
                        buffer.append("\t"+"Card Number : "+res.getString(6)+"\n");
                        buffer.append("\t"+"Exprity Date : "+res.getString(7)+"\n");
                        buffer.append("\t"+"Security Code : "+res.getString(8)+"\n");
                    }

                    //show all

                    showMessages("Data",buffer.toString());
                }
            }
        });
    }


    public void showMessages(String title, String message){

        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }


}
