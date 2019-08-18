package com.samueva.remindme;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface TaskDao {

    @Query("SELECT * FROM task WHERE id = :taskId")
    Task getById(int taskId);

    @Query("SELECT * FROM task WHERE status = :taskStatus")
    List<Task> getAllByStatus(String taskStatus);

    @Insert
    long insert(Task tasks);

    @Delete
    void delete(Task... tasks);

    @Query("DELETE FROM task WHERE category = :taskCategory")
    void deleteByCategory(String taskCategory);

    @Query("DELETE FROM task WHERE status = :taskStatus")
    void deleteByStatus(String taskStatus);

    @Update
    void update(Task... tasks);
}
