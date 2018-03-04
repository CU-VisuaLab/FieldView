package com.example.keke.notebook;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class ScatterPlotActivity extends AppCompatActivity {

    ScatterChart scatterChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scatter_plot);

        scatterChart = findViewById(R.id.scatterPlot);
        scatterChart.getDescription().setEnabled(false);

        setData(10);
    }
    private void setData(int count){
        ArrayList<Entry> yVals = new ArrayList<>();

        for(int i = 0; i < count; i++){
            float value = (float) (Math.random()*100);
            yVals.add(new BarEntry(i, (int) value));
        }

        ScatterDataSet set = new ScatterDataSet(yVals, "Data Set");
        set.setColors(ColorTemplate.LIBERTY_COLORS);
        set.setDrawValues(true);

        ScatterData data = new ScatterData(set);
        scatterChart.setData(data);
        scatterChart.invalidate();
        scatterChart.animateY(500);
    }

}
