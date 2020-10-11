package com.example.giris.drdroid.fragments.adapters;

/**
 * Created by giris on 27-03-2016.
 */
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devspark.robototextview.widget.RobotoTextView;
import com.example.giris.drdroid.R;
import com.example.giris.drdroid.fragments.PrescriptionsFragment;
import com.example.giris.drdroid.fragments.data.PrescriptionsModel;
import com.example.giris.drdroid.fragments.diagnosefragments.ShowDoctorsFragment;
import com.example.giris.drdroid.fragments.diagnosefragments.data.ShowDoctorsModel;
import com.example.giris.drdroid.libraries.LabelTextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class PrescriptionsAdapter extends RecyclerView.Adapter<PrescriptionsAdapter.MyViewHolder> {

    private ArrayList<PrescriptionsModel> dataSet;
    public PrescriptionsAdapter(ArrayList<PrescriptionsModel> data) {
        this.dataSet = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.prescriptions_card, parent, false);

        view.setOnClickListener(PrescriptionsFragment.myOnClickListener);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        LabelTextView name = holder.name;
        RobotoTextView date = holder.date;
        RobotoTextView id = holder.id;
        RobotoTextView prescriptions = holder.prescriptions;

        name.setText("Dr. "+dataSet.get(listPosition).name);
        name.setLabelEnable(false);

        date.setText("Date: "+dataSet.get(listPosition).date);
        String secondaryText = "ID: "+dataSet.get(listPosition).id+"\n"+
                "Prescription: "+dataSet.get(listPosition).prescriptions;
        prescriptions.setText(secondaryText);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        LabelTextView name;
        RobotoTextView date;
        RobotoTextView id;
        RobotoTextView prescriptions;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.name = (LabelTextView) itemView.findViewById(R.id.name);
            this.date = (RobotoTextView) itemView.findViewById(R.id.date);
            this.prescriptions = (RobotoTextView) itemView.findViewById(R.id.secondary);
        }
    }
}
