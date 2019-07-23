package com.samueva.remindme;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import android.view.Menu;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // TODO: 6/23/19 STRINGA DI DEBUG
    private static final String TAG = "ReMe_MainActivity";

    // Request codes
    private static final int addTaskActivity_requestCode = 101;

    // AppDatabase
    private AppDatabase db;
    private final DbAsyncTask.DbAsyncTaskListener dbAsyncTaskListener = new DbAsyncTask.DbAsyncTaskListener() {
        @Override
        public void onTaskGetAllByStatusCallback(List<Task> taskList) {
            recyclerAdapter.refreshData(taskList);
            recyclerAdapter.notifyDataSetChanged();
        }

        @Override
        public void onTaskUpdateCallback() {
            new DbAsyncTask(db, dbAction.TASK_GETALLBYSTATUS, "Pending", dbAsyncTaskListener).execute();
        }

        @Override
        public void onInfoTaskCallback(Task task) {

        }

        @Override
        public void onGetAllCategoryCallback(List<TaskCategory> categoryList) {

        }

        @Override
        public void onInsertCategoryCallback() {

        }

        @Override
        public void onDeleteCategoryCallback() {

        }
    };

    //RecylerView
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    TaskRecyclerAdapter recyclerAdapter;

    // FloatingActionButton
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Database
        this.db = AppDatabase.buildInstance(getApplicationContext(), AppDatabase.class, "database");

        // Init Categories
        List<TaskCategory> categoryList = new ArrayList<TaskCategory>();
        String[] categories = getResources().getStringArray(R.array.categories);
        for (int i = 0; i < categories.length; i++) {
            categoryList.add(new TaskCategory(categories[i], true, 0));
        }
        new DbAsyncTask(this.db, dbAction.INIT_CATEGORY, categoryList).execute();

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // FloatingActionButton
        this.fab = findViewById(R.id.fab);
        this.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAddTaskActivity();
            }
        });

        // NavigationDrawer
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle( this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        // RecyclerView
        this.recyclerView = (RecyclerView) findViewById(R.id.home_recycler_view);
        this.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if(dy > 0) {
                    fab.hide();
                } else {
                    fab.show();
                }
            }
        });
        this.layoutManager = new LinearLayoutManager(this);
        this.recyclerView.setLayoutManager(layoutManager);
        this.recyclerAdapter = new TaskRecyclerAdapter(new ArrayList<Task>(), new TaskRecyclerAdapter.TaskCardClickListener() {
            @Override
            public void onTaskCardClick(int taskId) {
                startTaskInfoActivity(taskId);
            }

            @Override
            public void onTaskCardLater(int taskId) {
                // TODO: 6/13/19 Implementare il rimando del task
            }

            @Override
            public void onTaskCardDone(int taskId) {
                new DbAsyncTask(db, dbAction.TASK_UPDATE_COMPLETE, taskId, Calendar.getInstance(), dbAsyncTaskListener).execute();
            }

            @Override
            public void onTaskCardDelete(int taskId) {
                new DbAsyncTask(db, dbAction.TASK_DELETE_BYTASKID, taskId, dbAsyncTaskListener).execute();
            }
        });
        this.recyclerView.setAdapter(recyclerAdapter);

        new DbAsyncTask(this.db, dbAction.TASK_GETALLBYSTATUS, "Pending", this.dbAsyncTaskListener).execute();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.main_optionmenu_search) {
            Toast.makeText(this, "Filters compare", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // TODO: 6/20/19 modificare correttamente il navigation drawer
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_add) {
            startAddTaskActivity();
        } else if (id == R.id.nav_home) {

        } else if (id == R.id.nav_history) {
            startHistoryActivity();
        } else if (id == R.id.nav_trend) {
            startTrendActivity();
        } else if (id == R.id.nav_categories) {
            startCategoryActivity();
        } else if (id == R.id.nav_settings) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case addTaskActivity_requestCode:
                    Log.d(TAG, "onActivityResult: " + data.getBooleanExtra("new", false));
                    if (data.getBooleanExtra("new", false)) {
                        new DbAsyncTask(this.db, dbAction.TASK_GETALLBYSTATUS, "Pending", dbAsyncTaskListener).execute();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void startAddTaskActivity() {
        Intent intent = new Intent(this, AddTaskActivity.class);
        startActivityForResult(intent, addTaskActivity_requestCode);
    }

    private void startTaskInfoActivity(int taskId) {
        Intent intent = new Intent(this, TaskInfoActivity.class);
        intent.putExtra("taskId", (int) taskId);
        startActivity(intent);
    }

    private void startTrendActivity() {
        Intent intent = new Intent(this, TrendActivity.class);
        startActivity(intent);
    }

    private void startHistoryActivity() {
        Intent intent = new Intent(this, HistoryActivity.class);
        startActivity(intent);
    }

    private void startCategoryActivity() {
        Intent intent = new Intent(this, CategoryActivity.class);
        startActivity(intent);
    }
}
