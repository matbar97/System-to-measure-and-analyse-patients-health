package com.android.telm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StudyRecyclerAdapterFromDocPointOfView extends RecyclerView.Adapter<StudyRecyclerAdapterFromDocPointOfView.ViewHolder> {

    private Context context;
    private List<Study> studyList;

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
        holder.pStudyDate.setText(study.getDateAdded());
    }

    @Override
    public int getItemCount() {
        return studyList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        StudyRecyclerAdapterFromDocPointOfView.OnStudyListener onStudyListener;
        public TextView pDoctorName;
        public TextView pObservations, pStudyDate;

        public ViewHolder(View itemView, StudyRecyclerAdapterFromDocPointOfView.OnStudyListener onStudyListener) {
            super(itemView);

            pDoctorName = itemView.findViewById(R.id.doctors_surname_txt);
            pObservations = itemView.findViewById(R.id.observations_txt);
            pStudyDate = itemView.findViewById(R.id.date_study_txt);
            this.onStudyListener = onStudyListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onStudyListener.onStudyClick(getAdapterPosition());
        }
    }

    public interface OnStudyListener {
        void onStudyClick(int position);
    }

}
