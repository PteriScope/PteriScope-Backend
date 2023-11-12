package com.pteriscope.pteriscopebackend.patient.api;

import com.pteriscope.pteriscopebackend.patient.domain.model.entity.Patient;
import com.pteriscope.pteriscopebackend.patient.domain.serivces.PatientService;
import com.pteriscope.pteriscopebackend.patient.dto.PatientResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class PatientController {

    @Autowired
    private PatientService patientService;

    @PostMapping("/specialists/createPatient/{specialistId}")
    public ResponseEntity<PatientResponse> createPatient(@RequestBody Patient patient, @PathVariable Long specialistId) {
        return ResponseEntity.ok(patientService.createPatient(specialistId, patient));
    }

    @GetMapping("/patients/get/{patientId}")
    public ResponseEntity<PatientResponse> getPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(patientService.getPatient(patientId));
    }

    @GetMapping("/specialists/{specialistId}/patients")
    public ResponseEntity<List<PatientResponse>> getPatientFromSpecialist(@PathVariable Long specialistId) {
        return ResponseEntity.ok(patientService.getPatientFromSpecialist(specialistId));
    }

    @PutMapping("/patients/update/{patientId}")
    public ResponseEntity<PatientResponse> updatePatient(@PathVariable Long patientId, @RequestBody Patient updatedPatient) {
        return ResponseEntity.ok(patientService.updatePatient(patientId, updatedPatient));
    }

    @DeleteMapping("/patients/delete/{patientId}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long patientId) {
        patientService.deletePatient(patientId);
        return ResponseEntity.ok().build();
    }
}
