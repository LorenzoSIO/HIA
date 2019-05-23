
package fr.lorenzocacciato.hiaconges.ui.rh

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
import fr.lorenzocacciato.hialib.extensions.formatToDate
import fr.lorenzocacciato.hialib.extensions.formatToViewDateDefaults
import fr.lorenzocacciato.hialib.extensions.setUpFragment

class RHAbsencePanelListAdapter(
    private val context: Context,
    private val supportFragmentManager: FragmentManager,
    private var absenceRequests: MutableList<DoctorAbsenceRequest>
) : RecyclerView.Adapter<RHAbsencePanelListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflate doctor item template
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rh_absence_list, parent, false)
        return ViewHolder(context, view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val absenceRequest = absenceRequests[position]

        // set up absence role icon from mnemonic
        holder.iconView.setImageResource(context.resources.getIdentifier("ic_" + absenceRequest.mnemonic, "drawable", this.context.packageName))

        // set up absence doctor name and role
        holder.nameView.text = "${absenceRequest.lastname} - ${absenceRequest.label}"

        // set up absence request created date
        holder.requestDateView.text = context.resources.getString(R.string.rh_request_recycler_item_request_date, absenceRequest.plannedDate.formatToDate().formatToViewDateDefaults())

        // set up click listener
        holder.itemLayout.setOnClickListener {
            val newFragment = RHAbsencePanelDetailsFragment(true)
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
        var iconView  = itemView.findViewById<ImageView>(R.id.item_doctor_type_icon)
        var nameView  = itemView.findViewById<TextView>(R.id.item_doctor_name)
        var requestDateView  = itemView.findViewById<TextView>(R.id.item_doctor_request_date)
    }

}