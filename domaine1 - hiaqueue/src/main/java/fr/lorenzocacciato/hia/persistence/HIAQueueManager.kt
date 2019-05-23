package fr.lorenzocacciato.hia.persistence

import android.util.Log
import fr.lorenzocacciato.hia.utils.*
import fr.lorenzocacciato.hialib.extensions.formatToServerTime
import fr.lorenzocacciato.hialib.model.Version
import fr.lorenzocacciato.hialib.persistence.DBAccessManager
import fr.lorenzocacciato.hialib.persistence.LoginListener
import fr.lorenzocacciato.hiaqueue.model.body.patient.AccountPatient
import fr.lorenzocacciato.hiaqueue.model.DoctorReviewRequest
import fr.lorenzocacciato.hiaqueue.model.body.patient.AccountSession
import fr.lorenzocacciato.hiaqueue.model.consultation.ConsultationType
import fr.lorenzocacciato.hiaqueue.model.consultation.MeetingRequestData
import fr.lorenzocacciato.hiaqueue.model.data.MeetingFormData
import fr.lorenzocacciato.hiaqueue.model.body.doctor.DoctorLoginSession
import fr.lorenzocacciato.hiaqueue.model.body.doctor.DoctorRequestApproval
import fr.lorenzocacciato.hiaqueue.model.body.patient.RequestSession
import fr.lorenzocacciato.hiaqueue.model.data.LogInFormData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class HIAQueueManager : DBAccessManager<HIAQueueService>(HIAQueueService::class.java) {

    // cache
    private var doctorLoginSession: DoctorLoginSession = DoctorLoginSession()
    private var consultationTypeList: MutableList<ConsultationType> = mutableListOf()

    // singleton companion object
    companion object {
        private var INSTANCE: HIAQueueManager ? = null

        fun getInstance() : HIAQueueManager {
            if(INSTANCE == null){
                INSTANCE = HIAQueueManager()
            }
            return INSTANCE!!
        }

    }

    fun isLoaded(hiaLoadServiceListener: HIALoadServiceListener) {
        getService()?.getVersion().callAPI(object: ResultCallback<Version> {

            override fun onSuccess(body: Version?) {
                if (!body!!.error) {
                    hiaLoadServiceListener.onSuccessLoading()
                } else {
                    hiaLoadServiceListener.onFailedLoading()
                }
            }

            override fun onFailed() {
                hiaLoadServiceListener.onFailedLoading()
            }

        })
    }

    fun loadConsultationTypes(consultationTypeLoadingListener: ConsultationTypeLoadingListener) {

        getService()?.getConsultationTypes()?.callAPI(object : ResultCallback<Array<ConsultationType>> {

            override fun onSuccess(body: Array<ConsultationType>?) {
                consultationTypeList = body!!.toMutableList()
                consultationTypeLoadingListener.isConsultationLoadedSucces()
            }

            override fun onFailed() {
                consultationTypeLoadingListener.isConsultationLoadedError()
            }

        })

    }

    fun getMeetingRequests(doctorReviewRequestListListener: DoctorReviewRequestListListener) {

        getService()?.getMeetingRequests(doctorLoginSession.token)?.callAPI(object : ResultCallback<Array<DoctorReviewRequest>> {

            override fun onSuccess(body: Array<DoctorReviewRequest>?) {
                doctorReviewRequestListListener.onGet(body!!)
            }

            override fun onFailed() { }
        })

    }

    fun getMeetingCalendarRequests(doctorReviewRequestListListener: DoctorReviewRequestListListener) {

        getService()?.getMeetingCalendarRequests(doctorLoginSession.token)?.callAPI(object : ResultCallback<Array<DoctorReviewRequest>> {

            override fun onSuccess(body: Array<DoctorReviewRequest>?) {
                doctorReviewRequestListListener.onGet(body!!)
            }

            override fun onFailed() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })

    }

    fun loadPatientAccount(meetingFormData: MeetingFormData, accountSessionListener: PatientAccountSessionListener) {

        // create account session body
        val body = AccountSession()
        val patient = AccountPatient()
        patient.lastname = meetingFormData.lastname
        patient.surname = meetingFormData.surname
        patient.email = meetingFormData.email
        body.patient = patient

        // get patient account from rest api and create account if not exist
        getService()?.getPatientAccount(body)!!.callAPI(object : ResultCallback<AccountSession> {

            override fun onSuccess(session: AccountSession?) {
                Log.d(TAG, "onSuccessPatientLoaded()")
                meetingFormData.authToken = session!!.token
                meetingFormData.patientId = session!!.patientId
                accountSessionListener.onLoad(session)
            }

            override fun onFailed() { }

        })

    }

    fun createMeetingRequest(meetingFormData: MeetingFormData, requestSendingListener: RequestSendingListener) {

        // create meeting request body
        val meetingRequestBody = RequestSession()
        meetingRequestBody.patientId = meetingFormData.patientId
        meetingRequestBody.consultationTypeId = meetingFormData.consultationType.id
        meetingRequestBody.preferences = 1

        // call rest api
        getService()?.createMeetingRequest(meetingFormData.authToken, meetingRequestBody)!!.callAPI(object: ResultCallback<MeetingRequestData> {

            override fun onSuccess(body: MeetingRequestData?) {
                Log.d(TAG, "onSuccessMeetingRequestSending()")
                requestSendingListener.onSending(meetingRequestBody)
            }

            override fun onFailed() {
                Log.d(TAG, "onFailingMeetingRequestSending()")
            }

        })

    }

    fun acceptMeetingRequest(meetingRequestId: Int, date: Date, doctorReviewApproveListener: DoctorReviewApproveListener) {

        // create meeting request approval body
        val requestApprovalBody = DoctorRequestApproval()
        requestApprovalBody.doctorId = doctorLoginSession.doctorId
        requestApprovalBody.meetingRequestId = meetingRequestId
        requestApprovalBody.date = date.formatToServerTime()

        // call rest api
        getService()?.acceptMeeting(doctorLoginSession.token, requestApprovalBody)!!.enqueue(object: Callback<DoctorRequestApproval> {

            override fun onResponse(call: Call<DoctorRequestApproval>, response: Response<DoctorRequestApproval>) {
                Log.d(TAG, "onSuccessRequestApproval()")
                doctorReviewApproveListener.onApprove()
            }

            override fun onFailure(call: Call<DoctorRequestApproval>, t: Throwable) { Log.d(TAG, "onFailingRequestApproval()") }

        })

    }

    fun loginAttempt(doctorLogInFormData: LogInFormData, loginListener: LoginListener) {

        // create doctor account login body
        doctorLoginSession.email = doctorLogInFormData.email
        doctorLoginSession.password = doctorLogInFormData.password

        // call rest api
        getService()?.login(doctorLoginSession)!!.callAPI(object: ResultCallback<DoctorLoginSession> {

            override fun onSuccess(doctorLoginResult: DoctorLoginSession?) {

                if (!doctorLoginResult!!.error)
                {
                    // assign it to local doctor session cache
                    doctorLoginSession = doctorLoginResult!!

                    // call on login success callback
                    loginListener.onLoginSuccess()
                }

                else
                {
                    // call on login failed callback
                    loginListener.onLoginFailed()
                }

            }

            override fun onFailed() {

                // call on login failed callback
                loginListener.onLoginFailed()
            }

        })

    }

    fun getDoctorSession(): DoctorLoginSession { return doctorLoginSession }

    fun getConsultationTypeById(position: Int): ConsultationType {
        return consultationTypeList[position]
    }

    fun getConsultationTypesName(): MutableList<String>? {
        val consultationTypeNames = mutableListOf<String>()
        consultationTypeList.forEach { consultationTypeNames.add(it.label) }
        return consultationTypeNames
    }

    interface HIALoadServiceListener {

        fun onSuccessLoading()

        fun onFailedLoading()

    }

}