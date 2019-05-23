package fr.lorenzocacciato.hiaqueue.ui.patient

import android.content.Intent
import android.os.Bundle
import androidx.annotation.Nullable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import fr.lorenzocacciato.hia.R
import fr.lorenzocacciato.hia.persistence.HIAQueueManager
import fr.lorenzocacciato.hia.ui.HomeActivity
import fr.lorenzocacciato.hia.utils.MeetingDialogFragment
import fr.lorenzocacciato.hia.utils.RequestSendingListener
import fr.lorenzocacciato.hiaqueue.model.data.MeetingFormData
import fr.lorenzocacciato.hiaqueue.model.body.patient.RequestSession

class MeetingFormDetailsFragment : MeetingDialogFragment(),
    RequestSendingListener {

    private val meetingFormData: MeetingFormData by lazy { arguments!!.getParcelable<MeetingFormData>("meetingForm") }
    private val servicesManager: HIAQueueManager by lazy { HIAQueueManager.getInstance() }

    // components
    private val sendButtonView: Button by lazy { view!!.findViewById<Button>(R.id.meeting_form_details_send_button) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // inflate form details fragment
        return inflater?.inflate(R.layout.fragment_meeting_form_details, container, false)
    }

    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // show edit text values from meeting form data temp object
        initView(R.id.meeting_form_details_surname_value, meetingFormData.surname)
        initView(R.id.meeting_form_details_lastname_value, meetingFormData.lastname)
        initView(R.id.meeting_form_details_email_value, meetingFormData.email)
        initView(R.id.meeting_form_details_consultation_type_value, meetingFormData.consultationType!!.label)

        // set up form details click listener
        sendButtonView.setOnClickListener { servicesManager.createMeetingRequest(meetingFormData, this) }

    }

    override fun onSending(requestSession: RequestSession) {

        // create intent
        val homeIntent = Intent(context, HomeActivity::class.java)
        homeIntent.putExtra("requestSend", true)

        // redirect to home activity
        startActivity(homeIntent)

    }

    override fun onStart() {
        super.onStart()

        // define full screen dialog fragment
        val d = dialog
        if (d != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            d.window!!.setLayout(width, height)
        }
    }


}
