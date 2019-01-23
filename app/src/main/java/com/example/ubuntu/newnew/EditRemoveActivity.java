package com.example.ubuntu.newnew;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditRemoveActivity extends AppCompatActivity {


    public EditText dateEditText, categoryEditText, amountEditText;
//    public Button backButton, doneButton, deleteButton ;
    public DatabaseHelper databaseHelper;

    public String dateText,categoryText;
    public Double amountText;
    public Integer idText,optionText;
    Toolbar toolbar;


    final Calendar calender = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_remove);
        toolbar = findViewById(R.id.toolbar_edit_remove_activity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit/Remove Transaction");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        dateEditText = findViewById(R.id.DateEditTextERAId);
        categoryEditText = findViewById(R.id.CategoryEditTextERAId);
        amountEditText = findViewById(R.id.AmountEditTextERAId);
        databaseHelper = new DatabaseHelper(this);

        Bundle bundle = getIntent().getExtras();
        idText=bundle.getInt("id");
        optionText = bundle.getInt("option");
        dateText = bundle.getString("date");
        categoryText = bundle.getString("category");
        amountText = bundle.getDouble("amount");

        dateEditText.setText(dateText);
        categoryEditText.setText(categoryText);
        amountEditText.setText(Double.toString(amountText));

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

        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new DatePickerDialog(EditRemoveActivity.this, dateSetListener,
                        calender.get(Calendar.YEAR), calender.get(Calendar.MONTH),
                        calender.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        //CategoryEditText Listener

        categoryEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditRemoveActivity.this, TypeSelectionActivity.class);
                intent.putExtra("TypeArray", optionText);
                Toast.makeText(EditRemoveActivity.this, "We are going to Type selection", Toast.LENGTH_SHORT).show();
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
                categoryEditText.setText(selectedItem);
            }
            else if(resultCode== RESULT_CANCELED){
                Toast.makeText(this, "Nothing is selected", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_delete,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id= item.getItemId();
        if(id==android.R.id.home){
            finish();
        }
        else if (id==R.id.deleteId) {
            int new_id = idText;
            Integer deleted = databaseHelper.deleteData(new_id);
            Toast.makeText(EditRemoveActivity.this, deleted+" row is deleted", Toast.LENGTH_SHORT).show();

            Intent resultIntent = new Intent();
            setResult(RESULT_OK,resultIntent);

            finish();
        }
        else if(id == R.id.editId){
            int new_id =idText;
            int selectedOption = optionText;
            String selectedDate = dateEditText.getText().toString();
            String selectedCategory = categoryEditText.getText().toString();
            Double amount = Double.parseDouble(amountEditText.getText().toString());
            Integer updated = databaseHelper.updateData(new_id,selectedOption,selectedDate,selectedCategory,amount);
            Toast.makeText(EditRemoveActivity.this, updated+" row is updated", Toast.LENGTH_SHORT).show();

            Intent resultIntent = new Intent();
            setResult(RESULT_OK,resultIntent);

            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateLabel(){
        String format = "dd MMM yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.US);
        dateEditText.setText(simpleDateFormat.format(calender.getTime()));
    }
}
