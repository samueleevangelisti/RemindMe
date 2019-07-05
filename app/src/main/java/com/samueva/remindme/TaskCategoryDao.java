package com.samueva.remindme;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface TaskCategoryDao {

    @Query("SELECT * FROM taskcategory")
    List<TaskCategory> getAll();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(TaskCategory... taskCategories);

    @Delete
    void delete(TaskCategory taskCategory);

    @Query("DELETE FROM taskcategory WHERE name = :categoryName")
    void deleteByName(String categoryName);
}
