package fr.lorenzocacciato.hialib.extensions

import java.text.SimpleDateFormat
import java.util.*


/**
 * Pattern: dd-MM-yyyy HH:mm:ss
 */
fun Date.formatToServerDateTimeDefaults(): String{
    val sdf= SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
    return sdf.format(this)
}

/**
 * Pattern: HH:mm:ss
 */
fun Date.formatToServerTime(): String{
    val sdf= SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    return sdf.format(this)
}

fun String.formatToDate(): Date {
    return SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(this)
}

/**
 * Pattern: dd/MM/yyyy
 */
fun Date.formatToViewDateDefaults(): String{
    val sdf= SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return sdf.format(this)
}

/**
 * Add field date to current date
 */
fun Date.add(field: Int, amount: Int): Date{
    val cal = Calendar.getInstance()
    cal.time=this
    cal.add(field, amount)

    this.time = cal.time.time

    cal.clear()

    return this
}