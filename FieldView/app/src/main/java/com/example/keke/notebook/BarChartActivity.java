package com.example.keke.notebook;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class BarChartActivity extends AppCompatActivity {

    BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_chart);

        barChart = findViewById(R.id.bargraph);
        barChart.getDescription().setEnabled(false);

        setData(10);
        barChart.setFitBars(true);
    }

    private void setData(int count){
        ArrayList<BarEntry> yVals = new ArrayList<>();

        for(int i = 0; i < count; i++){
            float value = (float) (Math.random()*100);
            yVals.add(new BarEntry(i, (int) value));
        }

        BarDataSet set = new BarDataSet(yVals, "Data Set");
        set.setColors(ColorTemplate.LIBERTY_COLORS);
        set.setDrawValues(true);

        BarData data = new BarData(set);
        barChart.setData(data);
        barChart.invalidate();
        barChart.animateY(500);
    }
}