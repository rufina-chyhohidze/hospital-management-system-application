package org.example.programming5project.presentation.controllers.mvc;

import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller

public class HomeController {
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
    @GetMapping("/")
    public String showHomePage(HttpSession session) {
        logger.info("Showing home page");
        logger.info("Current user: " + session.getAttribute("user"));
        return "home";
    }
}
