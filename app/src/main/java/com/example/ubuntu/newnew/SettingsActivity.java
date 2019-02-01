package com.example.ubuntu.newnew;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {

    private Button clearDataButton, clearIncomeButton, clearExpenseButton;
    DatabaseHelper databaseHelper;
    private static ArrayList<String> IncomeArrayList,ExpenseArrayList;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        toolbar = findViewById(R.id.toolbar_setting_activity);

        clearDataButton = findViewById(R.id.ClearDataButtonId);
        clearIncomeButton = findViewById(R.id.ClearIncomeButtonId);
        clearExpenseButton = findViewById(R.id.ClearExpenseButtonId);
        databaseHelper = new DatabaseHelper(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Settings");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        clearDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);

                ///title
                builder.setTitle("Alert Massage ");

                /// message
                builder.setMessage("Do you want to clear all data?");

                ///icon
                builder.setIcon(getResources().getDrawable(R.drawable.question));

                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        databaseHelper.deleteAllData();
                        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();


            }
        });

        clearIncomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);

                ///title
                builder.setTitle("Alert Massage ");

                /// message
                builder.setMessage("Do you want to clear all income list?");

                ///icon
                builder.setIcon(getResources().getDrawable(R.drawable.question));

                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        IncomeArrayList = Preferences.getArrayPrefsAll("IncomeList",SettingsActivity.this);
                        IncomeArrayList.clear();
                        Preferences.setArrayPrefsAll("IncomeList",IncomeArrayList,SettingsActivity.this);

                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();

            }
        });

        clearExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);

                ///title
                builder.setTitle("Alert Massage ");

                /// message
                builder.setMessage("Do you want to clear all expense list?");

                ///icon
                builder.setIcon(getResources().getDrawable(R.drawable.question));

                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ExpenseArrayList = Preferences.getArrayPrefsAll("ExpenseList",SettingsActivity.this);
                        ExpenseArrayList.clear();
                        Preferences.setArrayPrefsAll("ExpenseList",ExpenseArrayList,SettingsActivity.this);

                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();

            }
        });


    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
