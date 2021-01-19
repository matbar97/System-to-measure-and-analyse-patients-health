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
    private List<Study> studyList;

    //declare interface
    private StudyRecyclerAdapterFromDocPointOfView.OnStudyListener mOnStudyListener;



    public StudyRecyclerAdapterFromDocPointOfView(Context context, List<Study> studyList, StudyRecyclerAdapterFromDocPointOfView.OnStudyListener mOnStudyListener) {
        this.context = context;
        this.studyList = studyList;
        this.mOnStudyListener = mOnStudyListener;
    }

    @Override
    public StudyRecyclerAdapterFromDocPointOfView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.study_item, parent, false);
        return new StudyRecyclerAdapterFromDocPointOfView.ViewHolder(v, mOnStudyListener);
    }

    @Override
    public void onBindViewHolder(final StudyRecyclerAdapterFromDocPointOfView.ViewHolder holder, final int position) {
        Study study = studyList.get(position);
        holder.pDoctorName.setText(study.getDoctorName());
        holder.pObservations.setText(study.getObservations());

    }

    @Override
    public int getItemCount() {
        return studyList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        StudyRecyclerAdapterFromDocPointOfView.OnStudyListener onStudyListener;
        public TextView pDoctorName;
        public TextView pObservations;

        public ViewHolder(View itemView, StudyRecyclerAdapterFromDocPointOfView.OnStudyListener onStudyListener) {
            super(itemView);

            pDoctorName = itemView.findViewById(R.id.doctors_surname_txt);
            pObservations = itemView.findViewById(R.id.observations_txt);
            this.onStudyListener = onStudyListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onStudyListener.onStudyClick(getAdapterPosition());
        }
    }

    //make interface like this
    public interface OnStudyListener {
        void onStudyClick(int position);
    }

}
