package com.android.telm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PatientCardRecyclerAdapter extends RecyclerView.Adapter<PatientCardRecyclerAdapter.ViewHolder>{
    private Context context;
    private List<Study> studyList;

    private PatientCardRecyclerAdapter.OnStudyListener mOnStudyListener;


    public PatientCardRecyclerAdapter(Context context, List<Study> studyList, PatientCardRecyclerAdapter.OnStudyListener mOnStudyListener) {
        this.context = context;
        this.studyList = studyList;
        this.mOnStudyListener = mOnStudyListener;
    }

    @Override
    public PatientCardRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.study_item_patient_view, parent, false);
        return new PatientCardRecyclerAdapter.ViewHolder(v, mOnStudyListener);
    }

    @Override
    public void onBindViewHolder(final PatientCardRecyclerAdapter.ViewHolder holder, final int position) {
        Study study = studyList.get(position);
        holder.pDoctorName.setText(study.getDoctorName());
        holder.pObservations.setText(study.getObservations());
    }

    @Override
    public int getItemCount() {
        return studyList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        PatientCardRecyclerAdapter.OnStudyListener onStudyListener;
        public TextView pDoctorName;
        public TextView pObservations;

        public ViewHolder(View itemView, PatientCardRecyclerAdapter.OnStudyListener onStudyListener) {
            super(itemView);

            pDoctorName = itemView.findViewById(R.id.doctors_surname_txt_patient_view);
            pObservations = itemView.findViewById(R.id.observations_txt_patient_view);
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
