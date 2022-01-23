package com.mtapo.app.controller;

import com.mtapo.app.entity.*;
import com.mtapo.app.service.CategoryService;
import com.mtapo.app.service.NotificationService;
import com.mtapo.app.service.TopicService;
import com.mtapo.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Controller
public class AjaxController {

    private final UserService userService;
    private final TopicService topicService;
    private final CategoryService categoryService;
    private final NotificationService notificationService;

    @Autowired
    public AjaxController(UserService userService,
                          TopicService topicService,
                          CategoryService categoryService,
                          NotificationService notificationService) {
        this.userService = userService;
        this.topicService = topicService;
        this.categoryService = categoryService;
        this.notificationService = notificationService;
    }

    @ResponseBody
    @GetMapping(value = "/navbar/searchbar-topics")
    public ResponseEntity<List<Topic>> getSuggestedTopics(HttpServletRequest request) {

        Integer idUser = ((User) request.getSession().getAttribute("sessionUser")).getId();
        Optional<User> currentUser = userService.findById(idUser);
        if (currentUser.isPresent()) {
            Optional<Category> currentCategory = categoryService.findByUser(currentUser.get());
            if (currentCategory.isPresent()) {
                List<Topic> topicsOfCategory = this.topicService.findAllByCategory(currentCategory.get());
                return new ResponseEntity<>(topicsOfCategory, HttpStatus.OK);
            } else {
                List<Topic> topicsOfCategory = this.topicService.findAllByCategory(null);
                return new ResponseEntity<>(topicsOfCategory, HttpStatus.OK);
            }
        }
        return null;
    }

    @ResponseBody
    @GetMapping(value = "/navbar/notifications/{idUser}")
    public ResponseEntity<List<Notification>> getNotifications(HttpServletRequest request,
                                                               @PathVariable("idUser") Integer idUser) {
        Integer idSessionUser = ((User) request.getSession().getAttribute("sessionUser")).getId();
        if (!idUser.equals(idSessionUser)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        Optional<User> user = userService.findById(idUser);
        ResponseEntity<List<Notification>> response = null;
        if (user.isPresent()) {
            List<Notification> news = new ArrayList<>();
            List<Notification> comments = new ArrayList<>();
            List<Notification> notifications = new ArrayList<>();

            List<String> userRoles = new ArrayList<String>();
            user.get().getRoles().forEach(x -> userRoles.add(x.getName()));

            if (userRoles.contains(Role.STUDENT)) {
                news = notificationService.findRequestsFromTeacherToStudent(user.get());
                comments = notificationService.findCommentsForTopicOfStudent(user.get());
            }
            if (userRoles.contains(Role.TEACHER)) {
                news.addAll(notificationService.findRequestsFromStudentToTeacher(user.get()));
                news.addAll(notificationService.findApplicationCancelFromStudentToTeacher(user.get()));
                comments.addAll(notificationService.findCommentsForTopicsOfTeacher(user.get()));
            }
            notifications.addAll(news);
            notifications.addAll(comments);
            notifications.sort(Comparator.comparing(Notification::getDate).reversed());
            response = new ResponseEntity<>(notifications, HttpStatus.OK);
        }
        return response;
    }

    @ResponseBody
    @PutMapping(value = "/navbar/read/{historyId}")
    public ResponseEntity<Notification> setHistoryRead(@PathVariable("historyId") Integer historyId) {
        ResponseEntity<Notification> response = null;
        Optional<Notification> notification = notificationService.findById(historyId);
        if (notification.isPresent()) {
            notification.get().setIsRead(true);
            notification.get().setIsSeen(true);
            notificationService.saveOrUpdate(notification.get());
            response = new ResponseEntity<>(notification.get(), HttpStatus.OK);
        }
        return response;
    }

    @ResponseBody
    @PutMapping(value = "/navbar/seen/{historyId}")
    public ResponseEntity<Notification> setHistorySeen(@PathVariable("historyId") Integer historyId) {
        ResponseEntity<Notification> response = null;
        Optional<Notification> notification = notificationService.findById(historyId);
        if (notification.isPresent()) {
            notification.get().setIsSeen(true);
            notificationService.saveOrUpdate(notification.get());
            response = new ResponseEntity<>(notification.get(), HttpStatus.OK);
        }
        return response;
    }
}
