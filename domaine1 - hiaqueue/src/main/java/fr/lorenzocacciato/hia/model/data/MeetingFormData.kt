package fr.lorenzocacciato.hiaqueue.model.data

import android.os.Parcelable
import fr.lorenzocacciato.hiaqueue.model.consultation.ConsultationType
import kotlinx.android.parcel.Parcelize

@Parcelize
class MeetingFormData: Parcelable {

    var patientId: Int = 0
    var authToken: String = ""
    var surname: String = ""
    var lastname: String = ""
    var email: String = ""
    var consultationType: ConsultationType = ConsultationType(0, "None")
    val preferences: Int = 1

    fun isCompleted(): Boolean {
        return !surname.isEmpty() && !lastname.isEmpty()
                && !email.isEmpty() && email != null && consultationType != null
    }

}

