package fr.lorenzocacciato.hiaqueue.model.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class LogInFormData: Parcelable {

    var email: String = ""
    var password: String = ""

    fun isCompleted(): Boolean {
        return !email.isEmpty() && !password.isEmpty()
    }

}

