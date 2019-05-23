package fr.lorenzocacciato.hiaqueue.ui.patient

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import fr.lorenzocacciato.hiaconges.R
import fr.lorenzocacciato.hiaconges.persistence.HIACongesManager
import fr.lorenzocacciato.hiaconges.ui.rh.RHAbsencePanelActivity
import kotlinx.android.synthetic.main.fragment_doctor_panel.*
import kotlinx.android.synthetic.main.fragment_rh_panel.*


class RHAbsencePanelFragment : Fragment(){

    // services
    private val servicesManager: HIACongesManager? by lazy { HIACongesManager.getInstance() }

    /**
     * @inheritDoc
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_rh_panel, container, false)
    }

    /**
     * @inheritDoc
     */
    override fun onResume() {
        super.onResume()
        updateDoctorName()
        initClickButton()
    }

    private fun updateDoctorName() {
        // update on boarding doctor name value
        rh_request_onboard_title.text = resources.getString(R.string.rh_request_recycler_onboard_title,
            servicesManager?.getSession()!!.lastname)
    }

    private fun initClickButton() {
        // set up calendar button
        rh_request_recycler_onboard_button.setOnClickListener{
            startActivity(Intent(context, RHAbsencePanelActivity::class.java))
            activity?.overridePendingTransition(0, 0)
        }
    }

    companion object {
        const val TAG = "DoctorAbsencePanelFragment"
    }

}
