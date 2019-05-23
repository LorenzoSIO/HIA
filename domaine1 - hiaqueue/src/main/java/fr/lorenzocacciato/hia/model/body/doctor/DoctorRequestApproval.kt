package fr.lorenzocacciato.hiaqueue.model.body.doctor

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DoctorRequestApproval {

    @SerializedName("doctor_id")
    @Expose
    var doctorId: Int = 0

    @SerializedName("meeting_request_id")
    @Expose
    var meetingRequestId: Int = 0

    @SerializedName("meeting_date")
    @Expose
    var date: String = "2014-09-27T18:30:49-0300"

}
