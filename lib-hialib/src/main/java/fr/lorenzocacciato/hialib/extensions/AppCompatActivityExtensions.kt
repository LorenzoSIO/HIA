package fr.lorenzocacciato.hialib.extensions

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import fr.lorenzocacciato.hialib.R
/**
 * Execute [func] in transaction
 *
 * @param func [FragmentTransaction].() -> [Unit]
 */
inline fun FragmentManager.setUpFragment(newFragment: Fragment) {
    // set up main patient fragment
    // Create new fragment and transaction
    val transaction = this.beginTransaction()

    // Replace whatever is in the fragment_container view with this fragment,
    // and add the transaction to the back stack if needed
    transaction.replace(R.id.root_frame, newFragment)
    transaction.addToBackStack(null)

    // Commit the transaction
    transaction.commit()
}
