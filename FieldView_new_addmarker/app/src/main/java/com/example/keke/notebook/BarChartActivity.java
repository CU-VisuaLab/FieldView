package com.example.keke.notebook;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;

import java.util.ArrayList;

public class BarChartActivity extends AppCompatActivity {

    BarChart barChart;
    float barWidth = 0.3f;
    float barSpace = 0f;
    float groupSpace = 0.4f;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_chart);

        barChart = findViewById(R.id.bargraph);
        barChart.getDescription().setEnabled(false);
        barChart.setFitBars(true);

        //Data for Display
        int groupCount = 11;
        ArrayList xVals = new ArrayList();
        xVals.add("2005");
        xVals.add("2006");
        xVals.add("2007");
        xVals.add("2008");
        xVals.add("2009");
        xVals.add("2010");
        xVals.add("2011");
        xVals.add("2012");
        xVals.add("2013");
        xVals.add("2014");
        xVals.add("2015");

        ArrayList yVals1 = new ArrayList();
        ArrayList yVals2 = new ArrayList();

        yVals1.add(new BarEntry(1, (float) 5635));
        yVals2.add(new BarEntry(1, (float) 845));
        yVals1.add(new BarEntry(2, (float) 5275));
        yVals2.add(new BarEntry(2, (float) 780));
        yVals1.add(new BarEntry(3, (float) 5200));
        yVals2.add(new BarEntry(3, (float) 792));
        yVals1.add(new BarEntry(4, (float) 4900));
        yVals2.add(new BarEntry(4, (float) 825));
        yVals1.add(new BarEntry(5, (float) 4882));
        yVals2.add(new BarEntry(5, (float) 816));
        yVals1.add(new BarEntry(6, (float) 4362));
        yVals2.add(new BarEntry(6, (float) 815));
        yVals1.add(new BarEntry(7, (float) 3793));
        yVals2.add(new BarEntry(7, (float) 735));
        yVals1.add(new BarEntry(8, (float) 4037));
        yVals2.add(new BarEntry(8, (float) 713));
        yVals1.add(new BarEntry(9, (float) 3753));
        yVals2.add(new BarEntry(9, (float) 651));
        yVals1.add(new BarEntry(10, (float) 3443));
        yVals2.add(new BarEntry(10, (float) 595));
        yVals1.add(new BarEntry(11, (float) 3563));
        yVals2.add(new BarEntry(11, (float) 603));

        //Draw graph
        BarDataSet set1, set2;
        set1 = new BarDataSet(yVals2, "Lives Lost");
        set2 = new BarDataSet(yVals1, "Lives Saved");
        set1.setColor(Color.rgb(242, 203, 84));
        set2.setColor(Color.rgb(137, 186, 164));
        BarData data = new BarData(set1, set2);
        data.setValueFormatter(new LargeValueFormatter());
        barChart.setData(data);
        barChart.getBarData().setBarWidth(barWidth);
        barChart.getXAxis().setAxisMinimum(0.2f);
        barChart.getXAxis().setAxisMaximum(0.2f +
                barChart.getBarData().getGroupWidth(groupSpace, barSpace) * groupCount);
        barChart.groupBars(0.1f, groupSpace, barSpace);
        barChart.getData().setHighlightEnabled(true);
        barChart.invalidate();
        barChart.animateY(500);


        //Draw Indicator
        Legend l = barChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(true);
        l.setYOffset(20f);
        l.setXOffset(20f);
        l.setYEntrySpace(10f);
        l.setTextSize(8f);

        //Draw X-axis & Y-axis
        XAxis xAxis = barChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setCenterAxisLabels(true);
        xAxis.setLabelCount(11);
        xAxis.setDrawGridLines(false);
        xAxis.setAxisMaximum(11);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xVals));

        barChart.getAxisRight().setEnabled(false);
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setDrawGridLines(true);
        leftAxis.setSpaceTop(10f);
        leftAxis.setAxisMinimum(1f);
    }
}