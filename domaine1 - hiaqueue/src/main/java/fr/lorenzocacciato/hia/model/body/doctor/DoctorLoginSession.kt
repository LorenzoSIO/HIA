package fr.lorenzocacciato.hiaqueue.model.body.doctor

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DoctorLoginSession {

    @SerializedName("email")
    @Expose
    var email: String = ""

    @SerializedName("lastname")
    @Expose
    val lastname: String = ""

    @SerializedName("password")
    @Expose
    var password: String = ""

    @SerializedName("error")
    @Expose
    var error: Boolean = true

    @SerializedName("token")
    @Expose
    var token: String = ""

    @SerializedName("doctor_id")
    @Expose
    var doctorId: Int = 0

}