package com.samueva.remindme;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity implements AddCategoryDialogFragment.AddCategoryDialogListener, GestureDetector.OnGestureListener {

    // AppDatabase
    private AppDatabase db;
    private final DbAsyncTask.DbAsyncTaskListener dbAsyncTaskListener = new DbAsyncTask.DbAsyncTaskListener() {
        @Override
        public void onTaskGetAllByStatusCallback(List<Task> taskList) {

        }

        @Override
        public void onTaskUpdateCallback() {

        }

        @Override
        public void onInfoTaskCallback(Task task) {

        }

        @Override
        public void onGetAllCategoryCallback(List<TaskCategory> categoryList) {
            recyclerAdapter.refreshData(categoryList);
            recyclerAdapter.notifyDataSetChanged();
        }

        @Override
        public void onInsertCategoryCallback() {
            new DbAsyncTask(db, dbAction.GETALL_CATEGORY, dbAsyncTaskListener).execute();
        }

        @Override
        public void onDeleteCategoryCallback() {
            new DbAsyncTask(db, dbAction.GETALL_CATEGORY, dbAsyncTaskListener).execute();
        }
    };

    // RecyclerView
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    CategoryRecyclerAdapter recyclerAdapter;

    // FloatingActionButton
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        // Database
        this.db = AppDatabase.buildInstance(getApplicationContext(), AppDatabase.class, "database");

        // FloatingActionButton
        fab = findViewById(R.id.fab3);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment addCategoryDialogFragment = new AddCategoryDialogFragment();
                addCategoryDialogFragment.show(getSupportFragmentManager(), "acdfmanagerfromca");
            }
        });

        //RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.category_recycler_view);
        // TODO: 7/5/19 gestione dello scrolling per far sparire il FloatingActionButton
        this.layoutManager = new LinearLayoutManager(this);
        this.recyclerView.setLayoutManager(layoutManager);
        this.recyclerAdapter = new CategoryRecyclerAdapter(new ArrayList<TaskCategory>(), new CategoryRecyclerAdapter.CategoryCardClickListener() {
            @Override
            public void onCategoryCardDelete(String categoryName) {
                new DbAsyncTask(db, dbAction.DELETE_CATEGORY, dbAsyncTaskListener, categoryName).execute();
            }
        });
        this.recyclerView.setAdapter(recyclerAdapter);

        new DbAsyncTask(this.db, dbAction.GETALL_CATEGORY, this.dbAsyncTaskListener).execute();
    }

    @Override
    public void onDialogPositiveClick(String category) {
        TaskCategory newCategory = new TaskCategory(category, false);
        new DbAsyncTask(this.db, dbAction.INSERT_CATEGORY, this.dbAsyncTaskListener, newCategory).execute();
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }
}
