package com.samueva.remindme;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface TaskDao {

    @Query("SELECT * FROM task")
    List<Task> getAll();

    @Query("SELECT * FROM task WHERE id IN (:taskIds)")
    List<Task> getAllByIds(int[] taskIds);

    @Query("SELECT * FROM task WHERE id = :taskId")
    Task getById(int taskId);

    @Query("SELECT * FROM task WHERE year = :taskYear AND month = :taskMonth AND dayOfMonth = :taskDayOfMonth")
    List<Task> getAllByDate(int taskYear, int taskMonth, int taskDayOfMonth);

    @Query("SELECT * FROM task WHERE year = :taskYear AND month = :taskMonth AND dayOfMonth = :taskDayOfMonth AND status = :taskStatus")
    List<Task> getAllByDateStatus(int taskYear, int taskMonth, int taskDayOfMonth, int taskStatus);

    @Query("SELECT * FROM task WHERE category = :taskCategory")
    List<Task> getAllByCategory(String taskCategory);

    @Query("SELECT * FROM task WHERE status = :taskStatus")
    List<Task> getAllByStatus(String taskStatus);

    @Query("UPDATE task SET status = :taskStatus WHERE id = :taskId")
    void updateTaskStatus(int taskId, String taskStatus);

    @Query("UPDATE task SET doneYear = :taskYear, doneMonth = :taskMonth, doneDayOfMonth = :taskDayOfMonth, doneHourOfDay = :taskHourOfDay, doneMinute = :taskMinute WHERE id = :taskId")
    void updateTaskDoneCalendar(int taskId, int taskYear, int taskMonth, int taskDayOfMonth, int taskHourOfDay, int taskMinute);

    @Update
    void updateTask(Task task);

    @Insert
    void insertAll(Task... tasks);

    @Delete
    void delete(Task task);
}
