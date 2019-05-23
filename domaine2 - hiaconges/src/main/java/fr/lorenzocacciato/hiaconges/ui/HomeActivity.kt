package fr.lorenzocacciato.hiaconges.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import fr.lorenzocacciato.hiaconges.R
import fr.lorenzocacciato.hialib.extensions.setUpFragment
import fr.lorenzocacciato.hiaqueue.ui.patient.HomeFragment

class HomeActivity : AppCompatActivity() {

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
        supportFragmentManager.setUpFragment(HomeFragment())

    }

    override fun onBackPressed() {  }

}
