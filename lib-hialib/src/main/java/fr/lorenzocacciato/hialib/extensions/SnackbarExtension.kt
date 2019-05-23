package fr.lorenzocacciato.hialib.extensions

import android.view.View

import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

import com.google.android.material.snackbar.Snackbar
import fr.lorenzocacciato.hialib.R

/**
 * Show an error snack
 */
fun View.snack(message: String) {
    val snack = Snackbar.make(this, message, Snackbar.LENGTH_LONG)
    val activity = context
    snack.view.setBackgroundColor(ContextCompat.getColor(activity, R.color.colorRed))
    snack.show()
}

/**
 * Show an hint snack
 */
fun View.snackHint(message: String) {
    val snack = Snackbar.make(this, message, Snackbar.LENGTH_LONG)
    val activity = context
    snack.view.setBackgroundColor(ContextCompat.getColor(activity, R.color.colorPrimary))
    snack.show()
}

/**
 * Show an error snack
 */
fun Fragment.snack(message: String) {
    view?.snack(message)
}

/**
 * Show an hint snack
 */
fun Fragment.snackHint(message: String) {
    view?.snackHint(message)
}
