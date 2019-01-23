package com.example.ubuntu.newnew;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapterForTransaction extends BaseAdapter {


    List<ListCollection> collectionlist;
    Context context;

    private LayoutInflater inflater ;

    CustomAdapterForTransaction(Context context , List<ListCollection> collectionlist){

        this.context = context;
        this.collectionlist= collectionlist;

    }



    @Override
    public int getCount() {
        return collectionlist.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if(view == null){
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.transaction_listview_layout,viewGroup,false);

        }

        ListCollection listCollection = collectionlist.get(position);

        TextView CategoryTextView =  view.findViewById(R.id.categoryTextViewId);
        TextView DateTextView =  view.findViewById(R.id.DateTextViewId);
        TextView AmountTextView =  view.findViewById(R.id.AmountTextViewId);

        CategoryTextView.setText(listCollection.getCategory());
        DateTextView.setText(listCollection.getDate());
        AmountTextView.setText(listCollection.getAmount().toString());

        return view;
    }
}
