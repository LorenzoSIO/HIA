package fr.lorenzocacciato.hiaconges.persistence

import android.util.Log
import fr.lorenzocacciato.hiaconges.model.DoctorAbsenceRequest
import fr.lorenzocacciato.hiaconges.model.UserLoginSession
import fr.lorenzocacciato.hiaconges.utils.DoctorAbsenceRequestListListener
import fr.lorenzocacciato.hiaconges.utils.RequestSendingListener
import fr.lorenzocacciato.hialib.persistence.DBAccessManager
import fr.lorenzocacciato.hialib.persistence.LoginListener
import fr.lorenzocacciato.hiaqueue.model.data.LogInFormData

class HIACongesManager : DBAccessManager<HIACongesService>(HIACongesService::class.java) {

    // cache
    private var userLoginSession: UserLoginSession = UserLoginSession()

    // singleton companion object
    companion object {
        private var INSTANCE: HIACongesManager ? = null

        fun getInstance() : HIACongesManager {
            if(INSTANCE == null){
                INSTANCE = HIACongesManager()
            }
            return INSTANCE!!
        }

    }

    fun loginAttempt(logInFormData: LogInFormData, loginListener: LoginListener) {

        // create doctor account login body
        userLoginSession.email = logInFormData.email
        userLoginSession.password = logInFormData.password

        // call rest api
        getService()?.login(userLoginSession)!!.callAPI(object: ResultCallback<UserLoginSession> {

            override fun onSuccess(userLoginResult: UserLoginSession?) {

                if (!userLoginResult!!.error)
                {
                    // assign it to local doctor session cache
                    userLoginSession = userLoginResult!!

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

    fun createAbsenceRequest(requestSession: DoctorAbsenceRequest, requestSendingListener: RequestSendingListener) {

        // get user uid
        requestSession.uid = userLoginSession.uid

        // call rest api
        getService()?.createAbsenceRequest(getSession().token, requestSession)!!.callAPI(object: ResultCallback<DoctorAbsenceRequest> {

            override fun onSuccess(body: DoctorAbsenceRequest?) {
                Log.d(TAG, "onSuccessMeetingRequestSending(${getSession().token})")
                requestSendingListener.onSending(body!!)
            }

            override fun onFailed() { }

        })

    }

   fun getAbsenceCalendarRequests(doctorReviewRequestListListener: DoctorAbsenceRequestListListener) {

        getService()?.getAbsenceCalendarRequests(userLoginSession.token, userLoginSession)?.callAPI(object : ResultCallback<Array<DoctorAbsenceRequest>> {

            override fun onSuccess(body: Array<DoctorAbsenceRequest>?) {
                doctorReviewRequestListListener.onGet(body!!)
            }

            override fun onFailed() { }

        })

    }

    fun acceptAbsenceRequest(doctorAbsenceRequest: DoctorAbsenceRequest, requestSendingListener: RequestSendingListener) {

        getService()?.acceptAbsenceRequest(userLoginSession.token, doctorAbsenceRequest)?.callAPI(object : ResultCallback<DoctorAbsenceRequest> {

            override fun onSuccess(body: DoctorAbsenceRequest?) {
                requestSendingListener.onSending(body!!)
            }

            override fun onFailed() {}

        })

    }

    fun getAbsenceCalendarWaitingRequests(doctorReviewRequestListListener: DoctorAbsenceRequestListListener) {

        getService()?.getAbsenceCalendarWaitingRequest(userLoginSession.token)?.callAPI(object : ResultCallback<Array<DoctorAbsenceRequest>> {

            override fun onSuccess(body: Array<DoctorAbsenceRequest>?) {
                doctorReviewRequestListListener.onGet(body!!)
            }

            override fun onFailed() { }

        })

    }

    fun getSession(): UserLoginSession { return userLoginSession }

    fun getRole(): String { return getSession().mnemonic }

    interface HIALoadServiceListener {

        fun onSuccessLoading()

        fun onFailedLoading()

    }

}