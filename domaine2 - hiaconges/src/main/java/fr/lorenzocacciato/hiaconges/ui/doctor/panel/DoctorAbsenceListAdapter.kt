
package fr.lorenzocacciato.hiaconges.ui.doctor.panel

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import fr.lorenzocacciato.hiaconges.R
import fr.lorenzocacciato.hiaconges.model.DoctorAbsenceRequest
import fr.lorenzocacciato.hiaconges.ui.rh.RHAbsencePanelDetailsFragment
import fr.lorenzocacciato.hialib.extensions.formatToDate
import fr.lorenzocacciato.hialib.extensions.formatToViewDateDefaults

class DoctorAbsenceListAdapter(
    private val context: Context,
    private val supportFragmentManager: FragmentManager,
    private var absenceRequests: MutableList<DoctorAbsenceRequest>
) : RecyclerView.Adapter<DoctorAbsenceListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflate doctor item template
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_doctor_absence_list, parent, false)
        return ViewHolder(context, view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val absenceRequest = absenceRequests[position]
        val iconResource = if (absenceRequest.status == 0) R.drawable.ic_hourglass else R.drawable.ic_confirm

        // set up absence icon status
        holder.iconView.setImageResource(iconResource)

        // set up absence request created date
        holder.plannedDateView.text = context.resources.getString(R.string.doctor_request_recycler_item_absence_request_planned, absenceRequest.plannedDate.formatToDate().formatToViewDateDefaults())

        // set up planned request date
        holder.createdDateView.text = context.resources.getString(R.string.doctor_request_recycler_item_absence_request_created, absenceRequest.createddate.formatToDate().formatToViewDateDefaults())

        // set up click listener
        holder.itemLayout.setOnClickListener {
            val newFragment = RHAbsencePanelDetailsFragment(false)
            val bundle = Bundle()
            bundle.putParcelable("absenceRequest", absenceRequest)
            newFragment.arguments = bundle

            // display and build current dialog fragment
            newFragment.show(supportFragmentManager, "dialog")

        }

    }

    override fun getItemCount(): Int {
        // count item from absence requests list
        return absenceRequests.size
    }

    class ViewHolder(context: Context, itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemLayout = itemView
        var iconView  = itemView.findViewById<ImageView>(R.id.item_absence_status_icon)
        var plannedDateView  = itemView.findViewById<TextView>(R.id.item_absence_planned_date)
        var createdDateView  = itemView.findViewById<TextView>(R.id.item_absence_created_date)
    }

}