package com.samueva.remindme;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface TaskCategoryDao {

    @Query("SELECT * FROM taskcategory WHERE name = :categoryName")
    TaskCategory getByName(String categoryName);

    @Query("SELECT * FROM taskcategory")
    List<TaskCategory> getAll();

    @Query("SELECT * FROM taskcategory WHERE historyTasks > 0")
    List<TaskCategory> getAllInHistory();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(TaskCategory... taskCategories);

    @Delete
    void delete(TaskCategory category);

    @Update
    void update(TaskCategory... taskCategories);
}
