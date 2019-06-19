package com.samueva.remindme;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface TaskDao {

    @Query("SELECT * FROM task")
    List<Task> getAll();

    @Query("SELECT * FROM task WHERE id IN (:taskIds)")
    List<Task> getAllByIds(int[] taskIds);

    @Query("SELECT * FROM task WHERE id = :taskId")
    Task getById(int taskId);

    @Query("SELECT * FROM task WHERE category = :category")
    List<Task> getAllByCategory(String category);

    @Insert
    void insertAll(Task... tasks);

    @Delete
    void delete(Task task);
}
