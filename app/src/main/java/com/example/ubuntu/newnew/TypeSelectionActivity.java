package com.example.ubuntu.newnew;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class TypeSelectionActivity extends AppCompatActivity {

    //variables
    private static int value;
    private static String msg;
    private static Button addButton,backButton;
    private static ListView listView;
    private static ArrayList<String> IncomeArrayList,ExpenseArrayList;
    private static ArrayAdapter<String> arrayAdapter ;
    private static Toolbar toolbarTypeSelection;
//    CustomAdapterForType  customAdapterForType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_selection);

        valueReceiveFromActivity();
        bindingViewWithId();


        setSupportActionBar(toolbarTypeSelection);
        if(value == 1) getSupportActionBar().setTitle("Select An Income");
        else if(value == -1 ) getSupportActionBar().setTitle("Select An Expense");


        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ListViewSetAdapter();
        listViewClickedListener();


    }


    //ButtonClickedInPreviousActivity
    private void valueReceiveFromActivity(){
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            value = bundle.getInt("TypeArray");
        }
    }

    private void bindingViewWithId(){
        toolbarTypeSelection = findViewById(R.id.toolbar_type_selectionId);
        listView = findViewById(R.id.listViewId);
    }

    private void ListViewSetAdapter(){

        if(value == 1){
            IncomeArrayList = Preferences.getArrayPrefs("IncomeList",TypeSelectionActivity.this);
//            customAdapterForType = new CustomAdapterForType(TypeSelectionActivity.this,IncomeArrayList);
            arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,IncomeArrayList);
        }
        else if(value == -1){
            ExpenseArrayList = Preferences.getArrayPrefs("ExpenseList",TypeSelectionActivity.this);
//            customAdapterForType = new CustomAdapterForType(TypeSelectionActivity.this,ExpenseArrayList);
            arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,ExpenseArrayList);

        }
        listView.setAdapter(arrayAdapter);
    }

    private void listViewClickedListener(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                Toast.makeText(TypeSelectionActivity.this, "Hi", Toast.LENGTH_SHORT).show();
                String item = listView.getItemAtPosition(position).toString();
                Intent intent = new Intent();
                intent.putExtra("Selected",item);
                setResult(RESULT_OK,intent);
                finish();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {

//                Toast.makeText(TypeSelectionActivity.this, "Long clicked called", Toast.LENGTH_SHORT).show();

                AlertDialog.Builder builder = new AlertDialog.Builder(TypeSelectionActivity.this);

                builder.setTitle("Edit or Delete");

                final EditText input = new EditText(TypeSelectionActivity.this);
                if(value == 1 ) input.setText(IncomeArrayList.get(position).toString());
                else if(value == -1) input.setText(ExpenseArrayList.get(position).toString());
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        msg = input.getText().toString();
                        if(msg.length()==0){
                            View parentLayout = findViewById(android.R.id.content);
                            Snackbar.make(parentLayout,"Empty input is not allowed",Snackbar.LENGTH_LONG).show();
                            return;
                        }
                        else{

//                            Toast.makeText(TypeSelectionActivity.this, msg, Toast.LENGTH_SHORT).show();
                            if(value == 1){
                                IncomeArrayList.set(position,msg);
                                Preferences.setArrayPrefs("IncomeList",IncomeArrayList,TypeSelectionActivity.this);
                            }
                            else if(value == -1){
                                ExpenseArrayList.set(position,msg);
                                Preferences.setArrayPrefs("ExpenseList",ExpenseArrayList,TypeSelectionActivity.this);

                            }

                            arrayAdapter.notifyDataSetChanged();
                        }



                    }
                });

                builder.setNegativeButton("Remove", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (value == 1){
                            IncomeArrayList.remove(position);
                            Preferences.setArrayPrefs("IncomeList",IncomeArrayList,TypeSelectionActivity.this);

                        }
                        else if( value == -1){
                            ExpenseArrayList.remove(position);
                            Preferences.setArrayPrefs("ExpenseList",ExpenseArrayList,TypeSelectionActivity.this);

                        }
                        arrayAdapter.notifyDataSetChanged();
                        dialog.cancel();
                    }
                });

                builder.show();

                return true;
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            Intent intent = new Intent();
            setResult(RESULT_CANCELED,intent);
            finish();

        }
        else if (id == R.id.addId){
            final AlertDialog.Builder builder = new AlertDialog.Builder(TypeSelectionActivity.this);
            if(value == 1){
                builder.setTitle("Add An Income ");
            }
            else if(value == -1){
                builder.setTitle("Add An Expense");
            }


            final EditText input = new EditText(TypeSelectionActivity.this);
            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
            builder.setView(input);



            builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    msg = input.getText().toString();
                    if(msg.trim().length()==0){
                        Toast.makeText(TypeSelectionActivity.this, "Enter A Name", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(TypeSelectionActivity.this, msg, Toast.LENGTH_SHORT).show();

                        if(value == 1){
                            IncomeArrayList.add(msg);
                            Preferences.setArrayPrefs("IncomeList",IncomeArrayList,TypeSelectionActivity.this);
                        }
                        else if(value == -1){
                            ExpenseArrayList.add(msg);
                            Preferences.setArrayPrefs("ExpenseList",ExpenseArrayList,TypeSelectionActivity.this);

                        }

                        arrayAdapter.notifyDataSetChanged();
                    }
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
        return super.onOptionsItemSelected(item);
    }


}
