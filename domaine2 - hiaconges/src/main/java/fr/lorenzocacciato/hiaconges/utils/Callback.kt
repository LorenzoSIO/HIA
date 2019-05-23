package fr.lorenzocacciato.hiaconges.utils

import fr.lorenzocacciato.hiaconges.model.DoctorAbsenceRequest

interface RequestSendingListener {
    fun onSending(requestSession: DoctorAbsenceRequest)
}

interface DoctorAbsenceRequestListListener {
    fun onGet(absenceReviewRequest: Array<DoctorAbsenceRequest>)
}
