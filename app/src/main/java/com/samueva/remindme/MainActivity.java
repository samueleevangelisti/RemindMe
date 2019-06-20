package com.samueva.remindme;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;
//import android.view.Menu;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // Request codes
    private static final int addTaskActivity_requestCode = 101;

    // AppDatabase
    private AppDatabase db;

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
        this.db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "database").build();

        // Categories
        // TODO: 6/19/19 L'iniziallizzazione delle categories di default va fatta prendendole da un file risorsa
        List<String> categories = new ArrayList<String>();
        categories.add("Family");
        categories.add("Work");
        categories.add("Sport");
        new DbAsyncTask(this.db, dbAction.CONFIG_CATEGORY, categories);

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
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if(dy > 0) {
                    fab.hide();
                } else {
                    fab.show();
                }
            }
        });
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapter = new TaskRecyclerAdapter(new ArrayList<Task>(), new TaskRecyclerAdapter.TaskCardClickListener() {
            @Override
            public void onTaskCardClick(int taskId) {
                new DbAsyncTask(db, recyclerAdapter, dbAction.INFO_TASK, taskId, new DbAsyncTask.DbAsyncTaskCallback() {
                    @Override
                    public void onInfoReadyCallback(Task task) {
                        startTaskInfoActivity(task);
                    }
                }).execute();
            }

            @Override
            public void onTaskCardLater(int taskId) {
                // TODO: 6/13/19 Implementare il rimando del task
            }

            @Override
            public void onTaskCardDone(int taskId) {
                // TODO: 6/13/19 implementare il completamento del task
            }

            @Override
            public void onTaskCardDelete(int taskId) {
                new DbAsyncTask(db, recyclerAdapter, dbAction.DELETE_TASK, taskId).execute();
            }
        });
        recyclerView.setAdapter(recyclerAdapter);

        new DbAsyncTask(this.db, this.recyclerAdapter, dbAction.INIT_TASK).execute();
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

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }*/

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    // TODO: 6/20/19 modificare correttamente il navigation drawer
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

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
                    Task newTask = (Task) data.getExtras().getParcelable("task");
                    new DbAsyncTask(this.db, this.recyclerAdapter, dbAction.INSERT_TASK, newTask).execute();
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

    private void startTaskInfoActivity(Task task) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("task", task);
        Intent intent = new Intent(this, TaskInfoActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
