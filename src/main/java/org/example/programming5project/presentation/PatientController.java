package org.example.programming5project.presentation;

import org.example.programming5project.domain.Doctor;
import org.example.programming5project.domain.Gender;
import org.example.programming5project.domain.Patient;
import org.example.programming5project.exceptions.PatientNotFoundException;
import org.example.programming5project.viewmodels.PatientForm;
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

import java.text.SimpleDateFormat;
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
    public String getPatientDetails(@PathVariable String patientId, Model model, HttpSession session) {
        Patient patient = patientService.findPatientById(patientId);
        if (patient == null) {
            logger.error("Patient with ID {} not found", patientId);
            throw new PatientNotFoundException("Patient with ID " + patientId + " not found.");
        }
        List<Doctor> doctors = doctorService.getAllDoctors();
        List<Doctor> assignedDoctors = patientService.getDoctorsForPatient(patientId);

        model.addAttribute("patient", patient);
        model.addAttribute("allDoctors", doctors);
        model.addAttribute("assignedDoctors", assignedDoctors);
        return "patientDetails";
    }


    @RequestMapping("/delete/{patientId}")
    public String deletePatient(@PathVariable String patientId, HttpSession session) {
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
     * @param session
     * @return
     */
    @ExceptionHandler(PatientNotFoundException.class)
    public String handlePatientNotFoundException(PatientNotFoundException ex, Model model, HttpSession session) {
        logger.error("Exception: {}", ex.getMessage());
        model.addAttribute("errorMessage", ex.getMessage());
        return "patientError"; // Replace with your generic error page or create a specific error page for patients
    }

    @PostMapping("/add")
    public String addPatient(@ModelAttribute("patientForm") @Valid PatientForm patientForm,
                             BindingResult bindingResult,
                             Model model,
                             HttpSession session) {
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

        // Assign patient to a doctor
        if (patientForm.getDoctorId() != null) {
            Doctor assignedDoctor = doctorService.findDoctorByLicenseNumber(patientForm.getDoctorId());
            if (assignedDoctor != null) {
                patient.getDoctors().add(assignedDoctor);
            }
        }

        patientService.addPatient(patient);
        logger.info("Patient added successfully: {}", patient);
        return "redirect:/patients";
    }
        @PostMapping("/{patientId}/assign-doctor")
        public String assignDoctorToPatient(@PathVariable String patientId,
                                            @RequestParam String doctorId,HttpSession session) {
            logger.info("Received Patient ID: " + patientId);
            logger.info("Received Doctor ID: " + doctorId);

            if (doctorId == null || doctorId.isEmpty()) {
                throw new IllegalArgumentException("Doctor ID is empty");
            }
            patientService.assignDoctorToPatient(patientId, Integer.parseInt(doctorId));
            return "redirect:/patients/" + patientId;
        }

        //to be able to search for a patient by name or admission date.
        @GetMapping("/search")
        public String searchPatients(@RequestParam(required = false) String name,
                                     @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate admissionDate,
                                     Model model,HttpSession session) {
            List<Patient> patients = patientService.getPatientsByNameOrAdmissionDate(name, admissionDate);

            if (patients.isEmpty()) {
                model.addAttribute("error", "No patients found for the given criteria.");
                logger.warn("No patients found for name: {} and admissionDate: {}", name, admissionDate);
                return "patientError"; // Redirects to an error page (e.g., error.html)
            }

            model.addAttribute("patients", patients);
            return "patients"; // Reuse the patients page to display the results
        }


}
