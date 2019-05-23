package fr.lorenzocacciato.hiaqueue.ui.patient

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import fr.lorenzocacciato.hia.R
import fr.lorenzocacciato.hialib.components.LoadingButton
import kotlinx.android.synthetic.main.fragment_patient_home.*

class HomePatientFragment : Fragment(){

    /**
     * @inheritDoc
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_patient_home, container, false)
    }

    /**
     * @inheritDoc
     */
    override fun onResume() {
        super.onResume()
        setupLoadingButtonAnimationEndListener()
    }

    private fun setupLoadingButtonAnimationEndListener() {
        // set up on loading button end animation listener
        loading.setOnLoadingButtonEndAnimationListener(object: LoadingButton.LoadingButtonListener {
            override fun onEndAnimation() {
                startActivity(Intent(context, MeetingFormActivity::class.java))
            }
        })
    }


}
