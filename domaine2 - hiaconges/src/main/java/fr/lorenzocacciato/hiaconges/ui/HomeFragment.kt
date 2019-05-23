package fr.lorenzocacciato.hiaqueue.ui.patient

import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import fr.lorenzocacciato.hiaconges.R
import fr.lorenzocacciato.hiaconges.persistence.HIACongesManager
import fr.lorenzocacciato.hiaconges.ui.rh.RHAbsencePanelActivity
import fr.lorenzocacciato.hialib.components.LoadingButton
import fr.lorenzocacciato.hialib.extensions.afterTextChanged
import fr.lorenzocacciato.hialib.extensions.isValidEmail
import fr.lorenzocacciato.hialib.extensions.snack
import fr.lorenzocacciato.hialib.persistence.LoginListener
import fr.lorenzocacciato.hiaqueue.model.data.LogInFormData
import kotlinx.android.synthetic.main.activity_home.*
import fr.lorenzocacciato.hiaconges.ui.doctor.DoctorAbsencePanelActivity


class HomeFragment : Fragment(), HIACongesManager.HIALoadServiceListener, LoginListener {

    // services
    private val servicesManager: HIACongesManager? by lazy { HIACongesManager.getInstance() }

    // components
    private val editTextFormEmail: EditText by lazy { view!!.findViewById<EditText>(R.id.home_email_input) }
    private val editTextFormPassword: EditText by lazy { view!!.findViewById<EditText>(R.id.home_password_input) }
    private val sendButtonView: LoadingButton by lazy { view!!.findViewById<LoadingButton>(R.id.loading) }

    // user log in data
    private val userLogInFormData: LogInFormData by lazy { LogInFormData() }
    private val fragmentContext by lazy { this }

    /**
     * @inheritDoc
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    /**
     * @inheritDoc
     */
    override fun onResume() {
        super.onResume()
        initForm()
        setUpLogInAttemptButton()

        // auto login
        userLogInFormData.email = "medecin@gmail.com"
        userLogInFormData.password = "lorenzo99"
        checkButton()

    }

    private fun setUpLogInAttemptButton() {
        sendButtonView.setOnLoadingButtonEndAnimationListener(object: LoadingButton.LoadingButtonListener {
            override fun onEndAnimation() {
                // attempting login with inputs values
                servicesManager?.loginAttempt(userLogInFormData, fragmentContext)
            }
        })
    }

    override fun onLoginSuccess() {
        // get user role Id
        val roleId = servicesManager?.getSession()!!.roleId

        // notify good login result to the user
        Toast.makeText(context, "Login good ! $roleId", Toast.LENGTH_SHORT).show()

        // select rh or doctor absence panel type
        val activity = if (roleId == 1) RHAbsencePanelActivity::class.java else DoctorAbsencePanelActivity::class.java

        // start the activity instance
        startActivity(Intent(context, activity))
    }

    override fun onLoginFailed() {
        // notify failed login to the user
        Toast.makeText(context, "Login failed ! ", Toast.LENGTH_SHORT).show()

        // stop loading button
        sendButtonView.isLoading = false
    }

    private fun checkButton() {
        // check form completion
        sendButtonView.isEnable = userLogInFormData.isCompleted()
    }

    private fun initForm() {

        // set up password input mode
        editTextFormPassword.transformationMethod = PasswordTransformationMethod()

        // set up email input validator
        editTextFormEmail.afterTextChanged {
            userLogInFormData.email = it

            if(!it.isValidEmail())
            {
                userLogInFormData.email = ""
            }

            // check completion
            checkButton()
        }


        // set up password input validator
        editTextFormPassword.afterTextChanged {
            userLogInFormData.password = it

            // check completion
            checkButton()
        }

    }

    override fun onSuccessLoading() {
        // enable cta button state
        view!!.findViewById<LoadingButton>(R.id.loading).isEnable = true
    }

    override fun onFailedLoading() {
        home_toggle_mode_switch_button.snack(getString(R.string.common_error_api_failed))
    }

    companion object {
        const val TAG = "HomeFragment"
    }

}
