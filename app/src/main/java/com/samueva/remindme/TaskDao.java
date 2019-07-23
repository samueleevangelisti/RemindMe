package com.samueva.remindme;

import android.arch.persistence.room.Dao;
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
    void insertAll(Task... tasks);

    @Query("DELETE FROM task WHERE id = :taskId")
    void deleteByTaskId(int taskId);

    @Query("DELETE FROM task WHERE status = 'Completed' OR status = 'Failed'")
    void deleteHistory();

    @Update
    void update(Task... tasks);
}
