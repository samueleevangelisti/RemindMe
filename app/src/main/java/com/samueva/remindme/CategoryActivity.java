package com.samueva.remindme;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity implements AddCategoryDialogFragment.AddCategoryDialogListener, DeleteCategoryDialogFragment.DeleteCategoryDialogListener {

    // Need for update
    private boolean update;

    // TaskCategory
    TaskCategory taskCategory;

    // AppDatabase
    private AppDatabase db;
    private final DbAsyncTask.DbAsyncTaskListener dbAsyncTaskListener = new DbAsyncTask.DbAsyncTaskListener() {
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
        public void onCategoryGetAllCallback(List<TaskCategory> categoryList) {
            recyclerAdapter.refreshData(categoryList);
            recyclerAdapter.notifyDataSetChanged();
        }

        @Override
        public void onCategoryUpdateCallback() {
            new DbAsyncTask(db, dbAction.CATEGORY_GETALL, dbAsyncTaskListener).execute();
        }
    };

    // RecyclerView
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    CategoryRecyclerAdapter recyclerAdapter;

    // FloatingActionButton
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        // Need for update
        this.update = false;

        // Database
        this.db = AppDatabase.getInstance();

        // FloatingActionButton
        this.fab = findViewById(R.id.fab3);
        this.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment addCategoryDialogFragment = new AddCategoryDialogFragment();
                addCategoryDialogFragment.show(getSupportFragmentManager(), "acdfmanagerfromca");
            }
        });

        //RecyclerView
        this.recyclerView = (RecyclerView) findViewById(R.id.category_recycler_view);
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
        this.recyclerAdapter = new CategoryRecyclerAdapter(new ArrayList<TaskCategory>(), new CategoryRecyclerAdapter.CategoryCardClickListener() {
            @Override
            public void onCategoryCardDelete(TaskCategory category) {
                if (category.getTasks() > 0) {
                    taskCategory = category;
                    DialogFragment deleteCategoryDialogFragment = new DeleteCategoryDialogFragment();
                    deleteCategoryDialogFragment.show(getSupportFragmentManager(), "dcdfmanagerfromca");
                } else {
                    new DbAsyncTask(db, dbAction.CATEGORY_DELETE, category, dbAsyncTaskListener).execute();
                }
            }
        });
        this.recyclerView.setAdapter(recyclerAdapter);

        new DbAsyncTask(this.db, dbAction.CATEGORY_GETALL, this.dbAsyncTaskListener).execute();
    }

    @Override
    public void onAddCategoryDialogPositiveClick(String category) {
        List<TaskCategory> categoryList = new ArrayList<TaskCategory>();
        categoryList.add(new TaskCategory(category, false, 0, 0));
        new DbAsyncTask(this.db, dbAction.CATEGORY_INSERTALL, categoryList, dbAsyncTaskListener).execute();
    }

    @Override
    public void onDeleteCategoryDialogPositiveClick() {
        this.update = true;
        new DbAsyncTask(this.db, dbAction.CATEGORY_DELETE, this.taskCategory, this.dbAsyncTaskListener).execute();
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        intent.putExtra("update", this.update);
        setResult(RESULT_OK, intent);
        super.finish();
    }
}
