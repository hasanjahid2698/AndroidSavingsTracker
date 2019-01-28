package com.example.ubuntu.newnew;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PieActivity extends AppCompatActivity {


    private static Toolbar toolbar;
    private PieChart IncomePieChart,ExpensePieChart;
    private ImageButton PieLeftArrowImageButton,PieRightArrowImageButton;
    private TextView PieYearTextView;
    private Date date;
    private String selectedDate;
    private ArrayList<ListCollection> arrayListInformation;
    private DatabaseHelper databaseHelper;
    public float y[]={0f,1f,2f,3f,4f,5f,6f,7f,8f,9f,10f,11f};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie);

        databaseHelper = new DatabaseHelper(PieActivity.this);
        toolbar = findViewById(R.id.toolbar_Pie_Activity);

        IncomePieChart = findViewById(R.id.IncomePieChartId);
        ExpensePieChart = findViewById(R.id.ExpensePieChartId);
        PieYearTextView = findViewById(R.id.PieYearTextViewId);
        PieLeftArrowImageButton = findViewById(R.id.PieLeftArrowImageButtonId);
        PieRightArrowImageButton = findViewById(R.id.PieRightArrowImageButtonId);



        date= Calendar.getInstance().getTime();
        dateSetting(date);

        PieLeftArrowImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String oldDateText = PieYearTextView.getText().toString();

                String pattern = "yyyy";

                Date oldDate = new Date();
                Date newDate;

                SimpleDateFormat format = new SimpleDateFormat(pattern);
                try {
                    oldDate = format.parse(oldDateText);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                newDate = changeYear(oldDate, -1);


                dateSetting(newDate);
            }
        });

        PieRightArrowImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String oldDateText = PieYearTextView.getText().toString();
                String pattern = "yyyy";

                Date oldDate = new Date();
                Date newDate;

                SimpleDateFormat format = new SimpleDateFormat(pattern);
                try {
                    oldDate = format.parse(oldDateText);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                newDate = changeYear(oldDate, 1);


                dateSetting(newDate);
            }


        });

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Pie");

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadIncomePieChart();
        loadExpensePieChart();
    }

    private void loadIncomePieChart() {

        Double [] income = new Double[12];
        Double [] expense = new Double[12];

        arrayListInformation = databaseHelper.listfromdbondate(selectedDate);
        for (int i = 0; i < 12; ++i) {
            income[i] = 0.0;
            expense[i] = 0.0;
        }
        String[] string = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        for (ListCollection item : arrayListInformation) {

            for (int i = 0; i < 12; i++) {
                if (item.getDate().contains(string[i]) && item.getAmount() != null && item.getOption() == 1) {
                    income[i] += item.getAmount();
                } else if (item.getDate().contains(string[i]) && item.getOption() == -1) {
                    expense[i] += item.getAmount();
                }
            }
        }
//        double out = 0, in = 0;
//        String last = "";
//        for (int i = 0; i < 12; ++i) {
//            last += string[i];
//            last += " in " + income[i] + "\n";
//            in += income[i];
//            out += expense[i];
//        }

        List<PieEntry> pieEntries = new ArrayList<>();
        for (int i = 0; i < y.length; i++) {
            double rr = income[i];
            if(rr !=0.0)
                pieEntries.add(new PieEntry((float) rr, string[i]));
        }

        PieDataSet dataSet = new PieDataSet(pieEntries, "");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData data = new PieData(dataSet);
        PieChart chart = findViewById(R.id.IncomePieChartId);
        Description desc = new Description();
        desc.setText("");
        chart.setDescription(desc);
        chart.setData(data);
        chart.setCenterText("Income");
        chart.setHoleRadius(35f);
        chart.setTransparentCircleAlpha(0);
        chart.setCenterTextSize(14);
        chart.setHoleColor(Color.LTGRAY);
        chart.setCenterTextColor(Color.WHITE);
        chart.animateY(2000);
        chart.invalidate();
    }

    private void loadExpensePieChart() {

        Double [] income = new Double[12];
        Double [] expense = new Double[12];

        arrayListInformation = databaseHelper.listfromdbondate(selectedDate);
        for (int i = 0; i < 12; ++i) {
            income[i] = 0.0;
            expense[i] = 0.0;
        }
        String[] string = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        for (ListCollection item : arrayListInformation) {

            for (int i = 0; i < 12; i++) {
                if (item.getDate().contains(string[i]) && item.getAmount() != null && item.getOption() == 1) {
                    income[i] += item.getAmount();
                } else if (item.getDate().contains(string[i]) && item.getOption() == -1) {
                    expense[i] += item.getAmount();
                }
            }
        }
//        double out = 0, in = 0;
//        String last = "";
//        for (int i = 0; i < 12; ++i) {
//            last += string[i];
//            last += " in " + expense[i] + "\n";
//            in += income[i];
//            out += expense[i];
//        }

        List<PieEntry> pieEntries = new ArrayList<>();
        //ex
        int cnt=0;
        for (int i = 0; i < y.length; i++) {
            double rr = expense[i];
            cnt++;
            if(rr !=0.0)
            pieEntries.add(new PieEntry((float) rr, string[i]));
        }
        Toast.makeText(this, "entry "+cnt, Toast.LENGTH_SHORT).show();

        PieDataSet dataSet = new PieDataSet(pieEntries, "");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData data = new PieData(dataSet);
        PieChart chart = findViewById(R.id.ExpensePieChartId);
        Description desc = new Description();
        desc.setText("");
        chart.setDescription(desc);
        chart.setData(data);
        chart.setCenterText("Expense");
        chart.setHoleRadius(35f);
        chart.setTransparentCircleAlpha(0);
        chart.setCenterTextSize(14);
        chart.setHoleColor(Color.LTGRAY);
        chart.setCenterTextColor(Color.WHITE);
        chart.animateY(2000);
        chart.invalidate();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.menu_barId){
            finish();
            Intent intent = new Intent(PieActivity.this,BarActivity.class);
            startActivity(intent);
        }
        else if(id == android.R.id.home){
            finish();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bar,menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void  dateSetting(Date date){
        String pattern = "yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String dateText = simpleDateFormat.format(date);
        PieYearTextView.setText(dateText);
        selectedDate=dateText;
        loadExpensePieChart();
        loadIncomePieChart();
//        refreshActivity(dateText);
    }

    public static Date changeYear(Date date, int i) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, i);
        return cal.getTime();
    }
}
