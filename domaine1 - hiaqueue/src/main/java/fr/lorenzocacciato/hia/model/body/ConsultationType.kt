package fr.lorenzocacciato.hiaqueue.model.consultation

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ConsultationType(

    @SerializedName("id")
    @Expose
    var id: Int = 0,

    @SerializedName("label")
    @Expose
    var label: String = "",

    @SerializedName("estimationTime")
    @Expose
    var estimationTime: Int = 0

)
