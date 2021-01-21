package com.android.telm;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

public class PatientRecyclerAdapter extends RecyclerView.Adapter<PatientRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<Patient> patientList;

    //declare interface
    private OnPatientListener mOnPatientListener;



    public PatientRecyclerAdapter(Context context, List<Patient> patientList, OnPatientListener mOnPatientListener) {
        this.context = context;
        this.patientList = patientList;
        this.mOnPatientListener = mOnPatientListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.single_list_item, parent, false);
        return new ViewHolder(v, mOnPatientListener);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Patient patient = patientList.get(position);
        holder.pName.setText(patient.getName() + " " + patient.getSurname());
        holder.pPesel.setText(patient.getPesel());

    }

    @Override
    public int getItemCount() {
        return patientList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        OnPatientListener onPatientListener;
        public TextView pName;
        public TextView pPesel;

        public ViewHolder(View itemView, OnPatientListener onPatientListener) {
            super(itemView);

            pName = itemView.findViewById(R.id.nameSurnameTxt);
            pPesel = itemView.findViewById(R.id.peselTxt);

            this.onPatientListener = onPatientListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onPatientListener.onPatientClick(getAdapterPosition());
        }
    }

    //make interface like this
    public interface OnPatientListener {
        void onPatientClick(int position);
    }

}
