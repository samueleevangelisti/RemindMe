package com.samueva.remindme;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.widget.EditText;

public class FiltersDialogFragment extends DialogFragment {

    FiltersDialogListener filtersDialogListener;

    public interface FiltersDialogListener {
        void onFiltersDialogPositiveClick(String taskTitle, String taskPlace);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        builder.setTitle("Filters")
                .setView(inflater.inflate(R.layout.filters_dialog_layout, null))
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText taskTitle = (EditText) getDialog().findViewById(R.id.filters_task_title);
                        EditText taskPlace = (EditText) getDialog().findViewById(R.id.filters_task_place);
                        filtersDialogListener.onFiltersDialogPositiveClick(taskTitle.getText().toString(), taskPlace.getText().toString());
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            filtersDialogListener = (FiltersDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + " must implement FiltersDialogListener");
        }
    }
}
