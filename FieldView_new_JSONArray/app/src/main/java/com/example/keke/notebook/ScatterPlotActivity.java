package com.example.keke.notebook;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

public class ScatterPlotActivity extends AppCompatActivity{

    private static final String TAG = "ScatterplotActivity";
    private LineChart lineChart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scatter_plot);

        lineChart = (LineChart) findViewById(R.id.linechart);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(false);
        lineChart.getDescription().setEnabled(false);
        lineChart.getLegend().setEnabled(false);
        lineChart.getAxisRight().setEnabled(false);

        //Draw YAxis
        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setAxisMaximum(70f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setSpaceTop(10f);
        leftAxis.setSpaceBottom(10f);
        leftAxis.setCenterAxisLabels(true);
        leftAxis.setYOffset(19f);


        //Define YAxis Values
        ArrayList<Entry> yVals = new ArrayList<>();
        yVals.add(new Entry(0,8));
        yVals.add(new Entry(1,27));
        yVals.add(new Entry(2,24));
        yVals.add(new Entry(3,17));
        yVals.add(new Entry(4,64));
        yVals.add(new Entry(5,30));
        yVals.add(new Entry(6,23));
        yVals.add(new Entry(7,13));
        yVals.add(new Entry(8,3));
        yVals.add(new Entry(9,2));
        yVals.add(new Entry(10,5));
        yVals.add(new Entry(11,6));


        LineDataSet set1 = new LineDataSet(yVals,"");
        set1.setColor(Color.rgb(7, 87, 91));
        set1.setLineWidth(2f);
        set1.setCircleColor(Color.rgb( 102, 165, 173));
        set1.setCircleColorHole(Color.rgb( 102, 165, 173));
        set1.setCircleRadius(3f);
        set1.setValueTextSize(8f);
        set1.setValueTextColor(Color.rgb(0, 68, 69));

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        LineData data = new LineData(dataSets);
        lineChart.setData(data);
        lineChart.animateX(1000);

        //Define XAxis Labels
        ArrayList xVals = new ArrayList();
        xVals.add("Jan");
        xVals.add("Feb");
        xVals.add("Mar");
        xVals.add("Apr");
        xVals.add("May");
        xVals.add("Jun");
        xVals.add("Jul");
        xVals.add("Aug");
        xVals.add("Sep");
        xVals.add("Oct");
        xVals.add("Nov");
        xVals.add("Dec");

        //Draw XAxis
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xVals));
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelCount(12);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
    }



}