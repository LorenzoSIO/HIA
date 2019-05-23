package fr.lorenzocacciato.hia.ui

import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import fr.lorenzocacciato.hia.R
import fr.lorenzocacciato.hia.persistence.HIAQueueManager
import fr.lorenzocacciato.hia.utils.ConsultationTypeLoadingListener
import fr.lorenzocacciato.hialib.components.LoadingButton
import fr.lorenzocacciato.hialib.extensions.setUpFragment
import fr.lorenzocacciato.hialib.extensions.snack
import fr.lorenzocacciato.hiaqueue.ui.patient.HomePatientFragment
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : AppCompatActivity(), HIAQueueManager.HIALoadServiceListener, ConsultationTypeLoadingListener {

    // services
    private val servicesManager: HIAQueueManager by lazy { HIAQueueManager.getInstance() }
    private var patientMode = true

    /**
     * @inheritDoc
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
    }

    /**
     * @inheritDoc
     */
    override fun onResume() {
        super.onResume()

        // setup main patient fragment
        supportFragmentManager.setUpFragment(HomePatientFragment())

        // setup with patient / doctor fragment change
        setupToggleModeViewButton()

        // check if API is loaded
        servicesManager?.isLoaded(this)

    }

    override fun onSuccessLoading() {
        // load consultation types on service manager
        servicesManager!!.loadConsultationTypes(this)

        // enable cta button state
        findViewById<LoadingButton>(R.id.loading).isEnable = true
    }

    override fun onFailedLoading() {
        home_toggle_mode_switch_button.snack(getString(R.string.common_error_api_failed))
    }

    private fun setupToggleModeViewButton() {
        // set up switch mode text view
        findViewById<TextView>(R.id.home_toggle_mode_switch_button).apply {

            // set up underline paint flag
            paintFlags = Paint.UNDERLINE_TEXT_FLAG

            // set up on click listener
            setOnClickListener {
                when (patientMode) {
                    true ->
                    {
                        text = getString(R.string.home_toggle_doctor_mode_title)
                        supportFragmentManager.setUpFragment(HomePatientFragment())
                    }
                    false ->
                    {
                        text = getString(R.string.home_toggle_patient_mode_title)
                        supportFragmentManager.setUpFragment(HomeDoctorFragment())
                    }
                }
                patientMode = !patientMode
                setupToggleModeViewButton()
            }

        }
    }

    override fun isConsultationLoadedError() = onFailedLoading()

    override fun isConsultationLoadedSucces() {}

    override fun onBackPressed() { }

}