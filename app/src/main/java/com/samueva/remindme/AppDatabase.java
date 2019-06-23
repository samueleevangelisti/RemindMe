package com.samueva.remindme;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

@Database(entities = {Task.class, TaskCategory.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract TaskDao taskDao();
    public abstract TaskCategoryDao taskCategoryDao();

    private static AppDatabase INSTANCE;

    public static AppDatabase buildInstance(@NonNull Context context, @NonNull Class<AppDatabase> klass, @NonNull String name) {
        INSTANCE = Room.databaseBuilder(context, klass, name).build();
        return INSTANCE;
    }

    public static AppDatabase getInstance() {
        return INSTANCE;
    }
}
