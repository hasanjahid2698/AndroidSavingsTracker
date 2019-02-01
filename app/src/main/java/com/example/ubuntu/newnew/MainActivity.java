package com.example.ubuntu.newnew;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener , View.OnClickListener {


    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    public static MainActivity mainActivity;

    private Button SpendingButton, TransactionButton;
    DatabaseHelper databaseHelper;
    private ImageButton addImageButton,minusImageButton,optionImageButton;
    private TextView dateTextView;
    private static int selectedFragment;
    private static String selectedDate;

    Date date;
    String dateText;
    String pattern;

    String items[]={"Daily","Monthly","Yearly"};
    int option;
    ArrayList<ListCollection> arrayListInformation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        selectedFragment=1;
        mainActivity = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer);
        toolbar = findViewById(R.id.toolbar_main_activity);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Savings Tracker");

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle  = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open_drawer,R.string.close_drawer);
        drawerLayout.setDrawerListener(toggle);

        toggle.syncState();


        SpendingButton = findViewById(R.id.SpendingInfoButtonId);
        TransactionButton = findViewById(R.id.TransactionInfoButtonId);

        addImageButton = findViewById(R.id.RightArrowButtonId);
        minusImageButton = findViewById(R.id.LeftArrowButtonId);
        optionImageButton = findViewById(R.id.MenuOptionButtonId);
        dateTextView = findViewById(R.id.SelectedDateTextViewId);

        option=2;

        date = Calendar.getInstance().getTime();
        dateSetting(option,date);
        selectedDate = dateTextView.getText().toString();
        databaseHelper = new DatabaseHelper(MainActivity.this,selectedDate);

        SpendingButton.setOnClickListener(this);
        TransactionButton.setOnClickListener(this);

        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String oldDateText = dateTextView.getText().toString();

                if(option==1) pattern = "dd MMM yyyy";
                else if(option==2) pattern = "MMM yyyy";
                else pattern = "yyyy";

                Date oldDate = new Date();
                Date newDate;

                SimpleDateFormat format = new SimpleDateFormat(pattern);
                try {
                    oldDate = format.parse(oldDateText);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if(option==1)
                    newDate = changeDay(oldDate,1);
                else if(option==2)
                    newDate = changeMonth(oldDate,1);
                else
                    newDate = changeYear(oldDate,1);


                dateSetting(option,newDate);



            }
        });

        minusImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String oldDateText = dateTextView.getText().toString();

                if(option==1) pattern = "dd MMM yyyy";
                else if(option==2) pattern = "MMM yyyy";
                else pattern = "yyyy";

                Date oldDate = new Date();
                Date newDate;

                SimpleDateFormat format = new SimpleDateFormat(pattern);
                try {
                    oldDate = format.parse(oldDateText);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if(option==1)
                    newDate = changeDay(oldDate,-1);
                else if(option==2)
                    newDate = changeMonth(oldDate,-1);
                else
                    newDate = changeYear(oldDate,-1);


                dateSetting(option,newDate);


            }
        });

        optionImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertWithRadioButtons();

            }
        });

    }

    @Override
    public void onClick(View view) {

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("InfoArrayList",arrayListInformation);

        android.support.v4.app.Fragment fragment;
        if(view.getId()==R.id.SpendingInfoButtonId){
            selectedFragment =1;
            Toast.makeText(this, "Spending Button is clicked", Toast.LENGTH_SHORT).show();
            fragment= new SpendingInfoFragment();
            //xx
            fragment.setArguments(bundle);
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.FragmentId,fragment);
            fragmentTransaction.commit();
        }
        else if(view.getId()==R.id.TransactionInfoButtonId){
            selectedFragment = 2;
            Toast.makeText(this, "Transaction Button is clicked", Toast.LENGTH_SHORT).show();
            fragment= new TransactionInfoFragment();
            //xx
            fragment.setArguments(bundle);
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.FragmentId,fragment);
            fragmentTransaction.commit();
        }

    }


    public void  dateSetting(int option,Date date){
        if(option==1) pattern = "dd MMM yyyy";
        else if(option==2) pattern = "MMM yyyy";
        else pattern = "yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        dateText = simpleDateFormat.format(date);
        dateTextView.setText(dateText);
        databaseHelper = new DatabaseHelper(this);
        databaseHelper.setSelectedDate(dateText);
        openFragment();

    }

    public static Date changeDay(Date date, int i) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_YEAR, i);
        return cal.getTime();
    }
    public static Date changeMonth(Date date, int i) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, i);
        return cal.getTime();
    }
    public static Date changeYear(Date date, int i) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, i);
        return cal.getTime();
    }

    private void showAlertWithRadioButtons(){

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Show Spending");
        builder.setSingleChoiceItems(items, option-1,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        option=item+1;
                    }
                }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dateSetting(option,Calendar.getInstance().getTime());
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void openFragment(){
        android.support.v4.app.Fragment fragment;
        if(selectedFragment==1){
            fragment= new SpendingInfoFragment();
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.FragmentId,fragment);
            fragmentTransaction.commit();
        }
        else if(selectedFragment==2){
            fragment= new TransactionInfoFragment();
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.FragmentId,fragment);
            fragmentTransaction.commit();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Intent intent = new Intent();
        switch (id){
            case R.id.profile_id:
                intent= new Intent(MainActivity.this,ProfileActivity.class);
                break;
            case R.id.Pie_id:
                intent= new Intent(MainActivity.this,PieActivity.class);
                break;
            case R.id.Bar_id:
                intent= new Intent(MainActivity.this,BarActivity.class);
                break;
            case R.id.setting_id:
                intent= new Intent(MainActivity.this,SettingsActivity.class);
                break;
            case R.id.log_out_id:
                Toast.makeText(MainActivity.this, "Nope plz :(", Toast.LENGTH_SHORT).show();
                onBackPressed();
                break;
            case R.id.credits_id:
                intent= new Intent(MainActivity.this,Credits.class);
                break;

                }
        drawerLayout.closeDrawer(GravityCompat.START);
        startActivity(intent);
        return false;
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else
            super.onBackPressed();
    }
}
