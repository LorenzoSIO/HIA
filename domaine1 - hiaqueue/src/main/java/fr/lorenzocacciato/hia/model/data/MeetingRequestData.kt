package fr.lorenzocacciato.hiaqueue.model.consultation

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MeetingRequestData(
    var surname: String = "",
    var lastname: String = "",
    var consultationType: String = "",
    var reason: String = "",
    var createdDate: String = "24/01/2019",
    var preference: Int = 0
) : Parcelable
