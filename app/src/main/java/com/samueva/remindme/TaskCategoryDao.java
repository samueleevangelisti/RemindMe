package com.samueva.remindme;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface TaskCategoryDao {

    @Query("SELECT * FROM taskcategory")
    List<TaskCategory> getAll();

    @Insert
    void insertAll(TaskCategory... taskCategories);

    @Delete
    void delete(TaskCategory taskCategory);
}
