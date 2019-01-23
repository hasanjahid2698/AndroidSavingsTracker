package com.example.ubuntu.newnew;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class BarActivity extends AppCompatActivity {

    private static Toolbar toolbar;
    private BarChart IncomeBarChart,ExpenseBarChart;
    private ImageButton LeftArrowImageButton,RightArrowImageButton;
    private TextView YearTextView;
    private Date date;
    private ArrayList<ListCollection> arrayListInformation;
    private DatabaseHelper databaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar);

        databaseHelper = new DatabaseHelper(BarActivity.this);
        toolbar = findViewById(R.id.toolbar_Bar_Activity);

        IncomeBarChart = findViewById(R.id.IncomeBarChartId);
        ExpenseBarChart = findViewById(R.id.ExpenseBarChartId);
        YearTextView = findViewById(R.id.YearTextViewId);
        LeftArrowImageButton = findViewById(R.id.LeftArrowImageButtonId);
        RightArrowImageButton = findViewById(R.id.RightArrowImageButtonId);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Bar");

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        date= Calendar.getInstance().getTime();
        dateSetting(date);

        LeftArrowImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String oldDateText = YearTextView.getText().toString();

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

        RightArrowImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String oldDateText = YearTextView.getText().toString();
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



    }

    public void  dateSetting(Date date){
        String pattern = "yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String dateText = simpleDateFormat.format(date);
        YearTextView.setText(dateText);
        refreshActivity(dateText);
    }

    private void refreshActivity(String selectedYear){
        arrayListInformation = databaseHelper.listfromdbondate(selectedYear);
        String[] month=new String[] {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};

        ArrayList<Float> IncomeInfo = new ArrayList<>();
        ArrayList<Float> ExpenseInfo = new ArrayList<>();

        for(int i=0;i<12;i++){IncomeInfo.add(0f); ExpenseInfo.add(0f);}

        for (ListCollection item : arrayListInformation){
            if(item.getOption() == 1){
                //income
                for(int j=0;j<12;j++){
                    if(item.getDate().contains(month[j])){
                        float data = IncomeInfo.get(j);
                        data += item.getAmount();
                        IncomeInfo.remove(j);
                        IncomeInfo.add(j,data);
                        break;
                    }
                }

            }else if(item.getOption()==-1){
                //expense
                for(int j=0;j<12;j++){
                    if(item.getDate().contains(month[j])){
                        float data = ExpenseInfo.get(j);
                        data += item.getAmount();
                        ExpenseInfo.remove(j);
                        ExpenseInfo.add(j,data);
                        break;
                    }
                }
            }
        }

        setUpBarChart(IncomeBarChart,IncomeInfo,1);
        setUpBarChart(ExpenseBarChart,ExpenseInfo,2);

    }


    private  void setUpBarChart(BarChart barChart, ArrayList<Float> entry,int type){
        float maxValue=0f;

        for(int i=0;i<12;i++){
            if(entry.get(i)>maxValue){
                maxValue=entry.get(i);
            }
        }
        float rem = maxValue/1000;
        int m = (int) Math.ceil(rem);
        int max =  m*1000;

        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);
        barChart.setMaxVisibleValueCount(max);
        barChart.setPinchZoom(false);
        barChart.setTouchEnabled(false);
        barChart.setDrawGridBackground(false);

        ArrayList<BarEntry> barEntries = new ArrayList<>();
        for(int i=0;i<12;i++){
            float yValue = entry.get(i);
            barEntries.add( new BarEntry(i,yValue));
        }

        String label;
        int color;
        if(type==1){
            label="Income";
            color= R.color.IncomeBarColor;
        }
        else{
            label="Expense";
            color = R.color.ExpenseBarColor;
        }
        BarDataSet barDataSet = new BarDataSet(barEntries,label);
        barDataSet.setColors(ContextCompat.getColor(this,color));

        BarData barData = new BarData(barDataSet);
        float barWidth = 0.7f;


        String[] month=new String[] {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new MonthXAxisFormatter(month));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelCount(12,false);

        barChart.getAxisRight().setEnabled(false);
        Description desc = new Description();
        desc.setText("");
        barChart.setDescription(desc);
        barChart.animateY(2100, Easing.EasingOption.EaseOutBack);

        Legend legend=barChart.getLegend();
        legend.setForm(Legend.LegendForm.SQUARE);
        legend.setFormSize(10f);
        legend.setPosition(Legend.LegendPosition.ABOVE_CHART_RIGHT);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getAxisLeft().setTextColor(ContextCompat.getColor(this,R.color.colorBackground));
        barChart.getXAxis().setTextColor(ContextCompat.getColor(this,R.color.colorBackground));
        barChart.getAxisLeft().setTextColor(ContextCompat.getColor(this,R.color.colorBackground));


        barChart.setData(barData);
        barData.setBarWidth(barWidth);
    }

    public class MonthXAxisFormatter implements IAxisValueFormatter {

        private String[] Values;
        public MonthXAxisFormatter(String[] Values) {
            this.Values= Values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return Values[(int)value];
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.menu_pieId){
            finish();
            Intent intent = new Intent(BarActivity.this,PieActivity.class);
            startActivity(intent);
        }
        else if(id == android.R.id.home){
            finish();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pie,menu);
        return super.onCreateOptionsMenu(menu);
    }

    public static Date changeYear(Date date, int i) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, i);
        return cal.getTime();
    }
}
