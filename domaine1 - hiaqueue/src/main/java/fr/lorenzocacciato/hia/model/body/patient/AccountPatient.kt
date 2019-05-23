package fr.lorenzocacciato.hiaqueue.model.body.patient

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class AccountPatient : Parcelable {

    @SerializedName("id")
    @Expose
    var id: Int = 0

    @SerializedName("lastname")
    @Expose
    var lastname: String = ""

    @SerializedName("surname")
    @Expose
    var surname: String = ""

    @SerializedName("email")
    @Expose
    var email: String = ""


}
