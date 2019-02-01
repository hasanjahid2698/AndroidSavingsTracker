package com.example.ubuntu.newnew;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private String currentPassword;
    private int currentId;
    private EditText usernameEditText, FatherNameEditText, PhoneNumberEditText, passwordEditText, confirmPasswordEditText;
    DatabaseHelper databaseHelper;
    private ArrayList<String> IncomeArrayList,ExpenseArrayList;
    private ArrayList<UserDetails> allUser;
    private UserDetails currentUser ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        databaseHelper = new DatabaseHelper(this);
        allUser = databaseHelper.findAllUser();
        currentUser = allUser.get(0);



        toolbar = findViewById(R.id.toolbar_profile_activity);
        usernameEditText = findViewById(R.id.usernameProfileEditText);
        FatherNameEditText = findViewById(R.id.fathernameProfileEditText);
        PhoneNumberEditText = findViewById(R.id.phonenumberProfileEditText);
        passwordEditText = findViewById(R.id.passwordProfileEditText);
        confirmPasswordEditText = findViewById(R.id.ConfirmpasswordProfileEditText);
        currentPassword= currentUser.getPassword().toString();
        currentId= currentUser.getId();
        userInfoToProfile(currentUser);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


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
            databaseHelper.deleteAllUser();
            databaseHelper.deleteAllData();
            ExpenseArrayList = Preferences.getArrayPrefsAll("ExpenseList",ProfileActivity.this);
            ExpenseArrayList.clear();
            Preferences.setArrayPrefsAll("ExpenseList",ExpenseArrayList,ProfileActivity.this);
            IncomeArrayList = Preferences.getArrayPrefsAll("IncomeList",ProfileActivity.this);
            IncomeArrayList.clear();
            Preferences.setArrayPrefsAll("IncomeList",IncomeArrayList,ProfileActivity.this);
            MainActivity.mainActivity.finish();
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        else if(id == R.id.editId){
            int passwordLen = passwordEditText.getText().toString().trim().length();
            int confirmPasswordLen = confirmPasswordEditText.getText().toString().trim().length();
            int userNameLen = usernameEditText.getText().toString().trim().length();
            int FathernameLen = FatherNameEditText.getText().toString().trim().length();
            int PhonenumberLen = PhoneNumberEditText.getText().toString().trim().length();
            if(passwordLen==0 && confirmPasswordLen==0){
                if(userNameLen==0 || FathernameLen==0 || PhonenumberLen==0){
                    Toast.makeText(this, "Invalid edit request", Toast.LENGTH_SHORT).show();
                    return false;
                }
                else{
                    updateUserInfo();
                }
                //insert partial data
            }
            else if(passwordLen!=0 && confirmPasswordLen!=0){
                if(passwordEditText.getText().toString().equals(confirmPasswordEditText.getText().toString())){
                    //insert all data
                    currentPassword = passwordEditText.getText().toString();
                    updateUserInfo();
                }
                else{
                    passwordEditText.setText("");
                    confirmPasswordEditText.setText("");
                    Toast.makeText(this, "Password and confirm password isn't same.", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
            else if ((passwordLen==0 && confirmPasswordLen!=0) ||(passwordLen!=0 && confirmPasswordLen==0)){
                Toast.makeText(this, "Invalid edit request", Toast.LENGTH_SHORT).show();
                return false;
            }

            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void userInfoToProfile(UserDetails newUserInfo){
        usernameEditText.setText(newUserInfo.getUsername());
        FatherNameEditText.setText(newUserInfo.getFatherName());
        PhoneNumberEditText.setText(newUserInfo.getPhoneNumber());
    }

    public void updateUserInfo(){
        UserDetails  newUserDetails = new UserDetails();
        newUserDetails.setUsername(usernameEditText.getText().toString());
        newUserDetails.setFatherName(FatherNameEditText.getText().toString());
        newUserDetails.setPhoneNumber(PhoneNumberEditText.getText().toString());
        newUserDetails.setPassword(currentPassword);

        int cnt = databaseHelper.updateUserData(currentId,newUserDetails);
        Toast.makeText(this, cnt+" row updated", Toast.LENGTH_SHORT).show();
        allUser = databaseHelper.findAllUser();
        currentUser = allUser.get(0);
        userInfoToProfile(currentUser);
    };

}
