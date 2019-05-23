package fr.lorenzocacciato.hiaqueue.ui.doctor.meetinglist


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import fr.lorenzocacciato.hia.R
import fr.lorenzocacciato.hiaqueue.model.DoctorReviewRequest
import java.util.*

class DoctorHoursCalendarAdapter(
    private val context: Context,
    private val activity: DoctorMeetingPanelActivity,
    private val currentDate: Date,
    private var hoursOfService: IntArray
) : RecyclerView.Adapter<DoctorHoursCalendarAdapter.ViewHolder>() {

    // cache
    private var hourSelectListener: OnHourSelectListener? = null
    private var selectedHours = hoursOfService[0]
    private var lastSelectedItem: TextView? = null

    // current review request
    private var meetingRequest: DoctorReviewRequest = DoctorReviewRequest()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflate doctor calendar hours item
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_calendar_hours, parent, false)
        return ViewHolder(context, view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        // set up item hour view
        holder.itemHourView.text = context.resources.getString(R.string.doctor_request_details_section_period_item, hoursOfService[position])

        // set up star icons period preference
        if (isInPeriod(meetingRequest.preferences, hoursOfService[position]))
        {
            // turn visible to 'visible'
            holder.itemStarIconView.visibility = View.VISIBLE
        }

        // create calendar with current hour
        val mCalendar = Calendar.getInstance()
        mCalendar.time = currentDate
        mCalendar.set(Calendar.HOUR, hoursOfService[position])

        // get calendar date from calendar cache
        val calendarDate = activity.getCalendarByDate(mCalendar.time)

        // check if calendar cache containing date
        if(calendarDate.hasDate) {

            // disable calendar event with a date
            holder.itemHourView.alpha = 0.4f
            holder.itemLayout.isEnabled = false

            // turn visible to 'gone'
            holder.itemStarIconView.visibility = View.GONE

        }

        // assign on select hour item listener
        holder.itemLayout.setOnClickListener { selectHourItem(holder, position) }

    }

    private fun selectHourItem(holder: ViewHolder, position: Int) {

        // if has previous selected item
        if(lastSelectedItem != null) {
            // reset color to previous selected item
            lastSelectedItem!!.setBackgroundColor(ContextCompat.getColor(context, R.color.colorDarkGray))
        }

        // set up new selected item
        lastSelectedItem = holder.itemHourView
        selectedHours = hoursOfService[position]

        // update background item color
        lastSelectedItem!!.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPurple))

        // hour select callback
        hourSelectListener?.onHourSelected(selectedHours)

    }

    override fun getItemCount(): Int {
        // get numbers of hours
        return hoursOfService.size
    }

    private fun isInPeriod(periodPreference: Int, hour: Int): Boolean {
        // check preferences period internal to set up stars
        return if(periodPreference == 1) (hour <= 12) else (hour > 12)
    }

    fun setOnHourSelectListener(hourSelectListener: OnHourSelectListener) {
        // set up current class listener callback
        this.hourSelectListener = hourSelectListener
    }

    fun setCurrentMeetingRequest(meetingReviewRequest: DoctorReviewRequest) {
        // set up current meeting request listener callback
        this.meetingRequest = meetingReviewRequest
    }

    fun getSelectedHours(): Int {
        // get selected hours
        return selectedHours
    }

    class ViewHolder(context: Context, itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemLayout = itemView
        var itemHourView = itemLayout.findViewById<TextView>(R.id.item_calendar_hours)
        var itemStarIconView = itemLayout.findViewById<ImageView>(R.id.item_calendar_star)
    }

    interface OnHourSelectListener {
        fun onHourSelected(hour: Int)
    }


}