package fr.lorenzocacciato.hialib.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class Version : Parcelable {

    @SerializedName("error")
    @Expose
    var error: Boolean = true

    @SerializedName("data")
    @Expose
    var data: String = ""

    @SerializedName("message")
    @Expose
    var message: String = ""

}