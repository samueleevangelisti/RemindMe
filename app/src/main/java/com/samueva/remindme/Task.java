package com.samueva.remindme;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;

/* status:
        Pending,
        Ongoing,
        Completed,
        Failed
 */

@Entity
public class Task implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "year")
    private int year;
    @ColumnInfo(name = "month")
    private int month;
    @ColumnInfo(name = "day_of_month")
    private int dayOfMonth;
    @ColumnInfo(name = "hour_of_day")
    private int hourOfDay;
    @ColumnInfo(name = "minute")
    private int minute;
    @ColumnInfo(name = "notification_year")
    private int notificationYear;
    @ColumnInfo(name = "notification_month")
    private int notificationMonth;
    @ColumnInfo(name = "notification_day_of_month")
    private int notificationDayOfMonth;
    @ColumnInfo(name = "notification_hour_of_day")
    private int notificationHourOfDay;
    @ColumnInfo(name = "notification_minute")
    private int notificationMinute;
    @ColumnInfo(name = "done_year")
    private int doneYear;
    @ColumnInfo(name = "done_month")
    private int doneMonth;
    @ColumnInfo(name = "done_day_of_month")
    private int doneDayOfMonth;
    @ColumnInfo(name = "done_hour_of_day")
    private int doneHourOfDay;
    @ColumnInfo(name = "done_minute")
    private int doneMinute;
    @ColumnInfo(name = "place")
    private String place;
    @ColumnInfo(name = "description")
    private String description;
    @ColumnInfo(name = "category")
    private String category;
    @ColumnInfo(name = "priority")
    private int priority;
    @ColumnInfo(name = "status")
    private String status;

    public Task(String title, int year, int month, int dayOfMonth, int hourOfDay, int minute, String place, String description, String category, int priority, String status) {
        this.title = title;
        this.year = year;
        this.month = month;
        this.dayOfMonth = dayOfMonth;
        this.hourOfDay = hourOfDay;
        this.minute = minute;
        this.place = place;
        this.description = description;
        this.category = category;
        this.priority = priority;
        this.status = status;
    }

    public Task(String title, Calendar calendar, String place, String description, String category, int priority, String status) {
        this.title = title;
        this.year = calendar.get(Calendar.YEAR);
        this.month = calendar.get(Calendar.MONTH);
        this.dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        this.hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        this.minute = calendar.get(Calendar.MINUTE);
        this.place = place;
        this.description = description;
        this.category = category;
        this.priority = priority;
        this.status = status;
    }

    public Task(Parcel parcel) {
        this.id = parcel.readInt();
        this.title = parcel.readString();
        this.year = parcel.readInt();
        this.month = parcel.readInt();
        this.dayOfMonth = parcel.readInt();
        this.hourOfDay = parcel.readInt();
        this.minute = parcel.readInt();
        this.notificationYear = parcel.readInt();
        this.notificationMonth = parcel.readInt();
        this.notificationDayOfMonth = parcel.readInt();
        this.notificationHourOfDay = parcel.readInt();
        this.notificationMinute = parcel.readInt();
        this.doneYear = parcel.readInt();
        this.doneMonth = parcel.readInt();
        this.doneDayOfMonth = parcel.readInt();
        this.doneHourOfDay = parcel.readInt();
        this.doneMinute = parcel.readInt();
        this.place = parcel.readString();
        this.description = parcel.readString();
        this.category = parcel.readString();
        this.priority = parcel.readInt();
        this.status = parcel.readString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCalendar(Calendar calendar) {
        this.year = calendar.get(Calendar.YEAR);
        this.month = calendar.get(Calendar.MONTH);
        this.dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        this.hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        this.minute = calendar.get(Calendar.MINUTE);
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public int getHourOfDay() {
        return hourOfDay;
    }

    public void setHourOfDay(int hourOfDay) {
        this.hourOfDay = hourOfDay;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public void setNotificationCalendar(Calendar calendar) {
        this.notificationYear = calendar.get(Calendar.YEAR);
        this.notificationMonth = calendar.get(Calendar.MONTH);
        this.notificationDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        this.notificationHourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        this.notificationMinute = calendar.get(Calendar.MINUTE);
    }

    public int getNotificationYear() {
        return notificationYear;
    }

    public void setNotificationYear(int notificationYear) {
        this.notificationYear = notificationYear;
    }

    public int getNotificationMonth() {
        return notificationMonth;
    }

    public void setNotificationMonth(int notificationMonth) {
        this.notificationMonth = notificationMonth;
    }

    public int getNotificationDayOfMonth() {
        return notificationDayOfMonth;
    }

    public void setNotificationDayOfMonth(int notificationDayOfMonth) {
        this.notificationDayOfMonth = notificationDayOfMonth;
    }

    public int getNotificationHourOfDay() {
        return notificationHourOfDay;
    }

    public void setNotificationHourOfDay(int notificationHourOfDay) {
        this.notificationHourOfDay = notificationHourOfDay;
    }

    public int getNotificationMinute() {
        return notificationMinute;
    }

    public void setNotificationMinute(int notificationMinute) {
        this.notificationMinute = notificationMinute;
    }

    public void setDoneCalendar(Calendar doneCalendar) {
        this.doneYear = doneCalendar.get(Calendar.YEAR);
        this.doneMonth = doneCalendar.get(Calendar.MONTH);
        this.doneDayOfMonth = doneCalendar.get(Calendar.DAY_OF_MONTH);
        this.doneHourOfDay = doneCalendar.get(Calendar.HOUR_OF_DAY);
        this.doneMinute = doneCalendar.get(Calendar.MINUTE);
    }

    public int getDoneYear() {
        return doneYear;
    }

    public void setDoneYear(int doneYear) {
        this.doneYear = doneYear;
    }

    public int getDoneMonth() {
        return doneMonth;
    }

    public void setDoneMonth(int doneMonth) {
        this.doneMonth = doneMonth;
    }

    public int getDoneDayOfMonth() {
        return doneDayOfMonth;
    }

    public void setDoneDayOfMonth(int doneDayOfMonth) {
        this.doneDayOfMonth = doneDayOfMonth;
    }

    public int getDoneHourOfDay() {
        return doneHourOfDay;
    }

    public void setDoneHourOfDay(int doneHourOfDay) {
        this.doneHourOfDay = doneHourOfDay;
    }

    public int getDoneMinute() {
        return doneMinute;
    }

    public void setDoneMinute(int doneMinute) {
        this.doneMinute = doneMinute;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeInt(this.year);
        dest.writeInt(this.month);
        dest.writeInt(this.dayOfMonth);
        dest.writeInt(this.hourOfDay);
        dest.writeInt(this.minute);
        dest.writeInt(this.notificationYear);
        dest.writeInt(this.notificationMonth);
        dest.writeInt(this.notificationDayOfMonth);
        dest.writeInt(this.notificationHourOfDay);
        dest.writeInt(this.notificationMinute);
        dest.writeInt(this.doneYear);
        dest.writeInt(this.doneMonth);
        dest.writeInt(this.doneDayOfMonth);
        dest.writeInt(this.doneHourOfDay);
        dest.writeInt(this.doneMinute);
        dest.writeString(this.place);
        dest.writeString(this.description);
        dest.writeString(this.category);
        dest.writeInt(this.priority);
        dest.writeString(this.status);
    }

    public final static Parcelable.Creator CREATOR = new Parcelable.Creator() {

        @Override
        public Task createFromParcel(Parcel source) {
            return new Task(source);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };
}
