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

import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

// TODO: 5/9/19 implementare le funzione per la gestione rapida dei task 

public class CategoryRecyclerAdapter extends RecyclerView.Adapter<CategoryRecyclerAdapter.ViewHolder> {

    // TODO: 5/9/19 STRINGA DI DEBUG
    private static final String TAG = "ReMe_CategoryRecyclerAdapter";

    private List<TaskCategory> categories;

    private CategoryCardClickListener categoryCardClickListener;

    public interface CategoryCardClickListener {
        void onCategoryCardDelete(String categoryName);
    }

    public CategoryRecyclerAdapter(List<TaskCategory> categories, CategoryCardClickListener categoryCardClickListener) {
        this.categories = categories;
        sortData();
        this.categoryCardClickListener = categoryCardClickListener;
    }

    public void refreshData(List<TaskCategory> categories) {
        this.categories = categories;
        sortData();
    }

    private void sortData() {
        this.categories.sort(new Comparator<TaskCategory>() {
            @Override
            public int compare(TaskCategory o1, TaskCategory o2) {
                if (o1.getIsDefault()) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.category_card_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        viewHolder.categoryCard.setTag((String) this.categories.get(i).getName());
        if (this.categories.get(i).getIsDefault()) {
            viewHolder.buttonDelete.setEnabled(false);
            viewHolder.buttonDelete.setAlpha((float) 0.5);
        } else {
            viewHolder.buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    categoryCardClickListener.onCategoryCardDelete((String) viewHolder.categoryCard.getTag());
                }
            });
        }
        viewHolder.itemName.setText(this.categories.get(i).getName());
    }

    @Override
    public int getItemCount() {
        return this.categories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public CardView categoryCard;
        public TextView itemName;
        public ImageButton buttonDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.categoryCard = (CardView) itemView.findViewById(R.id.category_card);
            this.itemName = (TextView) itemView.findViewById(R.id.category_card_name);
            this.buttonDelete = (ImageButton) itemView.findViewById(R.id.category_card_delete);
        }
    }
}
