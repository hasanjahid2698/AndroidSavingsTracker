package com.example.ubuntu.newnew;


import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button Loginbutton;
    private EditText Loguser, LogPassword;
    private TextView Accountid, ForgotID;
    private AlertDialog.Builder alertDialogBuilder;
    private String password, username;

    private EditText input, input2;


    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        databaseHelper = new DatabaseHelper(this);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        if (!databaseHelper.haveAccount()) {
            Intent intent = new Intent(LoginActivity.this, Sign_Up_page.class);
            startActivity(intent);
            finish();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Loginbutton = (Button) findViewById(R.id.LogInId);
        //  Loguser=(EditText) findViewById(R.id.LogInUsernameEdittext);
        LogPassword = (EditText) findViewById(R.id.loginpasswordEditText);
        // Accountid=(TextView) findViewById(R.id.newAccountid);
        ForgotID = (TextView) findViewById(R.id.Forgotid);

        Loginbutton.setOnClickListener(this);
        //  Accountid.setOnClickListener(this);
        ForgotID.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        password = LogPassword.getText().toString();


        if (v.getId() == R.id.LogInId) {
//
//            if(username.equals(""))
//            {
//                Loguser.setError("The filed can't be empty");
//            }
            Log.d("->>>", "okokokk ");
            if (LogPassword.getText().toString().length() == 0) {
                LogPassword.setError("This field can't be empty");
                return;
            }

            Boolean Result = databaseHelper.findpassword(password);
            if (Result == true) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Wrong Password ", Toast.LENGTH_SHORT).show();
                LogPassword.setText("");
            }
        } else if (v.getId() == R.id.Forgotid) {


                //Toast.makeText(getApplicationContext(),"Clicked !",Toast.LENGTH_SHORT).show();
                alertDialogBuilder = new AlertDialog.Builder(this);

                ///title
                alertDialogBuilder.setTitle("Password Recovery ");

                /// message
                alertDialogBuilder.setMessage("What is your father's name?");

                ///icon
                alertDialogBuilder.setIcon(getResources().getDrawable(R.drawable.question));

                input = new EditText(this);
                alertDialogBuilder.setView(input);

                alertDialogBuilder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String father = input.getText().toString();


                        Boolean Result=databaseHelper.findfathername(father);
                        if(Result==true)
                        {
                            //Toast.makeText(getApplicationContext(),"Password recovery ",Toast.LENGTH_SHORT).show();
                            dialog.dismiss();

                       alertDialogBuilder=new AlertDialog.Builder(LoginActivity.this);
                            alertDialogBuilder.setTitle("Password Recovery");
                            alertDialogBuilder.setMessage("New Password");
                            final EditText recovery=new EditText(LoginActivity.this);
                            alertDialogBuilder.setView(recovery);

                            alertDialogBuilder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String newpassword=recovery.getText().toString();

                                    if(newpassword.equals(""))
                                    {
                                        recovery.setError("The field can't be empty");
                                    }
                                    else {


                                        Boolean result = databaseHelper.newPassword(newpassword);
                                        if (result == true)
                                            Toast.makeText(getApplicationContext(), "Password recovery " + newpassword, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                            alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialogBuilder.show();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Wrong Input ! ",Toast.LENGTH_SHORT).show();


                        }


                     //   Toast.makeText(getApplicationContext(), " " + txt, Toast.LENGTH_SHORT).show();

                    }
                });

                alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });


                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialogBuilder.show();
            }


        }
    }

