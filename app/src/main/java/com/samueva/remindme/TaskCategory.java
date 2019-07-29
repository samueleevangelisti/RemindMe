package com.samueva.remindme;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class TaskCategory {

    @PrimaryKey
    @NonNull private String name;
    private boolean isDefault;
    private int tasks;
    private int historyTasks;


    public TaskCategory(String name, boolean isDefault, int tasks, int historyTasks) {
        this.name = name;
        this.isDefault = isDefault;
        this.tasks = tasks;
        this.historyTasks = historyTasks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getIsDefault() {
        return isDefault;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public int getTasks() {
        return tasks;
    }

    public void setTasks(int tasks) {
        this.tasks = tasks;
    }

    public int getHistoryTasks() {
        return historyTasks;
    }

    public void setHistoryTasks(int historyTasks) {
        this.historyTasks = historyTasks;
    }
}
