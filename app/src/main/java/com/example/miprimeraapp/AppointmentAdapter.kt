package com.example.miprimeraapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AppointmentAdapter(private val appointments: List<Appointment>) :
    RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder>() {

    class AppointmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateTextView: TextView = itemView.findViewById(R.id.text_view_appointment_date)
        val timeTextView: TextView = itemView.findViewById(R.id.text_view_appointment_time)
        val descriptionTextView: TextView = itemView.findViewById(R.id.text_view_appointment_description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_appointment, parent, false)
        return AppointmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppointmentViewHolder, position: Int) {
        val appointment = appointments[position]

        // CAMBIO AQUÍ: Se formatea la fecha antes de mostrarla
        holder.dateTextView.text = appointment.date.split("-").reversed().joinToString("/")

        holder.timeTextView.text = appointment.time
        holder.descriptionTextView.text = appointment.description
    }

    override fun getItemCount(): Int {
        return appointments.size
    }
}