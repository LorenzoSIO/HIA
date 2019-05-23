package fr.lorenzocacciato.hiaqueue.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.sql.Date
import java.sql.Timestamp

@Parcelize
class DoctorReviewRequest : Parcelable {

    @SerializedName("id")
    @Expose
    var id: Int = 0

    @SerializedName("requestId")
    @Expose
    var meetingRequestId: Int = 0

    @SerializedName("lastname")
    @Expose
    var lastname: String = ""

    @SerializedName("surname")
    @Expose
    var surname: String = ""

    @SerializedName("label")
    @Expose
    var consultationTypeName: String = ""

    @SerializedName("preferences")
    @Expose
    var preferences: Int = 0

    @SerializedName("createddate")
    @Expose
    var createdDate: Timestamp = Timestamp(0)

    @SerializedName("meetingDate")
    @Expose
    var meetingDate: Timestamp = Timestamp(0)

}