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

    @Query("SELECT * FROM task WHERE title LIKE :taskTitle AND place LIKE :taskPlace")
    List<Task> getAllByFilters(String taskTitle, String taskPlace);

    @Query("SELECT * FROM task WHERE year = :taskYear AND month = :taskMonth AND day_of_month = :taskDayOfMonth")
    List<Task> getAllByDate(int taskYear, int taskMonth, int taskDayOfMonth);

    @Query("SELECT * FROM task WHERE year = :taskYear AND month = :taskMonth AND day_of_month = :taskDayOfMonth AND status = :taskStatus")
    List<Task> getAllByDateStatus(int taskYear, int taskMonth, int taskDayOfMonth, String taskStatus);

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
