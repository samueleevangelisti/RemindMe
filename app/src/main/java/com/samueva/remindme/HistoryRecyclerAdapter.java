package com.samueva.remindme;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

// TODO: 5/9/19 implementare le funzione per la gestione rapida dei task 

public class HistoryRecyclerAdapter extends RecyclerView.Adapter<HistoryRecyclerAdapter.ViewHolder> {

    // TODO: 5/9/19 STRINGA DI DEBUG
    private static final String TAG = "ReMe_HistoryRecyclerAdapter";

    private List<Task> tasks;

    private HistoryCardClickListener historyCardClickListener;

    public interface HistoryCardClickListener {
        void onTaskCardClick(Task task);
        void onHistoryCardRestore(int taskId);
        void onHistoryCardDelete(int taskId, String taskCategory);
    }

    public HistoryRecyclerAdapter(List<Task> tasks, HistoryCardClickListener historyCardClickListener) {
        this.tasks = tasks;
        this.historyCardClickListener = historyCardClickListener;
    }

    public void refreshData(List<Task> tasks) {
        this.tasks = tasks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.history_card_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        viewHolder.taskCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                historyCardClickListener.onTaskCardClick(tasks.get(viewHolder.getAdapterPosition()));
            }
        });
        viewHolder.buttonRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                historyCardClickListener.onHistoryCardRestore(tasks.get(viewHolder.getAdapterPosition()).getId());
            }
        });
        viewHolder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                historyCardClickListener.onHistoryCardDelete(tasks.get(viewHolder.getAdapterPosition()).getId(), tasks.get(viewHolder.getAdapterPosition()).getCategory());
            }
        });
        viewHolder.itemDate.setText(String.format("%02d/%02d/%04d", this.tasks.get(i).getDayOfMonth(), this.tasks.get(i).getMonth() + 1, this.tasks.get(i).getYear()));
        viewHolder.itemTime.setText(String.format("%02d:%02d", this.tasks.get(i).getHourOfDay(), this.tasks.get(i).getMinute()));
        viewHolder.itemDoneDate.setText(String.format("%02d/%02d/%04d", this.tasks.get(i).getDoneDayOfMonth(), this.tasks.get(i).getDoneMonth() + 1, this.tasks.get(i).getDoneYear()));
        viewHolder.itemDoneTime.setText(String.format("%02d:%02d", this.tasks.get(i).getDoneHourOfDay(), this.tasks.get(i).getDoneMinute()));
        viewHolder.itemTitle.setText(this.tasks.get(i).getTitle());
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
        public TextView itemDoneDate;
        public TextView itemDoneTime;
        public TextView itemTitle;
        public TextView itemStatus;
        public ImageButton buttonRestore;
        public ImageButton buttonDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.taskCard = (CardView) itemView.findViewById(R.id.history_card);
            this.itemDate = (TextView) itemView.findViewById(R.id.history_card_date);
            this.itemTime = (TextView) itemView.findViewById(R.id.history_card_time);
            this.itemDoneDate = (TextView) itemView.findViewById(R.id.history_card_done_date);
            this.itemDoneTime = (TextView) itemView.findViewById(R.id.history_card_done_time);
            this.itemTitle = (TextView) itemView.findViewById(R.id.history_card_title);
            this.itemStatus = (TextView) itemView.findViewById(R.id.history_card_status);
            this.buttonRestore = (ImageButton) itemView.findViewById(R.id.history_card_restore);
            this.buttonDelete = (ImageButton) itemView.findViewById(R.id.history_card_delete);
        }
    }
}
