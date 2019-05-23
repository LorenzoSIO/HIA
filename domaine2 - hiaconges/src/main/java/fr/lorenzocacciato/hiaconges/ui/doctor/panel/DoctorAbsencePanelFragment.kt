package fr.lorenzocacciato.hiaqueue.ui.patient

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import fr.lorenzocacciato.hiaconges.R
import fr.lorenzocacciato.hiaconges.persistence.HIACongesManager
import fr.lorenzocacciato.hialib.extensions.setUpFragment
import kotlinx.android.synthetic.main.fragment_doctor_panel.*


class DoctorAbsencePanelFragment : Fragment(){

    private val servicesManager: HIACongesManager? by lazy { HIACongesManager.getInstance() }

    /**
     * @inheritDoc
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_doctor_panel, container, false)
    }

    /**
     * @inheritDoc
     */
    override fun onResume() {
        super.onResume()
        initClickButton()
        updateIcon()
        updateDoctorName()
    }

    private fun updateIcon() {
        doctor_request_role_icon.setImageResource(context?.resources!!.getIdentifier("ic_" + servicesManager?.getRole(), "drawable", this.context?.packageName))
    }

    private fun updateDoctorName() {
        // update on boarding doctor name value
        doctor_request_onboard_title.text = resources.getString(R.string.doctor_request_recycler_onboard_title,
            servicesManager?.getSession()!!.lastname)
    }

    private fun initClickButton() {
        // set up calendar button
        doctor_request_recycler_onboard_button.setOnClickListener{
            // redirect to calendar activity
            fragmentManager?.setUpFragment(DoctorAbsenceFormFragment())
        }
    }

    companion object {
        const val TAG = "DoctorAbsencePanelFragment"
    }

}
