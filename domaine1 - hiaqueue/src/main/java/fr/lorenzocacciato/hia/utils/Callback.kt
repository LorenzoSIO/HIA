package fr.lorenzocacciato.hia.utils

import fr.lorenzocacciato.hiaqueue.model.DoctorReviewRequest
import fr.lorenzocacciato.hiaqueue.model.body.patient.AccountSession
import fr.lorenzocacciato.hiaqueue.model.body.patient.RequestSession

interface DoctorRefreshCalendarListener {
    fun onRefresh()
}

interface DoctorReviewApproveListener {
    fun onApprove()
}

interface DoctorReviewRequestListListener {
    fun onGet(meetingReviewRequest: Array<DoctorReviewRequest>)
}

interface PatientAccountSessionListener {
    fun onLoad(accountSession: AccountSession)
}

interface ConsultationTypeLoadingListener{
    fun isConsultationLoadedSucces()
    fun isConsultationLoadedError()
}

interface RequestSendingListener {
    fun onSending(requestSession: RequestSession)
}
