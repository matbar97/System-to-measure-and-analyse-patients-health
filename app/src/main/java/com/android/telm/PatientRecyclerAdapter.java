package com.android.telm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class PatientRecyclerAdapter extends RecyclerView.Adapter<PatientRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<Patient> patients;

    public PatientRecyclerAdapter(Context context, List personUtils) {
        this.context = context;
        this.patients = personUtils;
    }


    @NonNull
    @Override
    public PatientRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PatientRecyclerAdapter.ViewHolder holder, int position) {
        holder.itemView.setTag(patients.get(position));
        Patient patient = patients.get(position);
        holder.pName.setText(patient.getName() + " " + patient.getSurname());
        holder.pPesel.setText(patient.getPesel());
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView pName;
        public TextView pPesel;

        public ViewHolder(View itemView) {
            super(itemView);

            pName = (TextView) itemView.findViewById(R.id.patientNametxt);
            pPesel = (TextView) itemView.findViewById(R.id.patientPeselTxt);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Patient cPatient = (Patient) view.getTag();

                    Toast.makeText(view.getContext(), cPatient.getName() + " " + cPatient.getSurname() + " is " + cPatient.getPesel(), Toast.LENGTH_SHORT).show();
                    //TODO: go to patient.

                }
            });
        }
    }
}
