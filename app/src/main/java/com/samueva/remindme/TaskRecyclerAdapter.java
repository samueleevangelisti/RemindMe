package com.samueva.remindme;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

// TODO: 5/9/19 implementare le funzione per la gestione rapida dei task 

public class TaskRecyclerAdapter extends RecyclerView.Adapter<TaskRecyclerAdapter.ViewHolder> {

    // TODO: 5/9/19 STRINGA DI DEBUG
    private static final String TAG = "ReMe_TaskRecyclerAdapter";

    private List<Task> tasks;

    TaskCardClickListener taskCardClickListener;

    public interface TaskCardClickListener {
        void onTaskCardClick(int taskId);
        void onTaskCardLater(int taskId);
        void onTaskCardDone(int taskId);
        void onTaskCardDelete(int taskId);
    }

    public TaskRecyclerAdapter(List<Task> tasks, TaskCardClickListener taskCardClickListener) {
        this.tasks = tasks;
        this.taskCardClickListener = taskCardClickListener;
    }

    public void refreshData(List<Task> tasks) {
        this.tasks = tasks;
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
        viewHolder.itemDate.setText(String.format("%02d/%02d/%04d", this.tasks.get(i).getDayOfMonth(), this.tasks.get(i).getMonth() + 1, this.tasks.get(i).getYear()));
        viewHolder.itemTime.setText(String.format("%02d:%02d", this.tasks.get(i).getHourOfDay(), this.tasks.get(i).getMinute()));
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
            this.taskCard = (CardView) itemView.findViewById(R.id.history_card);
            this.itemDate = (TextView) itemView.findViewById(R.id.history_card_date);
            this.itemTime = (TextView) itemView.findViewById(R.id.history_card_time);
            this.itemTitle = (TextView) itemView.findViewById(R.id.history_card_title);
            this.itemPlace = (TextView) itemView.findViewById(R.id.history_card_place);
            this.itemStatus = (TextView) itemView.findViewById(R.id.history_card_status);
            this.buttonLater = (Button) itemView.findViewById(R.id.task_card_later);
            this.buttonDone = (Button) itemView.findViewById(R.id.task_card_done);
            this.buttonDelete = (Button) itemView.findViewById(R.id.history_card_delete);
        }
    }
}
