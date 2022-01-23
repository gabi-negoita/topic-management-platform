package com.mtapo.app.controller;

import com.mtapo.app.entity.*;
import com.mtapo.app.model.FormResponse;
import com.mtapo.app.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.*;

@Controller
public class UploadedTopicsController {

    private final TopicService topicService;
    private final UserService userService;
    private final CategoryService categoryService;
    private final TagService tagService;
    private final StudentTopicService studentTopicService;
    private final EventLogService eventlogService;
    private final NotificationService notificationService;

    @Autowired
    public UploadedTopicsController(EventLogService eventlogService,
                                    TopicService topicService,
                                    UserService userService,
                                    CategoryService categoryService,
                                    TagService tagService,
                                    StudentTopicService studentTopicService,
                                    NotificationService notificationService) {
        this.eventlogService = eventlogService;
        this.topicService = topicService;
        this.userService = userService;
        this.categoryService = categoryService;
        this.tagService = tagService;
        this.studentTopicService = studentTopicService;
        this.notificationService = notificationService;
    }

    @RequestMapping("/uploaded-topics")
    public String uploadedTopics(HttpServletRequest request,
                                 Model model) {

        int sessionUserId = ((User) request.getSession().getAttribute("sessionUser")).getId();

        String tags = request.getParameter("tags");
        String category = request.getParameter("category");

        List<String> tagList;
        List<Topic> topics;

        if (category == null || category.isEmpty()) {
            category = null;
        }

        if (tags == null || tags.isEmpty()) {
            topics = topicService.findAllByCategoryAndUser(category, sessionUserId);
        } else {
            tagList = tags.contains(",")
                    ? Arrays.asList(tags.split(","))
                    : new ArrayList<>(Collections.singletonList(tags));

            topics = topicService.findAllByCategoryUserAndTags(category, sessionUserId, tagList);
        }

        Map<Integer, List<User>> topicIdToUsersMap = new HashMap<>();
        topics.forEach(topic -> {
            List<User> users = userService.findAllByTopicId(topic.getId());
            topicIdToUsersMap.put(topic.getId(), users);
        });

        model.addAttribute("topics", topics);
        model.addAttribute("topicIdToUsersMap", topicIdToUsersMap);

        return "uploaded-topics";
    }

    @RequestMapping("/uploaded-topics/create-topic")
    public RedirectView createTopic(
            HttpServletRequest request,
            RedirectAttributes redirectAttributes) {

        User sessionUser = (User) request.getSession().getAttribute("sessionUser");

        String title = request.getParameter("title");
        String categoryName = request.getParameter("category");
        String tags = request.getParameter("tags");
        String description = request.getParameter("description");

        FormResponse formResponse = new FormResponse();
        try {

            Optional<Category> category = categoryService.findByName(categoryName);
            if (!category.isPresent()) {
                throw new Exception("Category could not be retrieved");
            }

            Set<Tag> tagSet = new HashSet<>();

            List<String> tagNameList =
                    (tags == null || tags.isEmpty())
                            ? new ArrayList<>()
                            : tags.contains(",")
                            ? Arrays.asList(tags.trim().split(","))
                            : Collections.singletonList(tags.trim());

            if (tagNameList.isEmpty()) {
                Optional<Tag> tagByName = tagService.findByName(Tag.UNCATEGORIZED);
                tagByName.ifPresent(tagSet::add);
            }

            tagNameList.forEach(tagName -> {
                Optional<Tag> tagByName = tagService.findByName(tagName);
                if (tagByName.isPresent()) {
                    tagSet.add(tagByName.get());
                } else {
                    Tag savedTag = tagService.saveOrUpdate(new Tag(null, tagName));
                    tagSet.add(savedTag);
                }
            });

            Topic topic = new Topic(null,
                    title,
                    description,
                    tagSet,
                    new Timestamp(System.currentTimeMillis()),
                    sessionUser,
                    category.get());
            Topic savedTopic = topicService.saveOrUpdate(topic);

            if (savedTopic == null) {
                throw new Exception();
            }
            EventLog log = new EventLog(null,
                    EventLog.OPERATION_INSERT,
                    EventLog.STATUS_SUCCESS,
                    "CREATE TOPIC",
                    new Timestamp(System.currentTimeMillis()),
                    sessionUser);
            eventlogService.saveOrUpdate(log);

            formResponse.setStatus(FormResponse.STATUS_SUCCESS);
            formResponse.setMessage("Topic successfully created");
            formResponse.setDescription(
                    "Topic with id of "
                            + "<a href='/topics/"
                            + savedTopic.getId()
                            + "'><b>#"
                            + savedTopic.getId()
                            + "</b></a>"
                            + " has been created");

        } catch (Exception e) {
            e.printStackTrace();
            EventLog log = new EventLog(null,
                    EventLog.OPERATION_INSERT,
                    EventLog.STATUS_FAILED,
                    "CREATE TOPIC",
                    new Timestamp(System.currentTimeMillis()),
                    sessionUser);
            eventlogService.saveOrUpdate(log);

            formResponse.setMessage("Oops!");
            formResponse.setStatus(FormResponse.STATUS_ERROR);
            formResponse.setDescription("Something went wrong while processing your request");
        }

        redirectAttributes.addFlashAttribute("formResponse", formResponse);

        return new RedirectView("/uploaded-topics", true);
    }

    @RequestMapping("/uploaded-topics/delete-topic/{id}")
    public RedirectView deleteTopic(
            @PathVariable("id") Integer topicId,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes) {
        User sessionUser = (User) request.getSession().getAttribute("sessionUser");
        FormResponse formResponse = new FormResponse();
        try {
            Optional<Topic> topic = topicService.findById(topicId);
            if (!topic.isPresent()) {
                throw new Exception("Topic not retrieved");
            }

            Set<Tag> oldTags = topic.get().getTags();

            topicService.deleteById(topic.get().getId());

            oldTags.forEach(tag -> {
                if (tagService.countUsesByName(tag.getName()) == 0) {
                    tagService.deleteById(tag.getId());
                }
            });
            EventLog log = new EventLog(null,
                    EventLog.OPERATION_DELETE,
                    EventLog.STATUS_SUCCESS,
                    "DELETE TOPIC: Topic " + topicId + ".",
                    new Timestamp(System.currentTimeMillis()),
                    sessionUser);
            eventlogService.saveOrUpdate(log);
            formResponse.setStatus(FormResponse.STATUS_SUCCESS);
            formResponse.setMessage("Topic successfully deleted");
            formResponse.setDescription("Topic with id of "
                    + "<a><b>#"
                    + topicId
                    + "</b></a> "
                    + "and objects-related to this topic have been deleted");

        } catch (Exception e) {
            e.printStackTrace();
            EventLog log = new EventLog(null,
                    EventLog.OPERATION_DELETE,
                    EventLog.STATUS_FAILED,
                    "DELETE TOPIC: Topic " + topicId + ".",
                    new Timestamp(System.currentTimeMillis()),
                    sessionUser);
            eventlogService.saveOrUpdate(log);
            formResponse.setMessage("Oops!");
            formResponse.setStatus(FormResponse.STATUS_ERROR);
            formResponse.setDescription("Something went wrong while processing your request");
        }

        redirectAttributes.addFlashAttribute("formResponse", formResponse);

        return new RedirectView("/uploaded-topics", true);
    }

    @RequestMapping("/uploaded-topics/edit-topic/{id}")
    public RedirectView editTopic(
            @PathVariable("id") Integer topicId,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes) {
        User sessionUser = (User) request.getSession().getAttribute("sessionUser");
        String title = request.getParameter("title");
        String categoryName = request.getParameter("category");
        String tags = request.getParameter("tags");
        String description = request.getParameter("description");

        FormResponse formResponse = new FormResponse();
        try {

            Optional<Category> category = categoryService.findByName(categoryName);
            if (!category.isPresent()) {
                throw new Exception("Category could not be retrieved");
            }

            Set<Tag> tagSet = new HashSet<>();

            List<String> tagNameList = (tags == null || tags.isEmpty())
                    ? new ArrayList<>()
                    : tags.contains(",")
                    ? Arrays.asList(tags.trim().split(","))
                    : Collections.singletonList(tags.trim());

            if (tagNameList.isEmpty()) {
                Optional<Tag> tagByName = tagService.findByName(Tag.UNCATEGORIZED);
                tagByName.ifPresent(tagSet::add);
            }

            tagNameList.forEach(tagName -> {
                Optional<Tag> tagByName = tagService.findByName(tagName);
                if (tagByName.isPresent()) {
                    tagSet.add(tagByName.get());
                } else {
                    Tag savedTag = tagService.saveOrUpdate(new Tag(null, tagName));
                    tagSet.add(savedTag);
                }
            });

            Optional<Topic> topicOptional = topicService.findById(topicId);
            if (!topicOptional.isPresent()) {
                throw new Exception("Topic with id: " + topicId + " could not be retrieved");
            }

            Set<Tag> oldTags = topicOptional.get().getTags();

            Topic topic = topicOptional.get();
            topic.setTitle(title);
            topic.setCategory(category.get());
            topic.setDescription(description);
            topic.setTags(tagSet);

            Topic savedTopic = topicService.saveOrUpdate(topic);
            if (savedTopic == null) {
                throw new Exception("Topic with id: " + topicId + " could not be updated");
            }

            oldTags.forEach(tag -> {
                if (tagService.countUsesByName(tag.getName()) == 0) {
                    tagService.deleteById(tag.getId());
                }
            });
            EventLog log = new EventLog(null,
                    EventLog.OPERATION_UPDATE,
                    EventLog.STATUS_SUCCESS,
                    "EDIT TOPIC: Topic " + topicId + ".",
                    new Timestamp(System.currentTimeMillis()),
                    sessionUser);
            eventlogService.saveOrUpdate(log);
            formResponse.setStatus(FormResponse.STATUS_SUCCESS);
            formResponse.setMessage("Topic successfully updated");
            formResponse.setDescription(
                    "Topic with id of " + "<a><b>#" + topicId + "</b></a> " + "has been updated");

        } catch (Exception e) {
            e.printStackTrace();
            EventLog log = new EventLog(null,
                    EventLog.OPERATION_UPDATE,
                    EventLog.STATUS_FAILED,
                    "EDIT TOPIC: Topic " + topicId + ".",
                    new Timestamp(System.currentTimeMillis()),
                    sessionUser);
            eventlogService.saveOrUpdate(log);
            formResponse.setMessage("Oops!");
            formResponse.setStatus(FormResponse.STATUS_ERROR);
            formResponse.setDescription("Something went wrong while processing your request");
        }

        redirectAttributes.addFlashAttribute("formResponse", formResponse);

        return new RedirectView("/uploaded-topics", true);
    }

    @RequestMapping("/uploaded-topics/remove-student/{topicId}/{userId}")
    public RedirectView removeStudent(
            @PathVariable("topicId") Integer topicId,
            @PathVariable("userId") Integer userId,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes) {

        User sessionUser = (User) request.getSession().getAttribute("sessionUser");

        FormResponse formResponse = new FormResponse();
        try {

            Optional<StudentTopic> studentTopicOptional = studentTopicService.findByTopicIdAndUserId(topicId, userId);
            if (!studentTopicOptional.isPresent()) {
                throw new Exception("Student-Topic could not be found");
            }

            StudentTopic studentTopic = studentTopicOptional.get();
            studentTopic.setStatus(StudentTopic.STATUS_CANCELED);
            studentTopic.setReason(StudentTopic.REASON_TEACHER_CANCELED_APPROVED_TOPIC);

            StudentTopic savedStudentTopic = studentTopicService.saveOrUpdate(studentTopic);
            if (savedStudentTopic == null) {
                throw new Exception("Student-Topic updates not saved");
            }

            Notification notification = new Notification(null,
                    StudentTopic.STATUS_DECLINED,
                    Notification.TYPE_TEACHER_APPLICATION_CANCEL,
                    new Timestamp(System.currentTimeMillis()),
                    false,
                    false,
                    studentTopic,
                    null);
            notificationService.saveOrUpdate(notification);

            EventLog log = new EventLog(null,
                    EventLog.OPERATION_UPDATE,
                    EventLog.STATUS_SUCCESS,
                    "REMOVE STUDENT FROM TOPIC: Topic " + topicId + " student " + userId + ".",
                    new Timestamp(System.currentTimeMillis()),
                    sessionUser);
            eventlogService.saveOrUpdate(log);
            formResponse.setStatus(FormResponse.STATUS_SUCCESS);
            formResponse.setMessage("Student successfully removed");
            formResponse.setDescription(
                    "Student with id of "
                            + "<a><b>#"
                            + userId
                            + "</b></a> "
                            + "were removed from topic with id of <a><b>#"
                            + topicId
                            + "</b></a>");

        } catch (Exception e) {
            e.printStackTrace();
            EventLog log = new EventLog(null,
                    EventLog.OPERATION_UPDATE,
                    EventLog.STATUS_FAILED,
                    "REMOVE STUDENT FROM TOPIC: Topic " + topicId + " student " + userId + ".",
                    new Timestamp(System.currentTimeMillis()),
                    sessionUser);
            eventlogService.saveOrUpdate(log);
            formResponse.setMessage("Oops!");
            formResponse.setStatus(FormResponse.STATUS_ERROR);
            formResponse.setDescription("Something went wrong while processing your request");
        }

        redirectAttributes.addFlashAttribute("formResponse", formResponse);

        return new RedirectView("/uploaded-topics", true);
    }

    @ResponseBody
    @PostMapping("/uploaded-topics/get-current-user-tags")
    public ResponseEntity<Set<Tag>> getCurrentUserTags(HttpServletRequest request) {

        int sessionUserId = ((User) request.getSession().getAttribute("sessionUser")).getId();
        Set<Tag> tags = tagService.findAllByUserId(sessionUserId);

        return new ResponseEntity<>(tags, HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping("/uploaded-topics/get-all-tags")
    public ResponseEntity<List<Tag>> getAllTags() {

        List<Tag> tags = tagService.findAll();

        return new ResponseEntity<>(tags, HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping("/uploaded-topics/get-topic/{id}")
    public ResponseEntity<Topic> getTopicById(@PathVariable("id") Integer topicId) {

        Optional<Topic> topic = topicService.findById(topicId);
        return topic
                .map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElse(new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }
}
