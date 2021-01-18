package com.android.telm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * Created by ankit on 27/10/17.
 */

public class StudyRecyclerAdapterFromDocPointOfView extends RecyclerView.Adapter<StudyRecyclerAdapterFromDocPointOfView.ViewHolder> {

    private Context context;
    private List<Study> list;

    public StudyRecyclerAdapterFromDocPointOfView(Context context, List<Study> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.study_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Study study = list.get(position);

        holder.textObservations.setText(study.getObservations());
        holder.textDoctorName.setText(study.getDoctorName());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textDoctorName, textObservations;

        public ViewHolder(View itemView) {
            super(itemView);

            textDoctorName = itemView.findViewById(R.id.doctors_surname_txt);
            textObservations = itemView.findViewById(R.id.observations_txt);
        }
    }

}
