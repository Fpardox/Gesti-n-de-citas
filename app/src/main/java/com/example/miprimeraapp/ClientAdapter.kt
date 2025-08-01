package com.example.miprimeraapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ClientAdapter(
    private var clients: List<Client>,
    private val onItemClicked: (Client) -> Unit,
    private val onDeleteClicked: (Client) -> Unit,
    private val onAddAppointmentClicked: (Client) -> Unit // NUEVA ACCIÓN
) : RecyclerView.Adapter<ClientAdapter.ClientViewHolder>() {

    class ClientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.text_view_client_name)
        val phoneTextView: TextView = itemView.findViewById(R.id.text_view_client_phone)
        val deleteButton: ImageButton = itemView.findViewById(R.id.button_delete)
        val addAppointmentButton: ImageButton = itemView.findViewById(R.id.button_add_appointment) // NUEVO BOTÓN
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClientViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_client, parent, false)
        return ClientViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClientViewHolder, position: Int) {
        val client = clients[position]
        holder.nameTextView.text = client.name
        holder.phoneTextView.text = client.phone

        holder.deleteButton.setOnClickListener { onDeleteClicked(client) }
        holder.itemView.setOnClickListener { onItemClicked(client) }
        // NUEVA LÓGICA
        holder.addAppointmentButton.setOnClickListener { onAddAppointmentClicked(client) }
    }

    override fun getItemCount(): Int {
        return clients.size
    }
}