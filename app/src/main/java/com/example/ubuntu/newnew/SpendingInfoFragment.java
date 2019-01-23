package com.example.ubuntu.newnew;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;


public class SpendingInfoFragment extends Fragment {

    //variables
    private static Button AddIncomeButton, AddExpenseButton;
    private static TextView incomeTextView, expenseTextView, balanceTextView;
    private ArrayList<ListCollection> arrayListInformation;
    private DatabaseHelper databaseHelper;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_spending_info, container, false);

        AddIncomeButton = view.findViewById(R.id.AddIncomeButtonId);
        AddExpenseButton = view.findViewById(R.id.AddExpenseButtonId);
        incomeTextView = view.findViewById(R.id.IncomeTotalTextViewId);
        expenseTextView = view.findViewById(R.id.ExpenseTotalTextViewId);
        balanceTextView = view.findViewById(R.id.BalanceTotalTextViewId);

        databaseHelper = new DatabaseHelper(getActivity());

//        Toast.makeText(getActivity(), "From Transaction "+ ((SpendingActivity)getActivity()).dateTextView.getText().toString(), Toast.LENGTH_SHORT).show();

        AddIncomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNewActivity(1);
            }
        });

        AddExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNewActivity(-1);
            }
        });

        AddInfoText();

        return view;
    }


    public void openNewActivity(int vl){
        int value;
        value= vl;
        Intent intent = new Intent(getActivity(), InputActivity.class);
        intent.putExtra("OptionType",value);
        startActivityForResult(intent,3);

    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 3){
            if(resultCode == Activity.RESULT_OK){
                AddInfoText();
            }
        }
    }

    private void AddInfoText(){
//        arrayListInformation = databaseHelper.listfromdb();
        arrayListInformation = databaseHelper.listfromdbondate();
        Double incomeAmount, expenseAmount, balanceAmount;

        incomeAmount = expenseAmount = balanceAmount = 0.0;
        for (ListCollection item : arrayListInformation){
            if(item.getOption() == 1){
                incomeAmount += item.getAmount();
            }else{
                expenseAmount += item.getAmount();
            }
        }

        balanceAmount = incomeAmount - expenseAmount;

        incomeTextView.setText("Income      ->"+fixedLengthString(incomeAmount.toString(),20));
        expenseTextView.setText("Expense     ->"+fixedLengthString(expenseAmount.toString(),20));
        balanceTextView.setText("Balance     ->"+fixedLengthString(balanceAmount.toString(),20));


    }

    private static String fixedLengthString(String string, int length){
        String format = "%1$"+length+"s";
        return String.format(format,string);
    }


}
