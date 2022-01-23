package com.mtapo.app.controller;

import com.mtapo.app.entity.*;
import com.mtapo.app.model.FormResponse;
import com.mtapo.app.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Controller
public class TopicController {

    private final TopicService topicService;
    private final TopicCommentService topicCommentService;
    private final StudentTopicService studentTopicService;
    private final NotificationService notificationService;
    private final EventLogService eventlogService;

    @Autowired
    public TopicController(EventLogService eventlogService,
                           TopicService topicService,
                           TopicCommentService topicCommentService,
                           StudentTopicService studentTopicService,
                           NotificationService notificationService) {
        this.eventlogService = eventlogService;
        this.topicService = topicService;
        this.topicCommentService = topicCommentService;
        this.studentTopicService = studentTopicService;
        this.notificationService = notificationService;
    }

    @GetMapping("/topics/{id}")
    public String topic(@PathVariable("id") Integer id,
                        Model model,
                        HttpServletRequest request) {

        Optional<Topic> topic = topicService.findById(id);
        if (!topic.isPresent()) {
            return "/error";
        }
        topic.ifPresent(value -> model.addAttribute("topic", value));

        int sessionUserId = ((User) request.getSession().getAttribute("sessionUser")).getId();
        Optional<StudentTopic> approvedTopic = studentTopicService.findApprovedByUserId(sessionUserId);
        model.addAttribute("hasApprovedTopic", approvedTopic.isPresent());

        List<TopicComment> comments = topicCommentService.findAllByTopicId(id);
        model.addAttribute("comments", comments);

        List<StudentTopic> studentTopics = studentTopicService.findAllByUserId(sessionUserId);
        Optional<StudentTopic> studentTopic =
                studentTopics.stream().filter(st -> st.getTopic().getId().equals(id)).findFirst();
        studentTopic.ifPresent(value -> model.addAttribute("studentTopic", value));

        return "topic";
    }

    @PostMapping("/topics/{id}/post-comment")
    public RedirectView postComment(@PathVariable("id") Integer topicId,
                                    HttpServletRequest request,
                                    RedirectAttributes redirectAttributes) {
        FormResponse formResponse = new FormResponse();

        User sessionUser = (User) request.getSession().getAttribute("sessionUser");

        Optional<Topic> topic = topicService.findById(topicId);
        String comment = request.getParameter("comment");
        try {
            if (!topic.isPresent()) {
                throw new Exception("Topic not retrieved");
            }

            TopicComment savedTopicComment =
                    topicCommentService.saveOrUpdate(
                            new TopicComment(
                                    null,
                                    sessionUser,
                                    topic.get(),
                                    comment,
                                    new Timestamp(System.currentTimeMillis())));

            if (savedTopicComment == null) {
                throw new Exception("Topic comment not saved");
            }

            Notification notification =
                    new Notification(
                            null,
                            null,
                            Notification.TYPE_COMMENT,
                            new Timestamp(System.currentTimeMillis()),
                            false,
                            false,
                            null,
                            savedTopicComment);
            notificationService.saveOrUpdate(notification);
            EventLog log =
                    new EventLog(
                            null,
                            EventLog.OPERATION_INSERT,
                            EventLog.STATUS_SUCCESS,
                            "POST COMMENT: Topic " + topicId + ".",
                            new Timestamp(System.currentTimeMillis()),
                            sessionUser);
            eventlogService.saveOrUpdate(log);
            formResponse.setStatus(FormResponse.STATUS_SUCCESS);
            formResponse.setMessage("Comment posted successfully");
            formResponse.setDescription("Your comment has been posted and can be visible to everyone");
        } catch (Exception e) {
            e.printStackTrace();
            EventLog log =
                    new EventLog(
                            null,
                            EventLog.OPERATION_INSERT,
                            EventLog.STATUS_FAILED,
                            "POST COMMENT: Topic " + topicId + ".",
                            new Timestamp(System.currentTimeMillis()),
                            sessionUser);
            eventlogService.saveOrUpdate(log);
            formResponse.setMessage("Oops!");
            formResponse.setStatus(FormResponse.STATUS_ERROR);
            formResponse.setDescription("Something went wrong while processing your request");
        }

        redirectAttributes.addFlashAttribute("formResponse", formResponse);

        return new RedirectView("/topics/" + topicId, true);
    }

    @PostMapping("/topics/{topicId}/edit-comment/{commentId}")
    public RedirectView editComment(@PathVariable("topicId") Integer topicId,
                                    @PathVariable("commentId") Integer commentId,
                                    HttpServletRequest request,
                                    RedirectAttributes redirectAttributes) {
        FormResponse formResponse = new FormResponse();

        String comment = request.getParameter("comment");
        Optional<TopicComment> commentOptional = topicCommentService.findById(commentId);

        User sessionUser = (User) request.getSession().getAttribute("sessionUser");

        try {
            if (!commentOptional.isPresent()) {
                throw new Exception("Comment not retrieved");
            }

            commentOptional.get().setComment(comment);
            TopicComment savedTopicComment = topicCommentService.saveOrUpdate(commentOptional.get());
            if (savedTopicComment == null) {
                throw new Exception("Topic comment not saved");
            }
            EventLog log =
                    new EventLog(
                            null,
                            EventLog.OPERATION_UPDATE,
                            EventLog.STATUS_SUCCESS,
                            "EDIT COMMENT: Topic " + topicId + " comment " + commentId + ".",
                            new Timestamp(System.currentTimeMillis()),
                            sessionUser);
            eventlogService.saveOrUpdate(log);
            formResponse.setStatus(FormResponse.STATUS_SUCCESS);
            formResponse.setMessage("Comment updated successfully");
            formResponse.setDescription(
                    "Your comment has been updated and the changes can be visible to everyone");
        } catch (Exception e) {
            e.printStackTrace();
            EventLog log =
                    new EventLog(
                            null,
                            EventLog.OPERATION_UPDATE,
                            EventLog.STATUS_FAILED,
                            "EDIT COMMENT: Topic " + topicId + " comment " + commentId + ".",
                            new Timestamp(System.currentTimeMillis()),
                            sessionUser);
            eventlogService.saveOrUpdate(log);
            formResponse.setMessage("Oops!");
            formResponse.setStatus(FormResponse.STATUS_ERROR);
            formResponse.setDescription("Something went wrong while processing your request");
        }

        redirectAttributes.addFlashAttribute("formResponse", formResponse);

        return new RedirectView("/topics/" + topicId, true);
    }

    @PostMapping("/topics/{topicId}/delete-comment/{commentId}")
    public RedirectView deleteComment(@PathVariable("topicId") Integer topicId,
                                      @PathVariable("commentId") Integer commentId,
                                      HttpServletRequest request,
                                      RedirectAttributes redirectAttributes) {
        FormResponse formResponse = new FormResponse();

        Optional<TopicComment> comment = topicCommentService.findById(commentId);
        User sessionUser = (User) request.getSession().getAttribute("sessionUser");

        try {
            if (!comment.isPresent()) {
                throw new Exception("Topic not found");
            }

            topicCommentService.deleteById(comment.get().getId());
            EventLog log =
                    new EventLog(
                            null,
                            EventLog.OPERATION_DELETE,
                            EventLog.STATUS_SUCCESS,
                            "DELETE COMMENT: Topic " + topicId + " comment " + commentId + ".",
                            new Timestamp(System.currentTimeMillis()),
                            sessionUser);
            eventlogService.saveOrUpdate(log);
            formResponse.setStatus(FormResponse.STATUS_SUCCESS);
            formResponse.setMessage("Comment successfully deleted");
            formResponse.setDescription("Your comment has been deleted");
        } catch (Exception e) {
            e.printStackTrace();
            EventLog log =
                    new EventLog(
                            null,
                            EventLog.OPERATION_DELETE,
                            EventLog.STATUS_FAILED,
                            "DELETE COMMENT: Topic " + topicId + " comment " + commentId + ".",
                            new Timestamp(System.currentTimeMillis()),
                            sessionUser);
            eventlogService.saveOrUpdate(log);
            formResponse.setMessage("Oops!");
            formResponse.setStatus(FormResponse.STATUS_ERROR);
            formResponse.setDescription("Something went wrong while processing your request");
        }

        redirectAttributes.addFlashAttribute("formResponse", formResponse);

        return new RedirectView("/topics/" + topicId, true);
    }

    @PostMapping("/topics/{topicId}/apply")
    public RedirectView applyTopicRequest(@PathVariable("topicId") Integer topicId,
                                          HttpServletRequest request,
                                          RedirectAttributes redirectAttributes) {
        FormResponse formResponse = new FormResponse();

        User sessionUser = (User) request.getSession().getAttribute("sessionUser");
        int sessionUserId = ((User) request.getSession().getAttribute("sessionUser")).getId();
        Optional<Topic> topic = topicService.findById(topicId);

        try {

            if (!topic.isPresent()) {
                throw new Exception("Topic not found");
            }

            StudentTopic studentTopic =
                    new StudentTopic(
                            null,
                            sessionUser,
                            topic.get(),
                            new Timestamp(System.currentTimeMillis()),
                            StudentTopic.STATUS_WAITING,
                            null);

            StudentTopic savedStudentTopic = studentTopicService.saveOrUpdate(studentTopic);

            if (savedStudentTopic == null) {
                throw new Exception("Student topic not saved");
            }
            Notification notification =
                    new Notification(
                            null,
                            StudentTopic.STATUS_WAITING,
                            Notification.TYPE_STUDENT_REQUEST,
                            new Timestamp(System.currentTimeMillis()),
                            false,
                            false,
                            savedStudentTopic,
                            null);
            notificationService.saveOrUpdate(notification);

            EventLog log =
                    new EventLog(
                            null,
                            EventLog.OPERATION_UPDATE,
                            EventLog.STATUS_SUCCESS,
                            "APPLY TOPIC: Topic " + topicId + " user " + sessionUserId + ".",
                            new Timestamp(System.currentTimeMillis()),
                            sessionUser);
            eventlogService.saveOrUpdate(log);

            formResponse.setStatus(FormResponse.STATUS_SUCCESS);
            formResponse.setMessage("Success");
            formResponse.setDescription("Your request has been sent to the teacher and will be examined shortly");
        } catch (Exception e) {
            e.printStackTrace();
            EventLog log =
                    new EventLog(
                            null,
                            EventLog.OPERATION_UPDATE,
                            EventLog.STATUS_FAILED,
                            "APPLY TOPIC: Topic " + topicId + " user " + sessionUserId + ".",
                            new Timestamp(System.currentTimeMillis()),
                            sessionUser);
            eventlogService.saveOrUpdate(log);
            formResponse.setMessage("Oops!");
            formResponse.setStatus(FormResponse.STATUS_ERROR);
            formResponse.setDescription("Something went wrong while processing your request");
        }

        redirectAttributes.addFlashAttribute("formResponse", formResponse);

        return new RedirectView("/topics/" + topicId, true);
    }

    @PostMapping("/topics/{topicId}/cancel-request")
    public RedirectView cancelTopicRequest(@PathVariable("topicId") Integer topicId,
                                           HttpServletRequest request,
                                           RedirectAttributes redirectAttributes) {
        FormResponse formResponse = new FormResponse();

        User sessionUser = (User) request.getSession().getAttribute("sessionUser");
        int sessionUserId = ((User) request.getSession().getAttribute("sessionUser")).getId();

        try {
            Optional<StudentTopic> studentTopic =
                    studentTopicService.findByTopicIdAndUserId(topicId, sessionUserId);
            if (!studentTopic.isPresent()) {
                throw new Exception("Student topic not retrieved");
            }

            studentTopicService.deleteById(studentTopic.get().getId());

            Notification notification =
                    new Notification(
                            null,
                            StudentTopic.STATUS_CANCELED,
                            Notification.TYPE_STUDENT_REQUEST,
                            new Timestamp(System.currentTimeMillis()),
                            false,
                            false,
                            studentTopic.get(),
                            null);
            notificationService.saveOrUpdate(notification);

            EventLog log =
                    new EventLog(
                            null,
                            EventLog.OPERATION_UPDATE,
                            EventLog.STATUS_SUCCESS,
                            "CANCEL REQUEST: Topic " + topicId + " user " + sessionUserId + ".",
                            new Timestamp(System.currentTimeMillis()),
                            sessionUser);
            eventlogService.saveOrUpdate(log);

            formResponse.setStatus(FormResponse.STATUS_SUCCESS);
            formResponse.setMessage("Request canceled successfully");
            formResponse.setDescription("Your request on this topic has been canceled");
        } catch (Exception e) {
            e.printStackTrace();
            EventLog log =
                    new EventLog(
                            null,
                            EventLog.OPERATION_UPDATE,
                            EventLog.STATUS_FAILED,
                            "CANCEL REQUEST: Topic " + topicId + " user " + sessionUserId + ".",
                            new Timestamp(System.currentTimeMillis()),
                            sessionUser);
            eventlogService.saveOrUpdate(log);
            formResponse.setMessage("Oops!");
            formResponse.setStatus(FormResponse.STATUS_ERROR);
            formResponse.setDescription("Something went wrong while processing your request");
        }

        redirectAttributes.addFlashAttribute("formResponse", formResponse);

        return new RedirectView("/topics/" + topicId, true);
    }

    @PostMapping("/topics/{topicId}/cancel-application")
    public RedirectView cancelApplication(@PathVariable("topicId") Integer topicId,
                                          HttpServletRequest request,
                                          RedirectAttributes redirectAttributes) {
        FormResponse formResponse = new FormResponse();

        User sessionUser = (User) request.getSession().getAttribute("sessionUser");
        int sessionUserId = ((User) request.getSession().getAttribute("sessionUser")).getId();

        try {
            Optional<StudentTopic> studentTopic = studentTopicService.findByTopicIdAndUserId(topicId, sessionUserId);
            if (!studentTopic.isPresent()) {
                throw new Exception("Student topic not retrieved");
            }

            studentTopic.get().setStatus(StudentTopic.STATUS_CANCELED);
            studentTopic.get().setReason(StudentTopic.REASON_STUDENT_CANCELED_APPROVED_TOPIC);

            StudentTopic savedStudentTopic = studentTopicService.saveOrUpdate(studentTopic.get());
            if (savedStudentTopic == null) {
                throw new Exception("Student topic not saved");
            }

            Notification notification = new Notification(null,
                    StudentTopic.STATUS_CANCELED,
                    Notification.TYPE_STUDENT_APPLICATION_CANCEL,
                    new Timestamp(System.currentTimeMillis()),
                    false,
                    false,
                    studentTopic.get(),
                    null);
            notificationService.saveOrUpdate(notification);

            List<StudentTopic> canceledWaitingRequests = studentTopicService
                    .findAllByUserIdAndStatusAndReason(sessionUserId, StudentTopic.STATUS_CANCELED, StudentTopic.REASON_ALREADY_APPROVED_TOPIC);
            canceledWaitingRequests.forEach(canceledWaitingRequest -> studentTopicService.deleteById(canceledWaitingRequest.getId()));

            EventLog log =
                    new EventLog(
                            null,
                            EventLog.OPERATION_UPDATE,
                            EventLog.STATUS_SUCCESS,
                            "CANCEL APPLICATION: Topic " + topicId + " user " + sessionUserId + ".",
                            new Timestamp(System.currentTimeMillis()),
                            sessionUser);
            eventlogService.saveOrUpdate(log);

            formResponse.setStatus(FormResponse.STATUS_SUCCESS);
            formResponse.setMessage("Request canceled successfully");
            formResponse.setDescription("Your request on this topic has been canceled");
        } catch (Exception e) {
            e.printStackTrace();
            EventLog log =
                    new EventLog(
                            null,
                            EventLog.OPERATION_UPDATE,
                            EventLog.STATUS_FAILED,
                            "CANCEL APPLICATION: Topic " + topicId + " user " + sessionUserId + ".",
                            new Timestamp(System.currentTimeMillis()),
                            sessionUser);
            eventlogService.saveOrUpdate(log);
            formResponse.setMessage("Oops!");
            formResponse.setStatus(FormResponse.STATUS_ERROR);
            formResponse.setDescription("Something went wrong while processing your request");
        }

        redirectAttributes.addFlashAttribute("formResponse", formResponse);

        return new RedirectView("/topics/" + topicId, true);
    }

    @ResponseBody
    @PostMapping("/topics/get-comment/{id}")
    public ResponseEntity<String> getCommentById(@PathVariable("id") Integer commentId) {
        Optional<TopicComment> comment = topicCommentService.findById(commentId);

        return comment
                .map(topicComment -> new ResponseEntity<>(topicComment.getComment(), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }
}
