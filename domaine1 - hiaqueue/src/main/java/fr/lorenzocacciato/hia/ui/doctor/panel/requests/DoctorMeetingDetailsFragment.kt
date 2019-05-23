
package fr.lorenzocacciato.hiaqueue.ui.doctor.meetinglist

import android.os.Bundle
import androidx.annotation.Nullable
import androidx.fragment.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.lorenzocacciato.hia.R
import fr.lorenzocacciato.hia.persistence.HIAQueueManager
import fr.lorenzocacciato.hia.utils.DoctorReviewApproveListener
import fr.lorenzocacciato.hia.utils.MeetingDialogFragment
import fr.lorenzocacciato.hialib.ItemOffsetDecoration
import fr.lorenzocacciato.hialib.extensions.formatToViewDateDefaults
import fr.lorenzocacciato.hiaqueue.model.DoctorReviewRequest
import java.util.*

class DoctorMeetingDetailsFragment : MeetingDialogFragment(),
    wheelpicker.aigestudio.widgets.WheelDatePicker.OnDateSelectedListener,
    DoctorHoursCalendarAdapter.OnHourSelectListener, DoctorReviewApproveListener {

    private val servicesManager: HIAQueueManager by lazy { HIAQueueManager.getInstance() }

    private val wheelDatePickerView by lazy { view!!.findViewById<wheelpicker.aigestudio.widgets.WheelDatePicker>(R.id.doctor_request_details_section_date_picker) }
    private val hoursCalendarRecycler by lazy { view!!.findViewById<RecyclerView>(R.id.doctor_request_details_section_period_calendar) }

    private val confirmButton by lazy { view!!.findViewById<Button>(R.id.doctor_request_details_confirm_button) }
    private val cancelButton by lazy { view!!.findViewById<Button>(R.id.doctor_request_details_cancel_button) }

    private val meetingRequest by lazy { arguments!!.getParcelable<DoctorReviewRequest>("meetingRequest") }

    var activity: DoctorMeetingPanelActivity? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_doctor_meeting_details, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // init default dialog size to full screen mode
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }

    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // init meeting request data to views
        initView(R.id.doctor_request_details_onboard_lastname_value, meetingRequest.lastname.toUpperCase())
        initView(R.id.doctor_request_details_onboard_surname_value, meetingRequest.surname)
        initView(R.id.doctor_request_details_onboard_consultation_type_value, meetingRequest.consultationTypeName)

        // hour doctor adapter
        val hourDoctorAdapter = DoctorHoursCalendarAdapter(
            context!!,
            activity!!,
            wheelDatePickerView.currentDate,
            resources.getIntArray(R.array.hours_of_services)
        )

        // init meeting request to recycler
        hourDoctorAdapter.setCurrentMeetingRequest(meetingRequest)
        hoursCalendarRecycler.adapter = hourDoctorAdapter

        // init adapter to hours calendar recycler view
        hoursCalendarRecycler.layoutManager = GridLayoutManager(context, 4)
        hoursCalendarRecycler.addItemDecoration(
            ItemOffsetDecoration(
                context!!,
                R.dimen.dp2
            )
        )

        // init listeners
        hourDoctorAdapter.setOnHourSelectListener(this)
        wheelDatePickerView.setOnDateSelectedListener(this)

        // init confirm button click listener
        confirmButton.setOnClickListener {

            // update calendar with hour
            val calendar = Calendar.getInstance()
            calendar.time = wheelDatePickerView.currentDate
            calendar.set(Calendar.HOUR_OF_DAY, hourDoctorAdapter.getSelectedHours())

            // approve request
            val meetingRequestId = meetingRequest.meetingRequestId
            servicesManager.acceptMeetingRequest(meetingRequestId, calendar.time, this)

        }

        /* init cancel button click listener */
        cancelButton.setOnClickListener { dismiss() }

        /* init and update confirm button */
        updateDate()

    }

    override fun onDateSelected(picker: wheelpicker.aigestudio.widgets.WheelDatePicker?, date: Date?) {  updateDate() }

    override fun onHourSelected(hour: Int) { updateDate() }

    override fun onApprove() {

        // display notification result
        Toast.makeText(context, "onSent() ", Toast.LENGTH_SHORT).show()

        // back to meeting requests list
        dismiss()

        // call activity refresh recycler
        activity!!.onRefresh()

    }

    private fun updateDate() {

        // get current selected date
        val currentDate = wheelDatePickerView.currentDate

        // create new instance of doctor hours calendar adapter
        val hoursOfService = resources.getIntArray(R.array.hours_of_services)
        val hourCalendarAdapter = DoctorHoursCalendarAdapter(
            context!!,
            activity!!,
            currentDate,
            hoursOfService
        )

        // update adapter for current day
        hoursCalendarRecycler.adapter = hourCalendarAdapter

        // update confirm button component with the selected date
        val confirmButtonText = R.string.doctor_request_details_confirm_button_title
        val currentDateText = currentDate.formatToViewDateDefaults()
        val selectedHours = hourCalendarAdapter.getSelectedHours()
        confirmButton.text = resources.getString(confirmButtonText, currentDateText, selectedHours)

    }

}
