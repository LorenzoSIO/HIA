package fr.lorenzocacciato.hiaqueue.ui.patient

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import fr.lorenzocacciato.hiaconges.R
import fr.lorenzocacciato.hiaconges.model.DoctorAbsenceRequest
import fr.lorenzocacciato.hiaconges.persistence.HIACongesManager
import fr.lorenzocacciato.hiaconges.ui.doctor.DoctorAbsencePanelActivity
import fr.lorenzocacciato.hiaconges.utils.RequestSendingListener
import fr.lorenzocacciato.hialib.components.LoadingButton
import fr.lorenzocacciato.hialib.extensions.formatToServerTime
import fr.lorenzocacciato.hialib.extensions.formatToViewDateDefaults
import fr.lorenzocacciato.hialib.extensions.setUpFragment
import kotlinx.android.synthetic.main.fragment_doctor_form_panel.*
import java.util.*


class DoctorAbsenceFormFragment : Fragment(), wheelpicker.aigestudio.widgets.WheelDatePicker.OnDateSelectedListener, RequestSendingListener {

    // services
    private val servicesManager: HIACongesManager? by lazy { HIACongesManager.getInstance() }

    // manager
    private val absenceFormData: DoctorAbsenceRequest by lazy { DoctorAbsenceRequest() }

    // context
    private val doctorAbsenceFormFragment by lazy { this }

    /**
     * @inheritDoc
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_doctor_form_panel, container, false)
    }

    /**
     * @inheritDoc
     */
    override fun onResume() {
        super.onResume()
        initWheelPicker()
        initConfirmButton()
        initCancelButton()
        updateDate()
    }

    private fun initWheelPicker() {
        doctor_request_details_section_date_picker.setOnDateSelectedListener(this)
    }

    private fun initConfirmButton() {
        doctor_request_details_confirm_button.setOnLoadingButtonEndAnimationListener(object: LoadingButton.LoadingButtonListener {
            override fun onEndAnimation() {
                servicesManager?.createAbsenceRequest(absenceFormData, doctorAbsenceFormFragment)
            }
        })

    }

    private fun initCancelButton() {
        doctor_request_details_cancel_button.setOnClickListener {
            fragmentManager?.setUpFragment(DoctorAbsencePanelFragment())
        }
    }

    private fun updateDate() {

        // get current selected date
        val currentDate = doctor_request_details_section_date_picker.currentDate

        // update confirm button component with the selected date
        val confirmButtonText = R.string.absence_request_details_confirm_button_title
        val currentDateText = currentDate.formatToViewDateDefaults()

        // enable loading button
        doctor_request_details_confirm_button.isEnable = true

        // set up confirm button text
        doctor_request_details_confirm_button.setText(resources.getString(confirmButtonText, currentDateText))

        // update form data with current data
        absenceFormData.plannedDate = currentDate.formatToServerTime()

    }

    override fun onSending(requestSession: DoctorAbsenceRequest) {

        // redirect to panel fragment
        startActivity(Intent(context?.applicationContext, DoctorAbsencePanelActivity::class.java))

    }

    override fun onDateSelected(picker: wheelpicker.aigestudio.widgets.WheelDatePicker?, date: Date?) {  updateDate() }

    companion object {
        const val TAG = "DoctorAbsenceFormFrag"
    }

}
