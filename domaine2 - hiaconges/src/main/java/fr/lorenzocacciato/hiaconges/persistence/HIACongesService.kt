package fr.lorenzocacciato.hiaconges.persistence

import fr.lorenzocacciato.hiaconges.model.DoctorAbsenceRequest
import fr.lorenzocacciato.hiaconges.model.UserLoginSession
import fr.lorenzocacciato.hialib.model.Version
import retrofit2.Call
import retrofit2.http.*

interface HIACongesService{

    @GET("/version")
    fun getVersion() : Call<Version>

    @POST("/hiaconges/login")
    fun login(@Body useLoginSession: UserLoginSession) : Call<UserLoginSession>

    @POST("/hiaconges/absenceRequest/create")
    fun createAbsenceRequest(@Header("x-doctor-token") token: String, @Body absenceRequestBody: DoctorAbsenceRequest) : Call<DoctorAbsenceRequest>

    @POST("/hiaconges/absence/request/all")
    fun getAbsenceCalendarRequests(@Header("x-doctor-token") token: String, @Body userLoginSession: UserLoginSession) : Call<Array<DoctorAbsenceRequest>>

    @GET("/hiaconges/absence/request/waiting")
    fun getAbsenceCalendarWaitingRequest(@Header("x-doctor-token") token: String) : Call<Array<DoctorAbsenceRequest>>

    @POST("/hiaconges/absence/request/accept")
    fun acceptAbsenceRequest(@Header("x-doctor-token") token: String, @Body doctorAbsenceRequest: DoctorAbsenceRequest) : Call<DoctorAbsenceRequest>

}
