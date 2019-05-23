package fr.lorenzocacciato.hiaqueue.ui.doctor.meetinglist


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.lorenzocacciato.hia.R
import fr.lorenzocacciato.hialib.extensions.formatToServerDateTimeDefaults
import fr.lorenzocacciato.hiaqueue.model.DoctorReviewRequest

class DoctorCalendarListAdapter(
    private val context: Context,
    private var meetingRequests: MutableList<DoctorReviewRequest>
) : RecyclerView.Adapter<DoctorCalendarListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflate doctor item template
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_doctor_meeting_list, parent, false)
        return ViewHolder(context, view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val meetingRequest = meetingRequests[position]

        // set up meeting request consultation type name
        holder.consultationName.text = meetingRequest.consultationTypeName

        // set up meeting request author surname and last_name
        holder.authorFullName.text = context.resources.getString(R.string.doctor_request_recycler_item_meeting_request_author, meetingRequest.lastname, meetingRequest.surname)

        // set up meeting request created date
        holder.createdDate.text = context.resources.getString(R.string.doctor_request_recycler_item_meeting_request_date, meetingRequest.createdDate.formatToServerDateTimeDefaults())

        // turn left arrow to 'invisible' mode
        holder.arrow.visibility = View.INVISIBLE

    }

    override fun getItemCount(): Int {

        // count item from meeting requests list
        return meetingRequests!!.size
    }

    class ViewHolder(context: Context, itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemLayout = itemView
        var consultationName  = itemView.findViewById<TextView>(R.id.item_consultation_name)
        var authorFullName = itemView.findViewById<TextView>(R.id.item_meeting_author)
        var createdDate = itemView.findViewById<TextView>(R.id.item_meeting_request_date)
        var arrow = itemView.findViewById<ImageView>(R.id.item_meeting_arrow)
    }

}