package fr.lorenzocacciato.hiaqueue.model.body.patient

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AccountSession {

    @SerializedName("patient")
    @Expose
    var patient: AccountPatient =
        AccountPatient()

    @SerializedName("exist_before")
    @Expose
    var existBefore: Boolean = false

    @SerializedName("message")
    @Expose
    var message: String = ""

    @SerializedName("patient_id")
    @Expose
    var patientId: Int = 0

    @SerializedName("token")
    @Expose
    var token: String = ""

}
