package org.example.programming5project.presentation;

import org.example.programming5project.domain.Department;
import org.example.programming5project.domain.Doctor;
import org.example.programming5project.domain.Gender;
import org.example.programming5project.domain.Patient;
import org.example.programming5project.exceptions.DoctorNotFoundException;
import org.example.programming5project.viewmodels.DoctorForm;
import org.example.programming5project.service.DoctorService;
import org.example.programming5project.service.PatientService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.SimpleDateFormat;
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
        logVisit(session,"Fetching all doctors...");
        model.addAttribute("doctors", doctorService.getAllDoctors());
        return "doctors";
    }

    @GetMapping("/add")
    public String showAddDoctorForm(Model model, HttpSession session) {
        logger.info("Processing doctor's form...");
        logVisit(session,"Visited Doctor's Form");
        model.addAttribute("doctorForm", new DoctorForm());
        return "adddoctor"; //
    }

    @GetMapping("/{doctorId}")
    public String getDoctorDetails(@PathVariable int doctorId, Model model, HttpSession session) {
        Doctor doctor = doctorService.findDoctorByLicenseNumber(doctorId);
        if (doctor == null) {
            logger.error("Doctor with ID {} not found", doctorId);
            logVisit(session,"Doctor with ID " + doctorId + " not found");
            throw new DoctorNotFoundException("Doctor with ID " + doctorId + " not found.");
        }

        List<Patient> patients = patientService.getPatientsForDoctor(doctorId);
        List<Patient> allPatients = patientService.getAllPatients();
        logVisit(session, "Getting details for Doctor with ID: " + doctorId);

        model.addAttribute("doctor", doctor);
        model.addAttribute("assignedPatients", patients);
        model.addAttribute("allPatients", allPatients);
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
        logVisit(session, "Error: " + ex.getMessage());
        return "error"; // Replace with the name of your error view
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
        logVisit(session,"Added Doctor with ID: " +doctorForm.toString());
        return "redirect:/doctors";
    }

    @PostMapping("/delete/{licenseNumber}")
    public String deleteDoctor(@PathVariable int licenseNumber, RedirectAttributes redirectAttributes,HttpSession session) {
        try {
            logger.info("Deleting doctor: "+licenseNumber + "....");
            doctorService.removeDoctor(licenseNumber);
            redirectAttributes.addFlashAttribute("successMessage", "Doctor deleted successfully!");
            logger.info("Successfully deleted doctor: "+licenseNumber + "!");
            logVisit(session,"Doctor with ID: "+ licenseNumber + " deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting doctor: " + e.getMessage());
        }
        return "redirect:/doctors";
    }

    @PostMapping("/{doctorId}/assign-patient")
    public String assignPatientToDoctor(@PathVariable int doctorId, @RequestParam String patientId,HttpSession session) {
        doctorService.assignPatientToDoctor(doctorId, patientId);
        logVisit(session,"Doctor assignment session...");
        logVisit(session,"Assigned patient to Doctor with ID: "+ doctorId);
        return "redirect:/doctors/" + doctorId;
    }

    @GetMapping("/search")
    public String searchDoctorByLicenseNumber(@RequestParam("licenseNumber") int licenseNumber, Model model, HttpSession session) {
        // Fetch the doctor
        Doctor doctor = doctorService.findDoctorByLicenseNumber(licenseNumber);
        if (doctor == null) {
            model.addAttribute("errorMessage", "No doctor found with license number " + licenseNumber);
            logVisit(session, "Failed to find Doctor with License Number: " + licenseNumber);
            return "error"; // Or another error page
        }
        List<Patient> patients = patientService.getPatientsForDoctor(licenseNumber);
        List<Patient> allPatients = patientService.getAllPatients();

        logVisit(session, "Searched Doctor with License Number: " + licenseNumber);
        model.addAttribute("doctor", doctor);
        model.addAttribute("assignedPatients", patients);
        model.addAttribute("allPatients", allPatients);

        return "doctorDetails";
    }
    /**
     * method to log the visit
     * @param session
     * @param pageName
     */
    public void logVisit(HttpSession session, String pageName) {
        List<Map<String, String>> visitHistory = (List<Map<String, String>>) session.getAttribute("visitHistory");
        if (visitHistory == null) {
            visitHistory = new ArrayList<>();
            session.setAttribute("visitHistory", visitHistory);
        }
        Map<String, String> visitEntry = new HashMap<>();
        visitEntry.put("page", pageName);
        visitEntry.put("timestamp", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        visitHistory.add(visitEntry);
    }
}
