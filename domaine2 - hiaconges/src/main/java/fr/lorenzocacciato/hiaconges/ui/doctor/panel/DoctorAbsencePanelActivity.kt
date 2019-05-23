package fr.lorenzocacciato.hiaconges.ui.doctor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import fr.lorenzocacciato.hiaconges.R
import fr.lorenzocacciato.hiaconges.model.DoctorAbsenceRequest
import fr.lorenzocacciato.hiaconges.persistence.HIACongesManager
import fr.lorenzocacciato.hiaconges.ui.HomeActivity
import fr.lorenzocacciato.hiaconges.ui.doctor.panel.DoctorAbsenceListAdapter
import fr.lorenzocacciato.hiaconges.utils.DoctorAbsenceRequestListListener
import fr.lorenzocacciato.hialib.extensions.setUpFragment
import fr.lorenzocacciato.hiaqueue.ui.patient.DoctorAbsencePanelFragment
import kotlinx.android.synthetic.main.fragment_doctor_panel.*

class DoctorAbsencePanelActivity : AppCompatActivity() {

    // services
    private val servicesManager: HIACongesManager? by lazy { HIACongesManager.getInstance() }
    private val context by lazy { this }

    /**
     * @inheritDoc
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_absence_panel)
    }

    /**
     * @inheritDoc
     */
    override fun onResume() {
        super.onResume()

        // setup main absence panel fragment
        supportFragmentManager.setUpFragment(DoctorAbsencePanelFragment())

        initAbsenceCalendarRequests()

    }

    private fun initAbsenceCalendarRequests() {

        servicesManager?.getAbsenceCalendarRequests(object: DoctorAbsenceRequestListListener {

            override fun onGet(absenceReviewRequests: Array<DoctorAbsenceRequest>) {

                var absenceRequestList = arrayListOf<DoctorAbsenceRequest>()

                absenceReviewRequests.forEach {
                    absenceRequestList.add(it)
                }

                // init and assign adapter to recycler view
                absence_request_recycler_view.adapter =
                    DoctorAbsenceListAdapter(
                        context,
                        supportFragmentManager,
                        absenceRequestList
                    )

                // assign layout manager
                absence_request_recycler_view.layoutManager = LinearLayoutManager(context)
            }

        })
    }


    /**
     * @inheritDoc
     */
    override fun onBackPressed() {
        startActivity(Intent(context, HomeActivity::class.java))
    }

}
