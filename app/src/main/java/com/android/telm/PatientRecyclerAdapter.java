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


//public class PatientRecyclerAdapter extends RecyclerView.Adapter<PatientRecyclerAdapter.ViewHolder> {
//
//    private Context context;
//    private List<Patient> patients;
//
//    public PatientRecyclerAdapter(Context context, List patients) {
//        this.context = context;
//        this.patients = patients;
//    }
//
//
//    @NonNull
//    @Override
//    public PatientRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_list_item, parent, false);
//        ViewHolder viewHolder = new ViewHolder(v);
//        return viewHolder;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull PatientRecyclerAdapter.ViewHolder holder, int position) {
//        holder.itemView.setTag(patients.get(position));
//        Patient patient = patients.get(position);
//        holder.pName.setText(patient.getName() + " " + patient.getSurname());
//        holder.pPesel.setText(patient.getPesel());
//    }
//
//    @Override
//    public int getItemCount() {
//        return 0;
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//
//        public TextView pName;
//        public TextView pPesel;
//
//        public ViewHolder(View itemView) {
//            super(itemView);
//
//            pName = (TextView) itemView.findViewById(R.id.nameSurnameTxt);
//            pPesel = (TextView) itemView.findViewById(R.id.peselTxt);
//
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    Patient cPatient = (Patient) view.getTag();
//
//                    Toast.makeText(view.getContext(), cPatient.getName() + " " + cPatient.getSurname() + " is " + cPatient.getPesel(), Toast.LENGTH_SHORT).show();
//                    //TODO: go to patient.
//
//                }
//            });
//        }
//    }
//}


public class PatientRecyclerAdapter extends RecyclerView.Adapter<PatientRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<Patient> list;

    public PatientRecyclerAdapter(Context context, List<Patient> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.single_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Patient patient = list.get(position);

        holder.name.setText(patient.getName() + " " + patient.getSurname());
        holder.pesel.setText(patient.getPesel());
//        holder.textYear.setText(patient.getYear());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name, surname, pesel;

        public ViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.nameSurnameTxt);
            pesel = itemView.findViewById(R.id.peselTxt);
//            textYear = itemView.findViewById(R.id.main_year);
        }
    }

}
