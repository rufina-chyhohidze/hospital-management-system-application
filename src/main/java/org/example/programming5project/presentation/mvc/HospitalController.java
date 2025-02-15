package org.example.programming5project.presentation.mvc;

import jakarta.validation.Valid;
import org.example.programming5project.presentation.mvc.viewmodels.HospitalForm;
import org.example.programming5project.service.HospitalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/hospitals")
public class HospitalController {
    private final HospitalService hospitalService;
    private final Logger logger = LoggerFactory.getLogger(HospitalController.class);

    public HospitalController(HospitalService hospitalService) {
        this.hospitalService = hospitalService;
    }
    @GetMapping
    public String listHospitals(Model model) {
        logger.debug("Listing all hospitals...");
        model.addAttribute("hospitals", hospitalService.getAllHospitals());
        return "hospitals";
    }
    @GetMapping("/add")
    public String showAddHospitalForm(Model model) {
        logger.debug("Showing hospital form...");
        model.addAttribute("hospitalForm", new HospitalForm());
        return "addhospital";
    }

    @PostMapping("/add")
    public String addHospital(@ModelAttribute @Valid HospitalForm hospitalForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "addhospital";
        }
        hospitalService.addHospital(hospitalForm);
        return "redirect:/hospitals";
    }
}
