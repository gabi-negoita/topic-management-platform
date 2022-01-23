package com.mtapo.app.controller;

import com.mtapo.app.entity.StudentTopic;
import com.mtapo.app.entity.TopicComment;
import com.mtapo.app.entity.User;
import com.mtapo.app.model.UserActivity;
import com.mtapo.app.service.StudentTopicService;
import com.mtapo.app.service.TopicCommentService;
import com.mtapo.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class ActivityController {
    private final StudentTopicService studentTopicService;
    private final TopicCommentService topicCommentService;
    private final UserService userService;

    @Autowired
    public ActivityController(StudentTopicService studentTopicService,
                              UserService userService,
                              TopicCommentService topicCommentService) {
        this.studentTopicService = studentTopicService;
        this.userService = userService;
        this.topicCommentService = topicCommentService;
    }

    @RequestMapping("/activity")
    public String topics(HttpServletRequest request,
                         Model model) {
        User currentUser = (User) request.getSession().getAttribute("sessionUser");
        model.addAttribute("currentUser", currentUser);

        int sessionUserId = currentUser.getId();

        List<StudentTopic> studentTopics = studentTopicService.findAllByUserId(sessionUserId);
        model.addAttribute("studentTopics", studentTopics);

        Optional<StudentTopic> approvedTopic = studentTopicService.findApprovedByUserId(sessionUserId);
        approvedTopic.ifPresent(value -> model.addAttribute("approvedStudentTopic", value));

        List<TopicComment> comments = topicCommentService.findAllByUserId(sessionUserId);
        model.addAttribute("comments", comments);

        return "activity";
    }

    @RequestMapping("/activity/{id}")
    public String topics(@PathVariable("id") Integer id,
                         Model model) {
        Optional<User> user = userService.findById(id);

        if (user.isPresent()) {

            model.addAttribute("currentUser", user.get());

            List<StudentTopic> allTopics;
            allTopics = studentTopicService.findAllByUserId(id);

            Optional<StudentTopic> approvedTopic = studentTopicService.findApprovedByUserId(id);

            List<TopicComment> comments = topicCommentService.findAllByUserId(id);

            model.addAttribute("topics", allTopics);
            model.addAttribute("approvedStudentTopic", approvedTopic.orElse(null));

            model.addAttribute("comments", comments);
            model.addAttribute("user", user.get().getFirstName());

        } else {
            return "redirect:/activity";
        }

        return "activity";
    }

    @ResponseBody
    @PostMapping("/activity/get-activity/{userId}")
    public ResponseEntity<List<UserActivity>> getUserActivity(@PathVariable("userId") Integer userId,
                                                              HttpServletRequest request) {
        User currentUser = (User) request.getSession().getAttribute("sessionUser");
        int sessionUserId = currentUser.getId();

        if (userId != null && sessionUserId != userId) {
            sessionUserId = userId;
        }

        List<String> dates = userService.getActivityDatesByUserId(sessionUserId);
        List<Integer> actions = userService.getActivityActionsByUserId(sessionUserId);

        List<UserActivity> userActivities = new ArrayList<>();
        for (int i = 0; i < dates.size(); i++) {
            userActivities.add(new UserActivity(dates.get(i), actions.get(i)));
        }

        return new ResponseEntity<>(userActivities, HttpStatus.OK);
    }
}
