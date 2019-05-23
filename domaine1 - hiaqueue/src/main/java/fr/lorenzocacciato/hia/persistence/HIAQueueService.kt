package fr.lorenzocacciato.hia.persistence

import fr.lorenzocacciato.hialib.model.Version
import fr.lorenzocacciato.hiaqueue.model.DoctorReviewRequest
import fr.lorenzocacciato.hiaqueue.model.body.doctor.DoctorLoginSession
import fr.lorenzocacciato.hiaqueue.model.body.doctor.DoctorRequestApproval
import fr.lorenzocacciato.hiaqueue.model.body.patient.AccountSession
import fr.lorenzocacciato.hiaqueue.model.body.patient.RequestSession
import fr.lorenzocacciato.hiaqueue.model.consultation.ConsultationType
import fr.lorenzocacciato.hiaqueue.model.consultation.MeetingRequestData
import retrofit2.Call
import retrofit2.http.*

interface HIAQueueService{

    @GET("/version")
    fun getVersion() : Call<Version>

    @GET("/hiaqueue/consultationType/all")
    fun getConsultationTypes() : Call<Array<ConsultationType>>

    @POST("/hiaqueue/patient/account")
    fun getPatientAccount(@Body meetingFormData: AccountSession) : Call<AccountSession>?

    @POST("/hiaqueue/patient/meeting/request")
    fun createMeetingRequest(@Header("x-patient-token") token: String, @Body meetingRequestBody: RequestSession) : Call<MeetingRequestData>

    @POST("/hiaqueue/doctor/account/login")
    fun login(@Body doctorAccountSession: DoctorLoginSession) : Call<DoctorLoginSession>

    @GET("/hiaqueue/doctor/meeting/request/all")
    fun getMeetingRequests(@Header("x-doctor-token") token: String) : Call<Array<DoctorReviewRequest>>

    @GET("/hiaqueue/doctor/meeting/calendar/")
    fun getMeetingCalendarRequests(@Header("x-doctor-token") token: String) : Call<Array<DoctorReviewRequest>>

    @POST("/hiaqueue/doctor/meeting/accept")
    fun acceptMeeting(@Header("x-doctor-token") token: String, @Body meetingRequestApproval: DoctorRequestApproval) : Call<DoctorRequestApproval>

}

