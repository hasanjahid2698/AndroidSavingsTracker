package com.example.ubuntu.newnew;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class TransactionInfoFragment extends Fragment {


    ListView listView ;
    CustomAdapterForTransaction customAdapterForTransaction;
    ArrayList<ListCollection> arralistInformation;
    DatabaseHelper databaseHelper;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_transaction_info, container, false);
        listView =  view.findViewById(R.id.TransactionListViewId);

        databaseHelper = new DatabaseHelper(getActivity());
//        arralistInformation = databaseHelper.listfromdb();

        arralistInformation = databaseHelper.listfromdbondate();
        customAdapterForTransaction = new CustomAdapterForTransaction(getActivity(),arralistInformation);

        listView.setAdapter(customAdapterForTransaction);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                Toast.makeText(getActivity(), "listClicked", Toast.LENGTH_SHORT).show();
                ListCollection listCollection = arralistInformation.get(position);
                String selectedItemDate, selectedItemCategory;
                int selectedItemId,selectedOption;
                Double selectedItemAmount;
                selectedItemDate = listCollection.getDate();
                selectedItemCategory = listCollection.getCategory();
                selectedItemAmount = listCollection.getAmount();
                selectedItemId = listCollection.getId();
                selectedOption = listCollection.getOption();

                Intent intent = new Intent(getActivity(), EditRemoveActivity.class);
                intent.putExtra("date",selectedItemDate);
                intent.putExtra("category",selectedItemCategory);
                intent.putExtra("amount",selectedItemAmount);
                intent.putExtra("id",selectedItemId);
                intent.putExtra("option",selectedOption);
                startActivityForResult(intent, 1);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1){
            if(resultCode == Activity.RESULT_OK){
//                arralistInformation = databaseHelper.listfromdb();
                arralistInformation = databaseHelper.listfromdbondate();
                customAdapterForTransaction = new CustomAdapterForTransaction(getActivity(),arralistInformation);
                listView.setAdapter(customAdapterForTransaction);
                Toast.makeText(getActivity(), "Called", Toast.LENGTH_SHORT).show();
            }
        }


    }



}
