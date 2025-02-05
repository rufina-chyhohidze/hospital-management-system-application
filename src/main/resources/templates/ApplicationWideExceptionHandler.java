package templates;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * class for handling all exceptions across the application
 */
@ControllerAdvice
public class ApplicationWideExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationWideExceptionHandler.class);

    @ExceptionHandler(DataAccessException.class)
    public String handleDatabaseException(DataAccessException ex, Model model) {
        logger.error("Database exception occurred: {}", ex.getMessage(), ex);
        model.addAttribute("errorMessage", "A database error occurred. Please try again later.");
        return "databaseErrorPage";
    }

    @ExceptionHandler(Exception.class)
    public String handleGenericException(Exception ex, Model model) {
        logger.error("An unexpected exception occurred: {}", ex.getMessage(), ex);
        model.addAttribute("errorMessage", "An unexpected error occurred. Please contact support.");
        return "applicationErrorPage";
    }

    @ExceptionHandler(PatientNotFoundException.class)
    public String handlePatientNotFoundException(PatientNotFoundException ex, Model model) {
        logger.error("Patient error: {}", ex.getMessage());
        model.addAttribute("errorMessage", ex.getMessage());
        return "error"; // Use a generic or specific error page
    }
}
