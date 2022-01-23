package com.mtapo.app.controller;

import com.mtapo.app.entity.Topic;
import com.mtapo.app.entity.User;
import com.mtapo.app.service.TopicService;
import com.mtapo.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ControllerAdvice
public class GeneralController {

    private final UserService userService;
    private final TopicService topicService;

    @Autowired
    public GeneralController(UserService userService,
                             TopicService topicService) {
        this.userService = userService;
        this.topicService = topicService;
    }

    // ADDS VARIABLES TO EACH AND EVERY MODEL
    @ModelAttribute
    public void init(HttpServletRequest request,
                     Model model) {
        if (request.getSession().getAttribute("sessionUser") != null) {
            Integer idUser = ((User) request.getSession().getAttribute("sessionUser")).getId();
            Optional<User> currentUser = userService.findById(idUser);

            if (currentUser.isPresent()) {
                Optional<Topic> chosenAndApprovedTopic = topicService.findByUser(currentUser.get());
                model.addAttribute("navbarTopic", chosenAndApprovedTopic.orElse(null));

                List<String> userRoles = new ArrayList<String>();
                currentUser.get().getRoles().forEach(x -> userRoles.add(x.getName()));
                model.addAttribute("userRoles", userRoles);
            }

            String uri = request.getRequestURI();
            model.addAttribute("uri", uri.substring(1));
        }
    }
}
