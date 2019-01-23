package com.example.ubuntu.newnew;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class InputActivity extends AppCompatActivity {

    public EditText DateEditText, CategoryEditText,AmountEditText;
    private static Toolbar toolbarInput;

    Context context ;
    DatabaseHelper databaseHelper;
    SQLiteDatabase sqLiteDatabase;

    final Calendar calender = Calendar.getInstance();
    public int value;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);


        databaseHelper = new DatabaseHelper(this);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            value = bundle.getInt("OptionType");
            Toast.makeText(this, "Value received" + value , Toast.LENGTH_SHORT).show();
        }

        //clickable EditText handling
        toolbarInput = findViewById(R.id.toolbar_input_activity);
        DateEditText = (EditText) findViewById(R.id.DateEditTextId);
        CategoryEditText = (EditText) findViewById(R.id.CategoryEditTextId);
        AmountEditText = (EditText) findViewById(R.id.AmountEditTextId);

        setSupportActionBar(toolbarInput);
        if(value == 1) getSupportActionBar().setTitle("Input An Income");
        else if(value == -1 ) getSupportActionBar().setTitle("Input An Expense");


        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        //DatePicker Listener
        final DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int Year, int monthOfYear, int dayOfMonth) {
                calender.set(Calendar.YEAR,Year);
                calender.set(Calendar.MONTH,monthOfYear);
                calender.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                updateLabel();
            }
        };

        //DateEditText Listener

        DateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new DatePickerDialog(InputActivity.this, dateSetListener,
                        calender.get(Calendar.YEAR), calender.get(Calendar.MONTH),
                        calender.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //CategoryEditText Listener

        CategoryEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InputActivity.this, TypeSelectionActivity.class);
                intent.putExtra("TypeArray", value);
                Toast.makeText(InputActivity.this, "We are going to Type selection", Toast.LENGTH_SHORT).show();
                startActivityForResult(intent,1);
            }
        });





    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1){
            if(resultCode== RESULT_OK){
                String selectedItem = data.getStringExtra("Selected");
                Toast.makeText(this, selectedItem, Toast.LENGTH_SHORT).show();
                CategoryEditText.setText(selectedItem);
            }
            else if(resultCode== RESULT_CANCELED){
                Toast.makeText(this, "Nothing is selected", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateLabel(){
        String format = "dd MMM yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.US);
        DateEditText.setText(simpleDateFormat.format(calender.getTime()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();

        }
        else if (id == R.id.doneId){
            int selectedOption;
            String selectedDate, selectedCategory;
            Double selectedAmount;
            selectedOption = value;
            selectedDate = DateEditText.getText().toString();
            selectedCategory = CategoryEditText.getText().toString();
            selectedAmount = Double.parseDouble(AmountEditText.getText().toString());

            Boolean inserted = databaseHelper.insertData(selectedOption,selectedDate,selectedCategory,selectedAmount);

            if(inserted) Toast.makeText(InputActivity.this, "Data is inserted", Toast.LENGTH_SHORT).show();

            Intent resultIntent = new Intent();
            setResult(RESULT_OK,resultIntent);

            finish();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_done,menu);
        return super.onCreateOptionsMenu(menu);
    }




}
