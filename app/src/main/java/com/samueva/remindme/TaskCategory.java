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


    public TaskCategory(String name, boolean isDefault, int tasks) {
        this.name = name;
        this.isDefault = isDefault;
        this.tasks = tasks;
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
}
