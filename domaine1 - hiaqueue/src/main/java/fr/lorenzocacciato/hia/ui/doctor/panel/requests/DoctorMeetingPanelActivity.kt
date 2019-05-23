package fr.lorenzocacciato.hiaqueue.ui.doctor.meetinglist

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.lorenzocacciato.hia.R
import fr.lorenzocacciato.hia.persistence.HIAQueueManager
import fr.lorenzocacciato.hia.utils.DoctorRefreshCalendarListener
import fr.lorenzocacciato.hia.utils.DoctorReviewRequestListListener
import fr.lorenzocacciato.hiaqueue.model.DoctorReviewRequest
import fr.lorenzocacciato.hiaqueue.model.body.doctor.DoctorLoginSession
import fr.lorenzocacciato.hiaqueue.model.data.CalendarDate
import java.util.*

class DoctorMeetingPanelActivity : AppCompatActivity(), DoctorRefreshCalendarListener {

    // services
    private val servicesManager: HIAQueueManager by lazy { HIAQueueManager.getInstance() }

    // components
    private val meetingRecyclerView: RecyclerView by lazy { findViewById<RecyclerView>(R.id.meeting_recycler_view) }
    private val onBoardingDoctorNameView: TextView by lazy { findViewById<TextView>(R.id.doctor_request_onboard_title) }
    private val calendarButton: Button by lazy { findViewById<Button>(R.id.doctor_request_recycler_onboard_button) }

    // cache
    private val calendarCache: MutableMap<Date, DoctorReviewRequest> by lazy { mutableMapOf<Date, DoctorReviewRequest>() }
    private val doctorSession: DoctorLoginSession by lazy { servicesManager.getDoctorSession() }

    // contexts
    private val activity: DoctorMeetingPanelActivity by lazy { this }
    private val context: Context by lazy{ this }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // set up doctor meeting list layout
        setContentView(R.layout.activity_doctor_meeting_list)

        // set up on boarding doctor name view
        updateDoctorName()

        // calendar click button listener
        initClickButton()

        // refreshing requests list
        refreshMeetingRequests()
    }

    private fun updateDoctorName() {
        // update on boarding doctor name value
        onBoardingDoctorNameView.text = resources.getString(R.string.doctor_request_recycler_onboard_title, doctorSession.lastname)
    }

    private fun initClickButton() {
        // set up calendar button
        calendarButton.setOnClickListener{
            // redirect to calendar activity
            startActivity(Intent(applicationContext, DoctorMeetingCalendarActivity::class.java))
        }
    }

    private fun refreshMeetingRequests() {

        // getting meeting requests
        servicesManager.getMeetingRequests(object: DoctorReviewRequestListListener {

            override fun onGet(meetingReviewRequests: Array<DoctorReviewRequest>) {

                // create temp doctor review requests cache
                var meetingRequestList = arrayListOf<DoctorReviewRequest>()

                // inject values from call response
                meetingReviewRequests.forEach {
                    meetingRequestList.add(it)
                }

                // init and assign adapter to recycler view
                meetingRecyclerView.adapter =
                    DoctorMeetingListAdapter(
                        context,
                        supportFragmentManager,
                        activity,
                        meetingRequestList
                    )

                // set up layout manager
                meetingRecyclerView.layoutManager = LinearLayoutManager(context)

            }
        })

        // getting calendar meeting events
        servicesManager.getMeetingCalendarRequests(object: DoctorReviewRequestListListener {

            override fun onGet(meetingReviewRequests: Array<DoctorReviewRequest>) {
                // inject values from call response
                meetingReviewRequests.forEach { calendarCache[it.meetingDate] = it }
            }

        })

    }

    fun getCalendarByDate(currentDate: Date): CalendarDate {

        // set up calendar date object
        val calendarDate = CalendarDate()
        calendarDate.hasDate = false

        // find a date from the current doctor calendar
        if(calendarCache.containsKey(currentDate))
        {
            // turn has date to 'true'
            calendarDate.hasDate = true

            // set up calendar event
            calendarDate.event = calendarCache[currentDate]!!

        }

        // return this calendar date
        return calendarDate

    }

    override fun onRefresh() {
        // refreshing meeting request
        refreshMeetingRequests()
    }

    override fun onBackPressed() { finish() }

}
