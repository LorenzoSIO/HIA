package fr.lorenzocacciato.hiaqueue.ui.doctor.meetinglist

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.lorenzocacciato.hia.R
import fr.lorenzocacciato.hia.persistence.HIAQueueManager
import fr.lorenzocacciato.hia.utils.DoctorRefreshCalendarListener
import fr.lorenzocacciato.hia.utils.DoctorReviewRequestListListener
import fr.lorenzocacciato.hiaqueue.model.DoctorReviewRequest

class DoctorMeetingCalendarActivity : AppCompatActivity(), DoctorRefreshCalendarListener {

    private val context: Context by lazy{ this }
    private val servicesManager: HIAQueueManager by lazy { HIAQueueManager.getInstance() }

    private val backButton: Button by lazy { findViewById<Button>(R.id.doctor_request_recycler_onboard_button) }
    private val meetingCalendarRecyclerView: RecyclerView by lazy { findViewById<RecyclerView>(R.id.meeting_calendar_recycler_view) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_meeting_calendar_list)

        // set up back button
        backButton.setOnClickListener { finish() }

        // load meeting requests
        loadMeetingCalendarRequests()
    }

    private fun loadMeetingCalendarRequests() {
        servicesManager.getMeetingCalendarRequests(object: DoctorReviewRequestListListener {

            override fun onGet(meetingReviewRequests: Array<DoctorReviewRequest>) {

                var meetingRequestList = arrayListOf<DoctorReviewRequest>()

                meetingReviewRequests.forEach {
                    meetingRequestList.add(it)
                }

                // init and assign adapter to recycler view
                meetingCalendarRecyclerView.adapter =
                    DoctorCalendarListAdapter(
                        context,
                        meetingRequestList
                    )

                // assign layout manager
                meetingCalendarRecyclerView.layoutManager = LinearLayoutManager(context)
            }

        })
    }

    override fun onRefresh() {}

    override fun onBackPressed() { finish() }

}
