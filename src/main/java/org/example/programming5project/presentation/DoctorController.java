package org.example.programming5project.presentation;

import org.example.programming5project.domain.*;
import org.example.programming5project.exceptions.DoctorNotFoundException;
import org.example.programming5project.viewmodels.DoctorForm;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.*;


@Controller
@RequestMapping("/doctors")
public class DoctorController {
    private static final Logger logger = LoggerFactory.getLogger(DoctorController.class);
    private final DoctorService doctorService;
    private final PatientService patientService;
    /**
     *
     * @param doctorService - qualifier can be removed, to use doctorServiceImpl(h2),"doctorJpaDataServiceImpl"(jpaData),"patientJpaDataServiceImpl"(jpaData)
     * @param patientService - now qualifier is used for postgres implementation
     */
   // @Autowired
    public DoctorController( DoctorService doctorService,PatientService patientService) {
        this.doctorService = doctorService;
        this.patientService = patientService;
    }
    @GetMapping
    public String getAllDoctors(Model model, HttpSession session) {
        logger.info("Fetching all doctors...");
        model.addAttribute("doctors", doctorService.getAllDoctors());
        return "doctors";
    }

    @GetMapping("/add")
    public String showAddDoctorForm(Model model, HttpSession session) {
        logger.info("Processing doctor's form...");
        model.addAttribute("doctorForm", new DoctorForm());
        return "adddoctor"; //
    }

    @GetMapping("/{doctorId}")
    public String getDoctorDetails(@PathVariable int doctorId, Model model) {
        Doctor doctor = doctorService.findDoctorByLicenseNumber(doctorId);
        if (doctor == null) {
            logger.error("Doctor with ID {} not found", doctorId);
            throw new DoctorNotFoundException("Doctor with ID " + doctorId + " not found.");
        }

        List<Patient> patients = patientService.getPatientsForDoctor(doctorId);//getnotassignedpatients
        List<Patient> allPatients = patientService.getAllPatients();

        // medical records explicitly
        List<MedicalRecord> medicalRecords = doctor.getMedicalRecords();

        model.addAttribute("doctor", doctor);
        model.addAttribute("assignedPatients", patients);
        model.addAttribute("allPatients", allPatients);
        model.addAttribute("medicalRecords", medicalRecords);
        ///i need not assigned patients ,and remove assign patients, and do i need all patinets?
        // Include medical records

        return "doctorDetails";
    }
    /**
     * To handle doctorNotFoundException
     * @param ex
     * @param model
     * @param session
     * @return
     */
    @ExceptionHandler(DoctorNotFoundException.class)
    public String handleDoctorNotFoundException(DoctorNotFoundException ex, Model model, HttpSession session) {
        logger.error("Exception: {}", ex.getMessage());
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }


    @PostMapping("/add")
    public String addDoctor(@ModelAttribute("doctorForm")@Valid DoctorForm doctorForm, BindingResult bindingResult, Model model,HttpSession session) {
        if(bindingResult.hasErrors()) {
            logger.warn("Validation errors: {}", bindingResult.getAllErrors());
            return "adddoctor"; //it returns to the form if validation fails
        }
        // Convert PatientForm to Patient entity and add to service
        Doctor doctor = new Doctor();
        doctor.setFirstName(doctorForm.getFirstName());
        doctor.setLastName(doctorForm.getLastName());
        doctor.setLicenseNumber(doctorForm.getLicenseNumber());
        doctor.setSalary(doctorForm.getSalary());
        doctor.setDepartment(Department.valueOf(doctorForm.getDepartment().toUpperCase()));
        doctor.setHireDate(doctorForm.getHireDate());
        doctor.setGender(Gender.valueOf(doctorForm.getGender().toUpperCase()));;

        doctorService.addDoctor(doctor);
        logger.info("Successfully added a new doctor: {}", doctorForm.toString());
        return "redirect:/doctors";
    }

    @PostMapping("/delete/{licenseNumber}")
    public String deleteDoctor(@PathVariable int licenseNumber, RedirectAttributes redirectAttributes,HttpSession session) {
        try {
            logger.info("Deleting doctor: "+licenseNumber + "....");
            doctorService.removeDoctor(licenseNumber);
            redirectAttributes.addFlashAttribute("successMessage", "Doctor deleted successfully!");
            logger.info("Successfully deleted doctor: "+licenseNumber + "!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting doctor: " + e.getMessage());
        }
        return "redirect:/doctors";
    }

    @PostMapping("/{doctorId}/assign-patient")
    public String assignPatientToDoctor(@PathVariable int doctorId, @RequestParam String patientId) {
        doctorService.assignPatientToDoctor(doctorId, patientId);
        return "redirect:/doctors/" + doctorId;
    }

    @GetMapping("/search")
    public String searchDoctorByLicenseNumber(@RequestParam("licenseNumber") int licenseNumber, Model model) {
        // Fetch the doctor
        Doctor doctor = doctorService.findDoctorByLicenseNumber(licenseNumber);
        if (doctor == null) {
            model.addAttribute("errorMessage", "No doctor found with license number " + licenseNumber);
            return "error";
        }
        List<Patient> patients = patientService.getPatientsForDoctor(licenseNumber);
        List<Patient> allPatients = patientService.getAllPatients();

        model.addAttribute("doctor", doctor);
        model.addAttribute("assignedPatients", patients);
        model.addAttribute("allPatients", allPatients);

        return "doctorDetails";
    }

    @PostMapping("/{doctorId}/add-medical-record")
    public String addMedicalRecord(@PathVariable int doctorId,
                                   @RequestParam String patientId,
                                   @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate treatmentDate,
                                   @RequestParam String diagnosis,
                                   @RequestParam String treatment) {
        logger.info("Adding medical record for Patient: {}, Doctor: {}", patientId, doctorId);

        doctorService.addMedicalRecord(doctorId, patientId, treatmentDate, diagnosis, treatment);

        return "redirect:/doctors/" + doctorId;
    }

}
