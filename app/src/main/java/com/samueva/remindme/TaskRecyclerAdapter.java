package com.samueva.remindme;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

// TODO: 5/9/19 implementare le funzione per la gestione rapida dei task 

public class TaskRecyclerAdapter extends RecyclerView.Adapter<TaskRecyclerAdapter.ViewHolder> {

    // TODO: 5/9/19 STRINGA DI DEBUG
    private static final String TAG = "ReMe_TaskRecyclerAdapter";

    private List<Task> tasks;
    private static Calendar calendar;

    TaskCardClickListener taskCardClickListener;

    public interface TaskCardClickListener {
        void onTaskCardClick(int taskId);
        void onTaskCardLater(int taskId);
        void onTaskCardDone(int taskId);
        void onTaskCardDelete(int taskId);
    }

    public TaskRecyclerAdapter(List<Task> tasks, TaskCardClickListener taskCardClickListener) {
        this.tasks = tasks;
        sortData();
        this.taskCardClickListener = taskCardClickListener;
        this.calendar = Calendar.getInstance();
    }

    public void refreshData(List<Task> tasks) {
        this.tasks = tasks;
        sortData();
    }

    private void sortData() {
        this.tasks.sort(new Comparator<Task>() {
            @Override
            public int compare(Task o1, Task o2) {
                calendar.set(o1.getYear(), o1.getMonth(), o1.getDayOfMonth(), o1.getHourOfDay(), o1.getMinute());
                String s1 = String.format("%1$tY%1$tm%1$td%1$tH%1$tM", calendar);
                calendar.set(o2.getYear(), o2.getMonth(), o2.getDayOfMonth(), o2.getHourOfDay(), o2.getMinute());
                String s2 = String.format("%1$tY%1$tm%1$td%1$tH%1$tM", calendar);
                return s1.compareTo(s2);
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.task_card_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        viewHolder.taskCard.setTag((int) this.tasks.get(i).getId());
        viewHolder.taskCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskCardClickListener.onTaskCardClick((int) viewHolder.taskCard.getTag());
            }
        });
        viewHolder.buttonLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskCardClickListener.onTaskCardLater((int) viewHolder.taskCard.getTag());
            }
        });
        viewHolder.buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskCardClickListener.onTaskCardDone((int) viewHolder.taskCard.getTag());
            }
        });
        viewHolder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskCardClickListener.onTaskCardDelete((int) viewHolder.taskCard.getTag());
            }
        });
        this.calendar.set(this.tasks.get(i).getYear(), this.tasks.get(i).getMonth(), this.tasks.get(i).getDayOfMonth(), this.tasks.get(i).getHourOfDay(), this.tasks.get(i).getMinute());
        viewHolder.itemDate.setText(String.format("%1$td/%1$tm/%1$tY", this.calendar));
        viewHolder.itemTime.setText(String.format("%1$tH:%1$tM", this.calendar));
        viewHolder.itemTitle.setText(this.tasks.get(i).getTitle());
        viewHolder.itemPlace.setText(this.tasks.get(i).getPlace());
        viewHolder.itemStatus.setText(this.tasks.get(i).getStatus());
    }

    @Override
    public int getItemCount() {
        return this.tasks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public CardView taskCard;
        public TextView itemDate;
        public TextView itemTime;
        public TextView itemTitle;
        public TextView itemPlace;
        public TextView itemStatus;
        public Button buttonLater;
        public Button buttonDone;
        public Button buttonDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.taskCard = (CardView) itemView.findViewById(R.id.task_card);
            this.itemDate = (TextView) itemView.findViewById(R.id.task_card_date);
            this.itemTime = (TextView) itemView.findViewById(R.id.task_card_time);
            this.itemTitle = (TextView) itemView.findViewById(R.id.task_card_title);
            this.itemPlace = (TextView) itemView.findViewById(R.id.task_card_place);
            this.itemStatus = (TextView) itemView.findViewById(R.id.task_card_status);
            this.buttonLater = (Button) itemView.findViewById(R.id.task_card_later);
            this.buttonDone = (Button) itemView.findViewById(R.id.task_card_done);
            this.buttonDelete = (Button) itemView.findViewById(R.id.task_card_delete);
        }
    }
}
