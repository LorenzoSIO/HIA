package fr.lorenzocacciato.hiaconges.ui.rh

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.fragment.app.DialogFragment
import fr.lorenzocacciato.hiaconges.R
import fr.lorenzocacciato.hiaconges.model.DoctorAbsenceRequest
import fr.lorenzocacciato.hiaconges.persistence.HIACongesManager
import fr.lorenzocacciato.hiaconges.utils.RequestSendingListener
import fr.lorenzocacciato.hialib.components.LoadingButton
import fr.lorenzocacciato.hialib.extensions.formatToDate
import fr.lorenzocacciato.hialib.extensions.formatToViewDateDefaults
import kotlinx.android.synthetic.main.fragment_absence_form_details.*

class RHAbsencePanelDetailsFragment (var canConfirm: Boolean): DialogFragment(), RequestSendingListener,
    LoadingButton.LoadingButtonListener {

    // components
    private val servicesManager: HIACongesManager? by lazy { HIACongesManager.getInstance() }
    private val absenceRequest: DoctorAbsenceRequest by lazy { arguments!!.getParcelable<DoctorAbsenceRequest>("absenceRequest") }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // inflate form details fragment
        return inflater?.inflate(R.layout.fragment_absence_form_details, container, false)
    }

    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set up role icon
        home_onboard_title.setImageResource(context!!.resources.getIdentifier("ic_" + absenceRequest.mnemonic, "drawable", this.context!!.packageName))

        // set up details data
        initView(R.id.absence_details_surname_value, absenceRequest.lastname)
        initView(R.id.absence_details_role_value, absenceRequest.label)
        initView(R.id.absence_details_planned_date_value, absenceRequest.plannedDate.formatToDate().formatToViewDateDefaults())

        // set up confirm button to enabled state
        absence_details_send_button.isEnable = true

        // set up confirm button visibility
        absence_details_send_button.visibility = if (canConfirm) View.VISIBLE else View.GONE

        // set up confirm button listener
        absence_details_send_button.setOnLoadingButtonEndAnimationListener(this)

        // set up close/dismiss fragment dialog button
        absence_details_close_button.setOnClickListener { dismiss() }

    }

    override fun onEndAnimation() {
        servicesManager?.acceptAbsenceRequest(absenceRequest, this)
    }

    override fun onSending(requestSession: DoctorAbsenceRequest) {
        dismiss()
        startActivity(Intent(context, RHAbsencePanelActivity::class.java))
        activity?.overridePendingTransition(0, 0)

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

    fun initView(viewId: Int, value: String) {
        // set up text component value
        view!!.findViewById<TextView>(viewId).let { it.text = value }
    }


}
