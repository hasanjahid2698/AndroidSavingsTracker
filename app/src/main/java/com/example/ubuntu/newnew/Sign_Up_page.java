package com.example.ubuntu.newnew;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Sign_Up_page extends AppCompatActivity implements View.OnClickListener {

    private Button signupbutton;
    private TextView doneTextView;
    private EditText userSignup,passwordSignUp,phoneSignUp,confirmSignup,fatherSignup;
    DatabaseHelper databaseHelper;

    UserDetails userDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign__up_page);

        confirmSignup=(EditText) findViewById(R.id.ConfirmpasswordEditText);
        fatherSignup=(EditText) findViewById(R.id.fathernameEditText);
        phoneSignUp=(EditText) findViewById(R.id.phonenuberEditText);
        userSignup=(EditText) findViewById(R.id.usernameEditText);
        passwordSignUp=(EditText) findViewById(R.id.passwordEditText);

        signupbutton =(Button) findViewById(R.id.SignUpID);

        signupbutton.setOnClickListener(this);
        confirmSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(passwordSignUp.getText().toString().length()==0){
                    passwordSignUp.setError("Set password first");
                    return;
                }
                else if(passwordSignUp.getText().toString().length()<6){
                    passwordSignUp.setError("At least 6 Character");
                    return;
                }
            }
        });

        userDetails=new UserDetails();

        databaseHelper=new DatabaseHelper(this);
        SQLiteDatabase sqLiteDatabase= databaseHelper.getWritableDatabase();

    }

    @Override
    public void onClick(View v) {

        String name=userSignup.getText().toString();
        String father=fatherSignup.getText().toString();
        String phone=phoneSignUp.getText().toString();
        String confirmuser=confirmSignup.getText().toString();
        String password=passwordSignUp.getText().toString();
        if(name.length()==0 || father.length()==0 || phone.length()==0 || confirmuser.length()==0 || password.length()==0)
        {
            Toast.makeText(this,"Fill All info please",Toast.LENGTH_LONG).show();
            return;
        }
        if(password.equals(confirmuser)==false)
        {

            // Toast.makeText(this,password+" <> "+confirmuser+"<>  "+password.equals(confirmuser),Toast.LENGTH_LONG).show();
//            passwordSignUp.setText("");
            confirmSignup.setText("");
            confirmSignup.setError("Mismatch Password !");
            return;
        }
        userDetails.setUsername(name);
        userDetails.setFatherName(father);
        userDetails.setPhoneNumber(phone);
        userDetails.setPassword(password);

        long rowvalue=databaseHelper.insertData(userDetails);

        Intent intent= new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();
        if(rowvalue>-1)
        {
            Toast.makeText(getApplicationContext(),"Row inserted : "+rowvalue,Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"unsuccessful to insert row",Toast.LENGTH_SHORT).show();

        }

    }
}
