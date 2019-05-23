
const mysql = require('mysql')
const express = require('express')
const bodyParser = require('body-parser');
const bcrypt = require('bcrypt-nodejs');
const jwt = require('jsonwebtoken');
const config = require('./config/default.json');

const app = express();
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: true}));
app.use(bodyParser.json({ type: 'application/*+json' }));
app.listen(80);


/** Common */
app.get('/version', function (req, res) { return res.send({
        error: false,
        data: '1.2.5',
        message: 'HIA Restful API v1.5.0'
    }); });

/** Domaine 1 - Hia Radiologie **/

// connection configurations 'hiaqueue'
const mc = mysql.createConnection(config.HIAQUEUE);
mc.connect();

// Allow to get all consultation types
app.get('/hiaqueue/consultationType/all', function(req, res) {
   mc.query('SELECT * FROM consultationType', function (error, results, fields) {
        if (error) throw error;
        return res.send(results);
    });});

// Allow to get one specific consultation type by unique identifier
app.get('/hiaqueue/consultationType/:id', function(req, res) {
    let consultation_type_id = req.params.id

    if (!consultation_type_id) {
        return res.status(400).send({ error: true, message: 'Please provide consultation_type_id' });
    }

    mc.query('SELECT * FROM consultationType where id = ?', consultation_type_id, function (error, results, fields) {
        if (error) throw error;
        return res.send(results[0]);
    });});

// Allow to check if patient has account, and create one if not exist
app.post('/hiaqueue/patient/account', function(req, res) {

    let patient = req.body.patient;

    if (!patient) {
        return res.status(400).send({ error:true, message: 'Please provide patient' });
    }

    mc.query('SELECT * FROM patient where email = ?', [patient.email] , function (error, results, fields) {

        if (error) throw error;

        // Already have account
        if(results.length >= 1)
        {
            // create a patient token
            var token = jwt.sign({ id: patient.id }, config.JWT.patient, {
              expiresIn: 21600 // expires in 6 hours
            });

            return res.send({
                exist_before: true,
                message: 'Successfull account getting',
                patient_id: results[0].id,
                token: token
            });
        }

        // Do not have account
        mc.query('INSERT INTO patient SET ?', { lastname: patient.lastname, surname: patient.surname, email: patient.email }, function (err, rs, flds) {

           if (err) throw err;

            // create a patient token
            var token = jwt.sign({ id: patient.id }, config.JWT.patient, {
              expiresIn: 21600 // expires in 6 hours
            });

           return res.send({
                exist_before: false,
                message: 'New patient account created',
                patient_id: rs.insertId,
                token: token
           });
        });

    });});

// Allow to create a new patient meeting request
app.post('/hiaqueue/patient/meeting/request', function(req, res) {

    let token = req.headers['x-patient-token'];
    let patientId = req.body.patient_id;
    let consultationTypeId = req.body.consultation_type_id;
    let preferences = req.body.preferences;

    if (!patientId) return res.status(400).send({ error: true, message: 'Please provide patient id' });

    if (!consultationTypeId) return res.status(400).send({ error: true, message: 'Please provide consultationTypeId' });

    if (!preferences) return res.status(400).send({ error: true, message: 'Please provide preference' });

     jwt.verify(token, config.JWT.patient, function(err, decoded) {

        if (err) return res.status(500).send({ auth: false, message: 'Failed to authenticate token.' });

        // Create new meeting request
        mc.query('INSERT INTO meetingRequest SET ?', { patientId: patientId, consultationTypeId: consultationTypeId, preferences: preferences}, function (err, rs, flds) {

            if (err) throw err;

            return res.send({
                message: 'New meeting request created',
                error: false
            });

        });

    });

 });

// Allow to log in doctor user
app.post('/hiaqueue/doctor/account/login', function(req, res) {

    let email = req.body.email;
    let password = req.body.password;

    if (!email) return res.status(400).send({ error: true, message: 'Please provide email' });

    if (!password) return res.status(400).send({ error: true, message: 'Please provide password' });

    mc.query('SELECT * FROM doctor where ?', {email: email}, function (error, results, fields) {
        if (error) throw error;

        var result = results[0];

        if (bcrypt.compareSync(password, result.hashpassword)) {

            // create a doctor token
            var doctor_token = jwt.sign({ email: email }, config.JWT.doctor, {
              expiresIn: 21600 // expires in 6 hours
            });

            return res.send({
                error: false,
                message: "Password is good",
                lastname: results[0].lastname,
                doctor_id: results[0].id,
                token: doctor_token
            });
        }

        else
        {
            return res.send({
                error: true,
                message: "Password doesn't match with email"
            });
        }

    });});

// Allow to get all meeting requests on doctor panel
app.get('/hiaqueue/doctor/meeting/request/all', function(req, res) {

    let token = req.headers['x-doctor-token'];

    if (!token) return res.status(401).send({ auth: false, message: 'No Doctor token provided.' });

    mc.query('SELECT meetingRequest.requestId, patient.id, meetingRequest.consultationTypeId, meetingRequest.preferences, meetingRequest.status, meetingRequest.createddate, consultationType.label, consultationType.estimationTime, patient.lastname, patient.surname, patient.email, patient.joineddate FROM meetingRequest, consultationType, patient WHERE meetingRequest.status = 0 AND patient.id = meetingRequest.patientId AND consultationType.id = meetingRequest.consultationTypeId ORDER BY meetingRequest.createddate DESC', function (error, results, fields) {

        if (error) throw error;

        jwt.verify(token, config.JWT.doctor, function(err, decoded) {

            if (err) return res.status(500).send({ auth: false, message: 'Failed to authenticate token.' });
            return res.send(results);

        });

    });});

// Allow to get all planned meeting event on doctor calendar
app.get('/hiaqueue/doctor/meeting/calendar', function(req, res) {

    let token = req.headers['x-doctor-token'];

    if (!token) return res.status(401).send({ auth: false, message: 'No Doctor token provided.' });

    mc.query('SELECT * FROM meetingRequest, meeting, consultationType, patient where meeting.meetingRequestId = meetingRequest.requestId AND status = 1 AND patient.id = meetingRequest.patientId AND consultationType.id = meetingRequest.consultationTypeId ORDER BY meetingRequest.createddate DESC', function (error, results, fields) {

        if (error) throw error;

        jwt.verify(token, config.JWT.doctor, function(err, decoded) {
            if (err) return res.status(500).send({ auth: false, message: 'Failed to authenticate token.' });
            return res.send(results);
        });

    });});

// Allow to accept patient meeting request on doctor panel
app.post('/hiaqueue/doctor/meeting/accept', function(req, res) {

    let token = req.headers['x-doctor-token'];
    let doctorId = req.body.doctor_id;
    let meetingRequestId = req.body.meeting_request_id;
    let date = req.body.meeting_date;

    if (!token) return res.status(401).send({ auth: false, message: 'No Doctor token provided.' });

    if (!doctorId) return res.status(400).send({ error: true, message: 'Please provide doctor id' });

    if (!meetingRequestId) return res.status(400).send({ error: true, message: 'Please provide meetingRequest request id' });

    if (!date) return res.status(400).send({ error: true, message: 'Please provide date' });

       jwt.verify(token, config.JWT.doctor, function(err, decoded) {

	       if (err) return res.status(500).send({ auth: false, message: 'Failed to authenticate token.' });

		    // Updating meeting request status code
	        mc.query('UPDATE meetingRequest SET status = 1 WHERE ?', { requestId: meetingRequestId }, function (err, rs, fls) {})

	        // Create new meeting calendar event
	        mc.query('INSERT INTO meeting SET ?', { doctorId: doctorId, meetingRequestId: meetingRequestId, meetingDate: date }, function (err, rs, flds) {

	            if (err) throw err;

	            return res.send({
	                message: 'Meeting Sucessfully accepted !',
	                error: false
	            });

	        });

	   });});

/** Domaine 2 - Hia Congés **/

// connection configurations 'hiaconges'
const mc2 = mysql.createConnection(config.HIACONGES);
mc2.connect();

// Allow to attempt log in user
app.post('/hiaconges/login', function(req, res) {

    let email = req.body.email;
    let password = req.body.password;

    if (!email) return res.status(400).send({ error: true, message: 'Please provide email' });

    if (!password) return res.status(400).send({ error: true, message: 'Please provide password' });

    mc2.query('SELECT * FROM user where ?', {email: email}, function (error, results, fields) {
        if (error) throw error;

        if (results.length == 0)
        {
            return res.send({
                 error: true,
                 message: "Account not existing"
            });
        }

        var result = results[0];

        if (bcrypt.compareSync(password, result.hashpassword)) {

            // create a doctor token
            var user_token = jwt.sign({ email: email }, config.JWT.doctor, {
              expiresIn: 21600 // expires in 6 hours
            });

            return res.send({
                error: false,
                message: "Password is good",
                uid: results[0].uid,
                lastname: results[0].lastname,
                roleId: results[0].roleId,
                token: user_token
            });
        }

        else
        {
            return res.send({
                error: true,
                message: "Password doesn't match with email"
            });
        }

    });
   });

// Allow to create a new doctor absence request
app.post('/hiaconges/absenceRequest/create', function(req, res) {

    let token = req.headers['x-doctor-token'];
    let uid = req.body.uid;
    let plannedDate = req.body.plannedDate;

    if (!uid) return res.status(400).send({ error: true, message: 'Please provide doctor user id' });

    if (!plannedDate) return res.status(400).send({ error: true, message: 'Please provide planned absence date' });

     jwt.verify(token, config.JWT.doctor, function(err, decoded) {

        if (err) return res.status(500).send({ auth: false, message: 'Failed to authenticate token.' });

        // Create new meeting request
        mc2.query('INSERT INTO absenceRequest SET ?', { uid: uid, plannedDate: plannedDate}, function (err, rs, flds) {

            if (err) throw err;

            return res.send({
                message: 'New absence request created',
                error: false
            });

        });

    });

 });


 // Allow to get all planned absence event on doctor calendar
 app.post('/hiaconges/absence/request/all', function(req, res) {

     let token = req.headers['x-doctor-token'];
     let uid = req.body.uid;

     if (!token) return res.status(401).send({ auth: false, message: 'No Doctor token provided.' });

     if (!uid) return res.status(400).send({ error: true, message: 'Please provide doctor id' });

     mc2.query('SELECT * FROM absenceRequest, role, user where role.id = user.roleId AND user.uid = absenceRequest.uid AND user.uid = ? ORDER BY absenceRequest.createddate DESC', uid, function (error, results, fields) {

         if (error) throw error;

         jwt.verify(token, config.JWT.doctor, function(err, decoded) {
             if (err) return res.status(500).send({ auth: false, message: 'Failed to authenticate token.' });
             return res.send(results);
        });

     });});



 // Allow to get all planned absence event on doctor calendar
 app.get('/hiaconges/absence/request/waiting', function(req, res) {

     let token = req.headers['x-doctor-token'];

     if (!token) return res.status(401).send({ auth: false, message: 'No Doctor token provided.' });

     mc2.query('SELECT * FROM absenceRequest, role, user where role.id = user.roleId AND user.uid = absenceRequest.uid AND status = 0 ORDER BY absenceRequest.createddate DESC', function (error, results, fields) {

         if (error) throw error;

         jwt.verify(token, config.JWT.doctor, function(err, decoded) {
             if (err) return res.status(500).send({ auth: false, message: 'Failed to authenticate token.' });
             return res.send(results);
        });

     });});

 // Allow to confirm absence request
 app.post('/hiaconges/absence/request/accept/', function(req, res) {

     let token = req.headers['x-doctor-token'];
     let absenceId = req.body.absenceId;

     if (!token) return res.status(401).send({ auth: false, message: 'No Doctor token provided.' });

     if (!absenceId) return res.status(400).send({ error: true, message: 'Please provide absenceRequest request id' });

        jwt.verify(token, config.JWT.doctor, function(err, decoded) {

 	      if (err) return res.status(500).send({ auth: false, message: 'Failed to authenticate token.' });

 		    // Updating meeting request status code
 	        mc2.query('UPDATE absenceRequest SET status = 1 WHERE ?', { absenceId: absenceId }, function (err, rs, fls) {
                 if (err) throw err;

                 return res.send({
                      message: 'Absence request Successfully accepted !',
             	      error: false
                 });
 	        })

 	   });
 	  });

 // Allow to cancel absence request