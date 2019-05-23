package fr.lorenzocacciato.hiaconges.ui.rh

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import fr.lorenzocacciato.hiaconges.R
import fr.lorenzocacciato.hiaconges.model.DoctorAbsenceRequest
import fr.lorenzocacciato.hiaconges.persistence.HIACongesManager
import fr.lorenzocacciato.hiaconges.ui.HomeActivity
import fr.lorenzocacciato.hiaconges.utils.DoctorAbsenceRequestListListener
import fr.lorenzocacciato.hialib.extensions.setUpFragment
import fr.lorenzocacciato.hiaqueue.ui.patient.DoctorAbsencePanelFragment
import fr.lorenzocacciato.hiaqueue.ui.patient.RHAbsencePanelFragment
import kotlinx.android.synthetic.main.fragment_rh_panel.*
import java.util.*

class RHAbsencePanelActivity : AppCompatActivity() {

    // services
    private val servicesManager: HIACongesManager? by lazy { HIACongesManager.getInstance() }
    private val absenceCache: MutableMap<Date, List<DoctorAbsenceRequest>> by lazy { mutableMapOf<Date, List<DoctorAbsenceRequest>>() }
    private val context by lazy { this }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rh_absence_panel)
    }

    /**
     * @inheritDoc
     */
    override fun onResume() {
        super.onResume()

        // setup main patient fragment
        supportFragmentManager.setUpFragment(RHAbsencePanelFragment())

        initRequestCalendarRequests()

    }

    private fun initRequestCalendarRequests() {

        servicesManager?.getAbsenceCalendarWaitingRequests(object: DoctorAbsenceRequestListListener {

            override fun onGet(absenceReviewRequests: Array<DoctorAbsenceRequest>) {

                var absenceRequestList = arrayListOf<DoctorAbsenceRequest>()

                absenceReviewRequests.forEach {
                    absenceRequestList.add(it)
                }

                // init and assign adapter to recycler view
                rh_meeting_recycler_view.adapter =
                    RHAbsencePanelListAdapter(
                        context,
                        supportFragmentManager,
                        absenceRequestList
                    )

                // assign layout manager
                rh_meeting_recycler_view.layoutManager = LinearLayoutManager(context)
            }

        })

    }

    override fun onBackPressed() {
        startActivity(Intent(context, HomeActivity::class.java))
    }

}
