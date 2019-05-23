package fr.lorenzocacciato.hiaconges.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UserLoginSession {

    @SerializedName("uid")
    @Expose
    var uid: Int = 0

    @SerializedName("email")
    @Expose
    var email: String = ""

    @SerializedName("password")
    @Expose
    var password: String = ""

    @SerializedName("lastname")
    @Expose
    var lastname: String = ""

    @SerializedName("roleId")
    @Expose
    var roleId: Int = 1

    @SerializedName("error")
    @Expose
    var error: Boolean = true

    @SerializedName("token")
    @Expose
    var token: String = ""

    @SerializedName("mnemonic")
    @Expose
    var mnemonic: String = ""

}