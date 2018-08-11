package com.example.nasir.myparking;

import android.content.ContentValues;
import android.content.Intent;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Random;

public class Registration extends AppCompatActivity {

    //Call Database
    DBHelper myDB;

    //Initialized Button
    Button registerRG;

    //Initialized Edit Text
    EditText UserName,Password,FirstName,LastName,Address,City,Postal_Code;
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        UserName = (EditText)findViewById(R.id.editUserName);
        Password = (EditText)findViewById(R.id.editPassword);
        FirstName = (EditText)findViewById(R.id.editFName);
        LastName = (EditText)findViewById(R.id.editLName);
        Address = (EditText)findViewById(R.id.editAddress);
        City = (EditText)findViewById(R.id.editCity);
        Postal_Code = (EditText)findViewById(R.id.editPostCode);



        //Initialized Button
        registerRG = (Button)findViewById(R.id.btnSave);

        //Initialized Database
        myDB = new DBHelper(this);




        //Called AddData
       registerRG.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick (View v) {

                   EditText[] editTexts = {UserName, Password, FirstName, LastName, Address, City, Postal_Code};
                   for (EditText et : editTexts) {
                       if (et.getText().toString().equals("")) {
                           Toast.makeText(Registration.this, "Fields are required", Toast.LENGTH_LONG).show();
                           return;
                       }
                   }
               RegistrationValues();
               Toast.makeText(Registration.this, "Record Added to DB", Toast.LENGTH_LONG).show();

                   startActivity(new Intent(Registration.this, Login.class));
               }

       });
    }


    private void ToastMessage(String message){
        Toast.makeText(Registration.this,message, Toast.LENGTH_LONG).show();
    }

    public void RegistrationValues()
    {
        Random random = new Random();
        int randomInt =  random.nextInt() + 4;

        ContentValues cv = new ContentValues();
        cv.put("id", randomInt);
        cv.put("username", UserName.getText().toString());
        cv.put("password", Password.getText().toString());
        cv.put("fname", FirstName.getText().toString());
        cv.put("lname", LastName.getText().toString());
        cv.put("city", City.getText().toString() + " " + Address.getText().toString());
        cv.put("postalcode", Postal_Code.getText().toString());
        cv.put("administrator_id", 1);

        try{

            myDB.addRow(DBHelper.CUSTOMER_TABLE, cv);
        }
        catch(Exception e)
        {
            Toast.makeText(this, "Failed To Insert Registration Values", Toast.LENGTH_SHORT).show();
        }finally
        {
            myDB.close();
        }

    }

}
