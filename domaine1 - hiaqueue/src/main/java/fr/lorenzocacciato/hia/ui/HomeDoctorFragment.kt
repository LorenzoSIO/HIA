package fr.lorenzocacciato.hia.ui

import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import fr.lorenzocacciato.hia.R
import fr.lorenzocacciato.hia.persistence.HIAQueueManager
import fr.lorenzocacciato.hialib.components.LoadingButton
import fr.lorenzocacciato.hialib.extensions.afterTextChanged
import fr.lorenzocacciato.hialib.extensions.isValidEmail
import fr.lorenzocacciato.hialib.persistence.LoginListener
import fr.lorenzocacciato.hiaqueue.model.data.LogInFormData
import fr.lorenzocacciato.hiaqueue.ui.doctor.meetinglist.DoctorMeetingPanelActivity

class HomeDoctorFragment : Fragment(), LoginListener {

    // services
    private val servicesManager: HIAQueueManager? by lazy { HIAQueueManager.getInstance() }

    // components
    private val editTextFormEmail: EditText by lazy { view!!.findViewById<EditText>(R.id.doctor_home_email_input) }
    private val editTextFormPassword: EditText by lazy { view!!.findViewById<EditText>(R.id.doctor_home_password_input) }
    private val sendButtonView: LoadingButton by lazy { view!!.findViewById<LoadingButton>(R.id.loading) }

    // doctor log in data
    private val doctorLogInFormData: LogInFormData by lazy { LogInFormData() }
    private val fragmentContext by lazy { this }

    /**
     * @inheritDoc
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_doctor_home, container, false)
    }

    override fun onResume() {
        super.onResume()
        initForm()
        setUpLogInAttemptButton()
    }

    private fun setUpLogInAttemptButton() {
        sendButtonView.setOnLoadingButtonEndAnimationListener(object: LoadingButton.LoadingButtonListener {
            override fun onEndAnimation() {
                // attempting login with inputs values
                servicesManager?.loginAttempt(doctorLogInFormData, fragmentContext)
            }
        })
    }

    override fun onLoginSuccess() {
        // redirect to DoctorMeetingPanel activity
        startActivity(Intent(context, DoctorMeetingPanelActivity::class.java))

        // notify good login result to the user
        Toast.makeText(context, "Login good ! ", Toast.LENGTH_SHORT).show()
    }

    override fun onLoginFailed() {
        // notify failed login to the user
        Toast.makeText(context, "Login failed ! ", Toast.LENGTH_SHORT).show()

        // stop loading button
        sendButtonView.isLoading = false
    }

    private fun checkButton() {
        // check form completion
        sendButtonView.isEnable = doctorLogInFormData.isCompleted()
    }

    private fun initForm() {

        // set up password input mode
        editTextFormPassword.transformationMethod = PasswordTransformationMethod()

        // set up email input validator
        editTextFormEmail.afterTextChanged {
            doctorLogInFormData.email = it

            if(!it.isValidEmail())
            {
                doctorLogInFormData.email = ""
            }

            // check completion
            checkButton()
        }


        // set up password input validator
        editTextFormPassword.afterTextChanged {
            doctorLogInFormData.password = it

            // check completion
            checkButton()
        }

    }


}
