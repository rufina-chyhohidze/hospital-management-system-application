package org.example.programming5project.presentation;

import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.text.SimpleDateFormat;
import java.util.*;

@Controller

public class HomeController {
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
    @GetMapping("/")
    public String showHomePage(HttpSession session) {
        logger.info("Showing home page");
        logger.info("Current user: " + session.getAttribute("user"));
        logVisit(session,"At the home page at the moment....");
        return "home";
    }
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
//
}
