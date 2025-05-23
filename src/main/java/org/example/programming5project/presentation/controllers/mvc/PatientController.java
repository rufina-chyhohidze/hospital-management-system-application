package org.example.programming5project.presentation.controllers.mvc;

import org.example.programming5project.domain.Doctor;
import org.example.programming5project.domain.Gender;
import org.example.programming5project.domain.MedicalRecord;
import org.example.programming5project.domain.Patient;
import org.example.programming5project.exceptions.PatientNotFoundException;
import org.example.programming5project.presentation.controllers.mvc.viewmodels.PatientForm;
import org.example.programming5project.service.DoctorService;
import org.example.programming5project.service.PatientService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.*;

@Controller
@RequestMapping("/patients")
public class PatientController {
    private static final Logger logger = LoggerFactory.getLogger(PatientController.class);
    private final PatientService patientService;
    private final DoctorService doctorService;

    public PatientController( PatientService patientService,DoctorService doctorService) {
        this.patientService = patientService;
        this.doctorService = doctorService;
    }

    @GetMapping
    public String getAllPatients(Model model, HttpSession session) {
        logger.info("Fetching all patients...");
        List<Patient> patients = patientService.getAllPatients();
        model.addAttribute("patients", patients);
        return "patients"; // returns the view called patients.html
    }

    @GetMapping ("/add")
    public String addPatientForm(Model model,HttpSession session) {
        model.addAttribute("patientForm", new PatientForm());
        logger.info("Processing patient's form...");
        return "addpatient"; // returns the form to add a patient
    }

    @GetMapping("/{patientId}")
    public String getPatientDetails(@PathVariable String patientId, Model model) {
        // patient with medical records
        Patient patient = patientService.findPatientWithMedicalRecords(patientId);
        if (patient == null) {
            logger.error("Patient with ID {} not found", patientId);
            throw new PatientNotFoundException("Patient with ID " + patientId + " not found.");
        }

        List<Doctor> doctors = doctorService.getAllDoctors();
        List<Doctor> assignedDoctors = patientService.getDoctorsForPatient(patientId);
        List<MedicalRecord> medicalRecords = patient.getMedicalRecords(); // Fetch medical records

        model.addAttribute("patient", patient);
        model.addAttribute("allDoctors", doctors);
        model.addAttribute("assignedDoctors", assignedDoctors);
        model.addAttribute("medicalRecords", medicalRecords); // Pass medical records

        return "patientDetails";
    }


    @RequestMapping("/delete/{patientId}")
    public String deletePatient(@PathVariable String patientId) {
        Patient patient = patientService.findPatientById(patientId);
        if (patient == null) {
            logger.error("Patient with ID {} not found, cannot delete.", patientId);
            throw new PatientNotFoundException("Patient with ID " + patientId + " not found.");
        }
        logger.info("Deleting patient: " + patientId + "...");
        patientService.removePatient(patientId);
        logger.info("Patient deleted: " + patientId + "!");
        return "redirect:/patients";
    }

    /**
     * handles patientNotFoundException
     * @param ex
     * @param model
     * @return
     */
    @ExceptionHandler(PatientNotFoundException.class)
    public String handlePatientNotFoundException(PatientNotFoundException ex, Model model) {
        logger.error("Exception: {}", ex.getMessage());
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }

    @PostMapping("/add")
    public String addPatient(@ModelAttribute("patientForm") @Valid PatientForm patientForm,
                             BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasErrors()) {
            logger.warn("Validation errors occurred: {}", bindingResult.getAllErrors());
            // Fetch doctors again in case of form errors
            List<Doctor> doctors = doctorService.getAllDoctors();
            model.addAttribute("doctors", doctors);
            return "addpatient";
        }

        // Convert PatientForm to Patient entity
        Patient patient = new Patient();
        patient.setPatientId(patientForm.getPatientId());
        patient.setFirstName(patientForm.getFirstName());
        patient.setLastName(patientForm.getLastName());
        patient.setAge(patientForm.getAge());
        patient.setGender(Gender.valueOf(patientForm.getGender().toUpperCase()));
        patient.setAdmissionDate(patientForm.getAdmissionDate());
        patient.setBillingAmount(patientForm.getBillingAmount());
        patientService.addPatient(patient);
        logger.info("Patient added successfully: {}", patient);
        return "redirect:/patients";
    }
        //to be able to search for a patient by name or admission date.
        @GetMapping("/search")
        public String searchPatients(
                @RequestParam(required = false) String name,
                @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate admissionDate,
                Model model) {

            logger.info("Searching for patients - Name: {}, Admission Date: {}", name, admissionDate);

            if (admissionDate != null) {
                logger.info("Admission Date Type: {}", admissionDate.getClass().getName());
            } else {
                logger.info("Admission Date is NULL");
            }
            List<Patient> patients = patientService.getPatientsByNameOrAdmissionDate(name.isEmpty()?"name": name, admissionDate);

            if (patients.isEmpty()) {
                model.addAttribute("error", "No patients found for the given criteria.");
                logger.warn("No patients found for Name: {} and Admission Date: {}", name, admissionDate);
            }
            model.addAttribute("patients", patients);
            return "patients";
        }


}
