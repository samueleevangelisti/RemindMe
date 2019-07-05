package com.samueva.remindme;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {

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

        // FloatingActionButton
        fab = findViewById(R.id.fab3);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
                // TODO: 7/5/19 eliminazione della category quando richiesta
            }
        });
        this.recyclerView.setAdapter(recyclerAdapter);

        // TODO: 7/5/19 compilazione degli elementi della recycler view presi dal database
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
