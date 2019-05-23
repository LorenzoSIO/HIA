package fr.lorenzocacciato.hiaqueue.ui.patient

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import fr.lorenzocacciato.hia.R
import fr.lorenzocacciato.hia.persistence.HIAQueueManager
import fr.lorenzocacciato.hia.utils.ConsultationTypeLoadingListener
import fr.lorenzocacciato.hia.utils.PatientAccountSessionListener
import fr.lorenzocacciato.hialib.extensions.afterTextChanged
import fr.lorenzocacciato.hialib.extensions.isValidEmail
import fr.lorenzocacciato.hialib.extensions.snack
import fr.lorenzocacciato.hiaqueue.model.data.MeetingFormData
import fr.lorenzocacciato.hiaqueue.model.body.patient.AccountSession
import kotlinx.android.synthetic.main.activity_home.*

class MeetingFormActivity : AppCompatActivity(), wheelpicker.aigestudio.WheelPicker.OnItemSelectedListener,
    PatientAccountSessionListener, HIAQueueManager.HIALoadServiceListener, ConsultationTypeLoadingListener {

    // services
    private val servicesManager: HIAQueueManager by lazy { HIAQueueManager.getInstance() }

    // components
    private val wheelPickerView: wheelpicker.aigestudio.WheelPicker by lazy { findViewById<wheelpicker.aigestudio.WheelPicker>(R.id.meeting_form_consultation_type_list) }
    private val editTextFormSurName: EditText by lazy { findViewById<EditText>(R.id.meeting_form_surname_input) }
    private val editTextFormLastName: EditText by lazy { findViewById<EditText>(R.id.meeting_form_lastname_input) }
    private val editTextFormEmail: EditText by lazy { findViewById<EditText>(R.id.meeting_form_email_input) }
    private val estimationTimeView: TextView by lazy { findViewById<TextView>(R.id.meeting_form_consultation_estimation_time) }
    private val sendButtonView: Button by lazy { findViewById<Button>(R.id.meeting_form_send_button) }

    // manager
    private val meetingFormData: MeetingFormData by lazy { MeetingFormData() }

    // context
    private val context: Context by lazy { this }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        // set up meeting form form layout
        setContentView(R.layout.activity_meeting_request_form)

        // set up email validation input
        initForm()

        // assign to an onItemClickListener to perform click event
        wheelPickerView.setOnItemSelectedListener(this)

        // check if API is loaded
        servicesManager?.isLoaded(this)

    }

    private fun initForm() {

        // set up lastname input validator
        editTextFormLastName.afterTextChanged {
            meetingFormData.lastname = it

            // check completion
            checkButton()
        }

        // set up surname input validator
        editTextFormSurName.afterTextChanged {
            meetingFormData.surname = it

            // check completion
            checkButton()
        }

        // set up email input validator
        editTextFormEmail.afterTextChanged {
            meetingFormData.email = it

            // check email format
            if(!it.isValidEmail())
            {
                meetingFormData.email = ""
            }

            // check completion
            checkButton()
        }
    }

    private fun checkButton() {
        // check form completion
        if(meetingFormData.isCompleted()){
            // display meeting request button
            sendButtonView.isEnabled = true
            sendButtonView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary))
        } else{
            // hide meeting request button
            sendButtonView.isEnabled = false
            sendButtonView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorGray))
        }
    }

    private fun loadWheelPickerViewData() {

        // get list as wheel picker data
        wheelPickerView.data = servicesManager.getConsultationTypesName()

        // turn wheel picker as visible
        wheelPickerView.visibility = View.VISIBLE

        // set up a default consultation type list
        meetingFormData.consultationType = servicesManager.getConsultationTypeById(0)

        // set up default estimation time
        estimationTimeView.text = resources.getString(R.string.meeting_form_time_estimation_title, meetingFormData.consultationType!!.estimationTime)

    }


    override fun onSuccessLoading() {
        // load consultation types on service manager
        servicesManager!!.loadConsultationTypes(this)
    }

    override fun onFailedLoading() {
        wheelPickerView.visibility = View.GONE
        home_toggle_mode_switch_button.snack(getString(R.string.common_error_api_failed))
    }

    override fun onItemSelected(picker: wheelpicker.aigestudio.WheelPicker, data: Any, position: Int) {

        // get consultation type object from item position
        val consultationType = servicesManager.getConsultationTypeById(position)

        // update temp meeting form data object
        meetingFormData.consultationType = consultationType

        // update consultation estimation time
        estimationTimeView.text = resources.getString(R.string.meeting_form_time_estimation_title, meetingFormData.consultationType!!.estimationTime)

    }

    fun onSubmitForm(view: View) {

        // creating meeting form details fragment instance
        val newFragment = MeetingFormDetailsFragment()
        val bundle = Bundle()
        bundle.putParcelable("meetingForm", meetingFormData)
        newFragment.arguments = bundle

        // set up patient account
        servicesManager.loadPatientAccount(meetingFormData, this)

        // display and build current dialog fragment
        newFragment.show(supportFragmentManager, "dialog")

    }

    override fun isConsultationLoadedError() = onFailedLoading()

    override fun isConsultationLoadedSucces() = loadWheelPickerViewData()

    override fun onLoad(accountSession: AccountSession) {}


}

