package fr.lorenzocacciato.hiaqueue.model.body.patient

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class RequestSession : Parcelable {

    @SerializedName("patient_id")
    @Expose
    var patientId: Int = 0

    @SerializedName("consultation_type_id")
    @Expose
    var consultationTypeId: Int = 0

    @SerializedName("preferences")
    @Expose
    var preferences: Int = 0

}