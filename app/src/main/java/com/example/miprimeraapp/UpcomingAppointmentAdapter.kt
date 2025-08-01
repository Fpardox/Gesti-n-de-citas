package com.example.miprimeraapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UpcomingAppointmentAdapter(private val appointments: List<AppointmentWithClientInfo>) :
    RecyclerView.Adapter<UpcomingAppointmentAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val clientName: TextView = view.findViewById(R.id.upcoming_client_name)
        val description: TextView = view.findViewById(R.id.upcoming_appointment_description)
        val date: TextView = view.findViewById(R.id.upcoming_appointment_date)
        val time: TextView = view.findViewById(R.id.upcoming_appointment_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_upcoming_appointment, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = appointments[position]
        holder.clientName.text = item.clientName
        holder.description.text = item.description

        // CAMBIO AQUÍ: Se formatea la fecha antes de mostrarla
        holder.date.text = item.date.split("-").reversed().joinToString("/")

        holder.time.text = item.time
    }

    override fun getItemCount() = appointments.size
}