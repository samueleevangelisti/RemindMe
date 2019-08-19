package com.samueva.remindme;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class TrendActivity extends AppCompatActivity {

    // TODO: 8/19/19 STRING DI DEBUG
    private static final String TAG = "ReMe_TrendActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trend);

        final PieChart pieChartTasks = (PieChart) findViewById(R.id.pie_chart_tasks);
        pieChartTasks.setUsePercentValues(true);
        pieChartTasks.getDescription().setEnabled(false);
        pieChartTasks.setDrawHoleEnabled(true);
        pieChartTasks.setTransparentCircleAlpha(0);
        pieChartTasks.getLegend().setEnabled(false);

        final List<PieEntry> yValuesTasks = new ArrayList<PieEntry>();

        final PieChart pieChartDoneTasks = (PieChart) findViewById(R.id.pie_chart_done_tasks);
        pieChartDoneTasks.setUsePercentValues(true);
        pieChartDoneTasks.getDescription().setEnabled(false);
        pieChartDoneTasks.setDrawHoleEnabled(true);
        pieChartDoneTasks.setTransparentCircleAlpha(0);
        pieChartDoneTasks.getLegend().setEnabled(false);

        final List<PieEntry> yValuesDoneTasks = new ArrayList<PieEntry>();

        new DbAsyncTask(AppDatabase.getInstance(), dbAction.CATEGORY_GETALL, new DbAsyncTask.DbAsyncTaskListener() {
            @Override
            public void onTaskGetByIdCallback(Task task) {

            }

            @Override
            public void onTaskGetAllByStatusCallback(List<Task> taskList) {

            }

            @Override
            public void onTaskUpdateCallback(int taskId) {

            }

            @Override
            public void onTaskGetAllByFiltersCallback(List<Task> taskList) {

            }

            @Override
            public void onCategoryGetAllCallback(List<TaskCategory> categoryList) {
                for (TaskCategory category : categoryList) {
                    if (category.getTasks() > 0) {
                        yValuesTasks.add(new PieEntry(category.getTasks(), category.getName()));
                    }
                    if (category.getHistoryTasks() > 0) {
                        yValuesDoneTasks.add(new PieEntry(category.getHistoryTasks(), category.getName()));
                    }
                }

                if (yValuesTasks.size() > 0) {
                    PieDataSet dataSetTasks = new PieDataSet(yValuesTasks, "");
                    dataSetTasks.setSliceSpace(3f);
                    dataSetTasks.setColors(ColorTemplate.MATERIAL_COLORS);

                    PieData dataTasks = new PieData(dataSetTasks);
                    dataTasks.setValueTextSize(15f);
                    dataTasks.setValueTextColor(Color.WHITE);

                    pieChartTasks.setData(dataTasks);
                    pieChartDoneTasks.notifyDataSetChanged();
                    pieChartTasks.invalidate();
                }

                if (yValuesDoneTasks.size() > 0) {
                    PieDataSet dataSetDoneTasks = new PieDataSet(yValuesDoneTasks, "");
                    dataSetDoneTasks.setSliceSpace(3f);
                    dataSetDoneTasks.setColors(ColorTemplate.MATERIAL_COLORS);

                    PieData dataDoneTasks = new PieData(dataSetDoneTasks);
                    dataDoneTasks.setValueTextSize(15f);
                    dataDoneTasks.setValueTextColor(Color.WHITE);

                    pieChartDoneTasks.setData(dataDoneTasks);
                    pieChartDoneTasks.notifyDataSetChanged();
                    pieChartDoneTasks.invalidate();
                }
            }

            @Override
            public void onCategoryUpdateCallback() {

            }
        }).execute();

        final BarChart barChartWeek = (BarChart) findViewById(R.id.bar_chart_week);
        barChartWeek.getDescription().setEnabled(false);
        barChartWeek.setFitBars(true);
        barChartWeek.getLegend().setEnabled(false);
        barChartWeek.getXAxis().setDrawLabels(false);

        final BarEntry[] yValuesWeekArray = new BarEntry[7];

        final boolean[] greenlightweek = {false, false, false, false, false, false, false};

        Calendar calendar;
        calendar = Calendar.getInstance();

        for (int i = 6; i >= 0; i--) {
            final int finalI = i;
            new DbAsyncTask(AppDatabase.getInstance(), dbAction.TASK_GETALLBYDATESTATUS, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), "Completed", new DbAsyncTask.DbAsyncTaskListener() {
                @Override
                public void onTaskGetByIdCallback(Task task) {

                }

                @Override
                public void onTaskGetAllByStatusCallback(List<Task> taskList) {

                }

                @Override
                public void onTaskUpdateCallback(int taskId) {

                }

                @Override
                public void onTaskGetAllByFiltersCallback(List<Task> taskList) {
                    yValuesWeekArray[finalI] = new BarEntry(finalI, taskList.size());

                    greenlightweek[finalI] = true;
                    if (greenlightweek[0] && greenlightweek[1] && greenlightweek[2] && greenlightweek[3] && greenlightweek[4] && greenlightweek[5] && greenlightweek[6]) {
                        BarDataSet dataSetWeek = new BarDataSet(Arrays.asList(yValuesWeekArray), "");
                        dataSetWeek.setColors(ColorTemplate.MATERIAL_COLORS);
                        dataSetWeek.setDrawValues(false);

                        BarData dataWeek = new BarData(dataSetWeek);

                        barChartWeek.setData(dataWeek);
                        barChartWeek.notifyDataSetChanged();
                        barChartWeek.invalidate();
                    }
                }

                @Override
                public void onCategoryGetAllCallback(List<TaskCategory> categoryList) {

                }

                @Override
                public void onCategoryUpdateCallback() {

                }
            }).execute();

            calendar.add(Calendar.DAY_OF_MONTH, -1);
        }

        final LineChart lineChartYear = (LineChart) findViewById(R.id.line_chart_year);
        lineChartYear.getDescription().setEnabled(false);
        lineChartYear.getLegend().setEnabled(false);
        lineChartYear.getXAxis().setDrawLabels(false);
        lineChartYear.setDragEnabled(false);
        lineChartYear.setScaleEnabled(false);

        final Entry[] yValuesYearArray = new Entry[12];

        final boolean[] greenLightYear = {false, false, false, false, false, false, false, false, false, false, false, false};

        calendar = Calendar.getInstance();

        for (int i = 11; i >= 0; i--) {
            final int finalI = i;
            new DbAsyncTask(AppDatabase.getInstance(), dbAction.TASK_GETALLBYMONTHSTATUS, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), "Completed", new DbAsyncTask.DbAsyncTaskListener() {
                @Override
                public void onTaskGetByIdCallback(Task task) {

                }

                @Override
                public void onTaskGetAllByStatusCallback(List<Task> taskList) {

                }

                @Override
                public void onTaskUpdateCallback(int taskId) {

                }

                @Override
                public void onTaskGetAllByFiltersCallback(List<Task> taskList) {
                    yValuesYearArray[finalI] = new Entry(finalI, taskList.size());

                    greenLightYear[finalI] = true;
                    if (greenLightYear[0] && greenLightYear[1] && greenLightYear[2] && greenLightYear[3] && greenLightYear[4] && greenLightYear[5] && greenLightYear[6] && greenLightYear[7] && greenLightYear[8] && greenLightYear[9] && greenLightYear[10] && greenLightYear[11]) {
                        LineDataSet dataSetYear = new LineDataSet(Arrays.asList(yValuesYearArray), "");
                        dataSetYear.setDrawValues(false);

                        List<ILineDataSet> lineDataSetYear = new ArrayList<ILineDataSet>();
                        lineDataSetYear.add(dataSetYear);

                        LineData dataYear = new LineData(lineDataSetYear);

                        lineChartYear.setData(dataYear);
                        lineChartYear.notifyDataSetChanged();
                        lineChartYear.invalidate();
                    }
                }

                @Override
                public void onCategoryGetAllCallback(List<TaskCategory> categoryList) {

                }

                @Override
                public void onCategoryUpdateCallback() {

                }
            }).execute();

            calendar.add(Calendar.MONTH, -1);
        }
    }
}
