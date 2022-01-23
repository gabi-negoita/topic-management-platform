package com.mtapo.app.controller;

import com.mtapo.app.model.FormResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    @RequestMapping("/error")
    public String error(HttpServletRequest request,
                        Model model) {

        if (request.getSession().getAttribute("sessionUser") == null) {
            return "redirect:/login";
        }

        Integer statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        FormResponse formResponse = new FormResponse();
        formResponse.setStatus(statusCode.toString());
        switch (statusCode) {
            case 403: {
                formResponse.setMessage("Forbidden");
                formResponse.setDescription("Seems like you don't have enough privileges to access this resource");
                break;
            }
            case 404: {
                formResponse.setMessage("Resource not found");
                formResponse.setDescription("The resource you are looking for doesn't exist or is not available anymore");
                break;
            }
            default: {
                formResponse.setMessage("Oops!");
                formResponse.setDescription("Something went wrong while processing your request");
            }
        }

        model.addAttribute("formResponse", formResponse);

        return "error";
    }
}
