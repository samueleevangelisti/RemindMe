package com.samueva.remindme;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class TaskCategory {

    @PrimaryKey
    @NonNull private String name;
    @NonNull private boolean isDefault;

    public TaskCategory(String name, boolean isDefault) {
        this.name = name;
        this.isDefault = isDefault;
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
}
