
package fr.lorenzocacciato.hia.utils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import fr.lorenzocacciato.hia.R

abstract class MeetingDialogFragment : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // inflate meeting form dialog fragment
        return inflater?.inflate(R.layout.fragment_meeting_form_details, container, false)
    }

    fun initView(viewId: Int, value: String) {
        // set up text component value
        view!!.findViewById<TextView>(viewId).let { it.text = value }
    }

    override fun onStart() {
        super.onStart()
        // define full screen dialog fragment
        val d = dialog
        if (d != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            d.window!!.setLayout(width, height)
        }
    }


}
