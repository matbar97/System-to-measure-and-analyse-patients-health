package com.android.telm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class DoctorsRecyclerAdapterAdminView extends RecyclerView.Adapter<DoctorsRecyclerAdapterAdminView.ViewHolder> {

    private Context context;
    private List<Doctor> doctorList;

    private OnDoctorListener mOnDoctorListener;

    public DoctorsRecyclerAdapterAdminView(Context context, List<Doctor> doctorList, OnDoctorListener mOnDoctorListener) {
        this.context = context;
        this.doctorList = doctorList;
        this.mOnDoctorListener = mOnDoctorListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.single_doctor_item, parent, false);
        return new ViewHolder(v, mOnDoctorListener);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Doctor doctor = doctorList.get(position);
        holder.pName.setText(doctor.getName()); //+ " " + doctor.getSurname());
        holder.pPesel.setText(doctor.getPesel());
    }

    @Override
    public int getItemCount() {
        return doctorList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        OnDoctorListener onDoctorListener;
        public TextView pName;
        public TextView pPesel;

        public ViewHolder(View itemView, OnDoctorListener onDoctorListener) {
            super(itemView);

            pName = itemView.findViewById(R.id.doctors_surname_txt_admin);
            pPesel = itemView.findViewById(R.id.doctors_pesel_txt_admin);

            this.onDoctorListener = onDoctorListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onDoctorListener.onDocClick(getAdapterPosition());
        }
    }

    public interface OnDoctorListener {
        void onDocClick(int position);
    }

}
