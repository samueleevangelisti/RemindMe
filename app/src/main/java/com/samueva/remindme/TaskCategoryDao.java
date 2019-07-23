package com.samueva.remindme;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface TaskCategoryDao {

    //REWORK
    @Query("SELECT * FROM taskcategory")
    List<TaskCategory> getAll();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(TaskCategory... taskCategories);

    @Query("DELETE FROM taskcategory WHERE name = :categoryName")
    void deleteByName(String categoryName);

    @Query("UPDATE taskcategory SET tasks = tasks + 1 WHERE name = :categoryName")
    void updateNTaskAdd(String categoryName);
}
