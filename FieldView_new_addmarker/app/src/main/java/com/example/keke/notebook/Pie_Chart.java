package com.example.keke.notebook;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class Pie_Chart extends AppCompatActivity {

    PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie__chart);

        pieChart = findViewById(R.id.piechart);
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);

        pieChart.setDragDecelerationFrictionCoef(0.95f);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(80f);
        pieChart.setDrawSliceText(false);
        pieChart.animateY(800, Easing.EasingOption.EaseInCubic);

        ArrayList<PieEntry> yVals = new ArrayList<>();
        yVals.add(new PieEntry(6f, "Human Related"));
        yVals.add(new PieEntry(8f, "Lightning"));
        yVals.add(new PieEntry(1f, "Undetermined"));
        yVals.add(new PieEntry(3f, "Powerlines"));
        yVals.add(new PieEntry(1f, "Under Investigation"));
        yVals.add(new PieEntry(1f, "Illegal Campfire"));

        Description description = new Description();
        description.setText("Causes of Top 20 Largest California Wildfires");
        description.setTextSize(15);
        description.setPosition(900f,50f);
        pieChart.setDescription(description);


        PieDataSet dataSet = new PieDataSet(yVals, "Causes");
        dataSet.setSliceSpace(0f);
        dataSet.setSelectionShift(20f);
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);


        PieData data = new PieData((dataSet));
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.BLACK);

        pieChart.setData(data);


        Legend l = pieChart.getLegend();
        l.setXOffset(125f);
        l.setYOffset(10f);
    }
}
