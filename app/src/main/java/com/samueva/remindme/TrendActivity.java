package com.samueva.remindme;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class TrendActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trend);

        PieChart pieChart = (PieChart) findViewById(R.id.pie_chart);
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setTransparentCircleRadius(0f);

        List<PieEntry> yValues = new ArrayList<PieEntry>();
        yValues.add(new PieEntry(30f, "ValoreA"));
        yValues.add(new PieEntry(30f, "ValoreB"));
        yValues.add(new PieEntry(40f, "ValoreC"));

        PieDataSet dataSet = new PieDataSet(yValues, "DataSet");
        dataSet.setSliceSpace(3f);
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        PieData data = new PieData(dataSet);
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.WHITE);

        pieChart.setData(data);

        PieChart pieChart1 = (PieChart) findViewById(R.id.pie_chart2);

        pieChart1.setData(data);
    }
}
