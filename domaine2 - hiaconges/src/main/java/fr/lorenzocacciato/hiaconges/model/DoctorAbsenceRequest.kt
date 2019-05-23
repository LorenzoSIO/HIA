package fr.lorenzocacciato.hiaconges.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.sql.Timestamp

@Parcelize
class DoctorAbsenceRequest : Parcelable {

    @SerializedName("id")
    @Expose
    var id: Int = 1

    @SerializedName("absenceId")
    @Expose
    var absenceId: Int = 1

    @SerializedName("lastname")
    @Expose
    var lastname: String = ""

    @SerializedName("label")
    @Expose
    var label: String = ""

    @SerializedName("mnemonic")
    @Expose
    var mnemonic: String = ""

    @SerializedName("uid")
    @Expose
    var uid: Int = 0

    @SerializedName("plannedDate")
    @Expose
    var plannedDate: String = "2050-09-27T18:30:49-0300"

    @SerializedName("createddate")
    @Expose
    var createddate: String = "2050-09-27T18:30:49-0300"

    @SerializedName("status")
    @Expose
    var status: Int = 0

}