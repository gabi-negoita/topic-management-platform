package com.mtapo.app.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mtapo.app.entity.EventLog;
import com.mtapo.app.entity.Notification;
import com.mtapo.app.entity.StudentTopic;
import com.mtapo.app.entity.User;
import com.mtapo.app.service.EventLogService;
import com.mtapo.app.service.NotificationService;
import com.mtapo.app.service.StudentTopicService;
import com.mtapo.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Controller
public class TopicRequestController {

    private final StudentTopicService studentTopicService;
    private final UserService userService;
    private final EventLogService eventLogService;
    private final NotificationService notificationService;

    @Autowired
    public TopicRequestController(StudentTopicService studentTopicService,
                                  UserService userService,
                                  EventLogService eventLogService,
                                  NotificationService notificationService) {
        this.studentTopicService = studentTopicService;
        this.userService = userService;
        this.eventLogService = eventLogService;
        this.notificationService = notificationService;
    }

    @GetMapping("/topic-requests")
    public String topicRequests(HttpServletRequest request,
                                Model model) {
        int sessionUserId = ((User) request.getSession().getAttribute("sessionUser")).getId();
        Optional<User> currentUser = this.userService.findById(sessionUserId);

        Pageable firstBatch = PageRequest.of(0, 10, Sort.by(Sort.Order.desc("id")));
        List<StudentTopic> listOfRequests =
                this.studentTopicService.findAllRequestsForTopicOfUser(currentUser.get(), null, firstBatch);
        model.addAttribute("requestsBatch", listOfRequests);

        Integer numberOfElements =
                this.studentTopicService
                        .findAllRequestsForTopicOfUser(currentUser.get(), null, Pageable.unpaged())
                        .size();
        model.addAttribute("numberOfElements", numberOfElements);

        return "topic-requests";
    }

    @ResponseBody
    @PutMapping(value = "/topic-requests/requests")
    public ResponseEntity<List<StudentTopic>> getUpdatedRequests(
            HttpServletRequest request,
            @RequestBody Object object) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(object);
        JsonNode rootNode = mapper.readTree(json);

        JsonNode criteriaNode = rootNode.get("0");
        JsonNode orderNode = rootNode.get("1");
        JsonNode searchNode = rootNode.get("2");
        JsonNode pageNode = rootNode.get("3");

        Integer idUser = ((User) request.getSession().getAttribute("sessionUser")).getId();
        Optional<User> currentUser = userService.findById(idUser);
        if (currentUser.isPresent()) {
            String criteria = !criteriaNode.isNull() ? criteriaNode.asText() : null;
            String order = !orderNode.isNull() ? orderNode.asText() : null;
            String search = !searchNode.isNull() ? searchNode.asText() : null;
            int page = !pageNode.isNull() ? pageNode.asInt() : 0;
            Pageable batch;
            if (criteria == null) {
                batch = PageRequest.of(page, 10);
            } else {
                criteria = "id".equals(criteria) ? criteria : "user." + criteria;
                batch = PageRequest.of(page, 10, Sort.by(("asc".equals(order))
                        ? Sort.Direction.ASC
                        : Sort.Direction.DESC, criteria));
            }
            List<StudentTopic> listOfRequests = !"".equals(search)
                    ? studentTopicService.findAllRequestsForTopicOfUser(currentUser.get(), search, batch)
                    : studentTopicService.findAllRequestsForTopicOfUser(currentUser.get(), null, batch);

            return new ResponseEntity<>(listOfRequests, HttpStatus.OK);
        }
        return null;
    }

    @ResponseBody
    @PutMapping(value = "/topic-requests/approve-request/{requestId}")
    public ResponseEntity<StudentTopic> approveRequest(@PathVariable("requestId") Integer requestId) {
        Optional<StudentTopic> topicRequest = studentTopicService.findById(requestId);
        ResponseEntity<StudentTopic> response = null;
        EventLog el;
        if (topicRequest.isPresent()) {
            topicRequest.get().setStatus(StudentTopic.STATUS_APPROVED);
            studentTopicService.saveOrUpdate(topicRequest.get());
            List<StudentTopic> stList =
                    studentTopicService.findAllByUserId(topicRequest.get().getUser().getId());
            Notification notificationApproved =
                    new Notification(
                            null,
                            StudentTopic.STATUS_APPROVED,
                            Notification.TYPE_TEACHER_RESPONSE,
                            new Timestamp(System.currentTimeMillis()),
                            false,
                            false,
                            topicRequest.get(),
                            null);
            notificationService.saveOrUpdate(notificationApproved);
            stList.forEach(
                    st -> {
                        if (StudentTopic.STATUS_WAITING.equals(st.getStatus())) {
                            st.setStatus(StudentTopic.STATUS_CANCELED);
                            st.setReason(StudentTopic.REASON_ALREADY_APPROVED_TOPIC);
                            Notification notificationCancelled =
                                    new Notification(
                                            null,
                                            StudentTopic.STATUS_CANCELED,
                                            Notification.TYPE_TEACHER_RESPONSE,
                                            new Timestamp(System.currentTimeMillis()),
                                            false,
                                            false,
                                            st,
                                            null);
                            notificationService.saveOrUpdate(notificationCancelled);
                        }
                    });
            studentTopicService.saveAll(stList);
            response = new ResponseEntity<>(topicRequest.get(), HttpStatus.OK);
            el = new EventLog(null,
                    EventLog.OPERATION_UPDATE,
                    EventLog.STATUS_SUCCESS,
                    Notification.REQUEST_APPROVED_SUCCESS,
                    new Timestamp(System.currentTimeMillis()),
                    topicRequest.get().getUser());
        } else {
            el = new EventLog(null,
                    EventLog.OPERATION_UPDATE,
                    EventLog.STATUS_FAILED,
                    Notification.REQUEST_APPROVED_FAILURE,
                    new Timestamp(System.currentTimeMillis()),
                    null);
        }
        eventLogService.saveOrUpdate(el);

        return response;
    }

    @ResponseBody
    @PutMapping(value = "/topic-requests/deny-request/{requestId}")
    public ResponseEntity<StudentTopic> denyRequest(@PathVariable("requestId") Integer requestId) {
        Optional<StudentTopic> topicRequest = studentTopicService.findById(requestId);
        ResponseEntity<StudentTopic> response = null;
        EventLog el;
        if (topicRequest.isPresent()) {
            topicRequest.get().setStatus(StudentTopic.STATUS_DECLINED);
            topicRequest.get().setReason(StudentTopic.REASON_REQUEST_DECLINED);
            studentTopicService.saveOrUpdate(topicRequest.get());
            response = new ResponseEntity<>(topicRequest.get(), HttpStatus.OK);
            el = new EventLog(null,
                    EventLog.OPERATION_UPDATE,
                    EventLog.STATUS_SUCCESS,
                    Notification.REQUEST_DECLINED_SUCCESS,
                    new Timestamp(System.currentTimeMillis()),
                    topicRequest.get().getUser());
            Notification notification = new Notification(null,
                    StudentTopic.STATUS_DECLINED,
                    Notification.TYPE_TEACHER_RESPONSE,
                    new Timestamp(System.currentTimeMillis()),
                    false,
                    false,
                    topicRequest.get(),
                    null);
            notificationService.saveOrUpdate(notification);
        } else {
            el = new EventLog(null,
                    EventLog.OPERATION_UPDATE,
                    EventLog.STATUS_FAILED,
                    Notification.REQUEST_DECLINED_FAILURE,
                    new Timestamp(System.currentTimeMillis()),
                    null);
        }
        eventLogService.saveOrUpdate(el);

        return response;
    }
}
