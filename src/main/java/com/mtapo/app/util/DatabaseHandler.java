package com.mtapo.app.util;

import com.mtapo.app.entity.*;
import com.mtapo.app.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

@Slf4j
@Component
public class DatabaseHandler {

    private final PasswordEncoder passwordEncoder;
    private final CategoryService categoryService;
    private final RoleService roleService;
    private final UserService userService;
    private final TopicService topicService;
    private final StudentTopicService studentTopicService;
    private final TopicCommentService topicCommentService;
    private final EventLogService eventLogService;
    private final TagService tagService;
    private final NotificationService notificationService;

    @Value("${database.initialize}")
    private String initializeDatabase;

    @Autowired
    public DatabaseHandler(PasswordEncoder passwordEncoder,
                           CategoryService categoryService,
                           RoleService roleService,
                           UserService userService,
                           TopicService topicService,
                           StudentTopicService studentTopicService,
                           TopicCommentService topicCommentService,
                           EventLogService eventLogService,
                           TagService tagService,
                           NotificationService notificationService) {
        this.passwordEncoder = passwordEncoder;
        this.categoryService = categoryService;
        this.roleService = roleService;
        this.userService = userService;
        this.topicService = topicService;
        this.studentTopicService = studentTopicService;
        this.topicCommentService = topicCommentService;
        this.eventLogService = eventLogService;
        this.tagService = tagService;
        this.notificationService = notificationService;
    }

    @PostConstruct
    public void initializeDatabase() {
        if (!initializeDatabase.equalsIgnoreCase("yes")) {
            return;
        }

        /* CLEANING AND INITIALIZING DATABASE */
        log.debug("initializeDatabase()");

        // Remove all instances
        cleanDatabase();

        // Create categories
        Category license = new Category(null, Category.LICENSE);
        Category master = new Category(null, Category.MASTER);
        Arrays.asList(license, master).forEach(category -> {
            categoryService.saveOrUpdate(category);
            logSuccessInsertOperation("Created category with id #" + category.getId(), null);
        });

        // Create user roles
        Role studentRole = new Role(null, Role.STUDENT);
        Role teacherRole = new Role(null, Role.TEACHER);
        Role adminRole = new Role(null, Role.ADMIN);
        Arrays.asList(studentRole, teacherRole, adminRole).forEach(role -> {
            roleService.saveOrUpdate(role);
            logSuccessInsertOperation("Created role with id #" + role.getId(), null);
        });

        // Create students
        User studentLicense1 = new User(null,
                "Barack",
                "Obama",
                "student1@email.com",
                passwordEncoder.encode("pass"),
                new HashSet<>(Collections.singletonList(studentRole)),
                license);
        User studentLicense2 = new User(null,
                "John",
                "Wich",
                "student2@email.com",
                passwordEncoder.encode("pass"),
                new HashSet<>(Collections.singletonList(studentRole)),
                license);
        User studentLicense3 = new User(null,
                "Ionut-Cristian",
                "Adam-Papadatu",
                "student3@email.com",
                passwordEncoder.encode("pass"),
                new HashSet<>(Collections.singletonList(studentRole)),
                license);
        User studentMaster1 = new User(null,
                "Dan",
                "Badea",
                "student4@email.com",
                passwordEncoder.encode("pass"),
                new HashSet<>(Collections.singletonList(studentRole)),
                master);
        User studentMaster2 = new User(null,
                "Maria",
                "Dumitrescu",
                "student5@email.com",
                passwordEncoder.encode("pass"),
                new HashSet<>(Collections.singletonList(studentRole)),
                master);
        User studentMaster3 = new User(null,
                "Darth",
                "Vader",
                "student6@email.com",
                passwordEncoder.encode("pass"),
                new HashSet<>(Collections.singletonList(studentRole)),
                master);

        // Create teachers
        User teacherUser1 = new User(null,
                "Catalina",
                "Bejan",
                "teacher1@email.com",
                passwordEncoder.encode("pass"),
                new HashSet<>(Collections.singletonList(teacherRole)),
                null);
        User teacherUser2 = new User(null,
                "Gavriil",
                "Negoita",
                "teacher2@email.com",
                passwordEncoder.encode("pass"),
                new HashSet<>(Collections.singletonList(teacherRole)),
                null);
        User teacherUser3 = new User(null,
                "Alexandru Cristi",
                "Panaite",
                "teacher3@email.com",
                passwordEncoder.encode("pass"),
                new HashSet<>(Collections.singletonList(teacherRole)),
                null);

        // Create admins
        User adminUser = new User(null,
                "Adriana",
                "Stratulat-Diaconu",
                "admin@email.com",
                passwordEncoder.encode("pass"),
                new HashSet<>(Collections.singletonList(adminRole)),
                null);

        // Create users with mixed roles
        User studentTeacherUser = new User(null,
                "John",
                "Cena",
                "student.teacher@email.com",
                passwordEncoder.encode("pass"),
                new HashSet<>(Arrays.asList(studentRole, teacherRole)),
                license);
        User teacherAdminUser = new User(null,
                "Mike",
                "Tyson",
                "teacher.admin@email.com",
                passwordEncoder.encode("pass"),
                new HashSet<>(Arrays.asList(studentRole, teacherRole)),
                null);
        User studentTeacherAdminUser = new User(null,
                "Arnold",
                "Schwarzenegger",
                "student.teacher.admin@email.com",
                passwordEncoder.encode("pass"),
                new HashSet<>(Arrays.asList(studentRole, teacherRole, adminRole)),
                master);
        Arrays.asList(studentLicense1, studentLicense2, studentLicense3,
                studentMaster1, studentMaster2, studentMaster3,
                teacherUser1, teacherUser2, teacherUser3,
                adminUser, studentTeacherUser, teacherAdminUser, studentTeacherAdminUser).forEach(user -> {
            userService.saveOrUpdate(user);
            logSuccessInsertOperation("Created user with id #" + user.getId(), null);
        });

        // Create common tags
        Tag uncategorizedTag = new Tag(null, Tag.UNCATEGORIZED);
        Tag webAppTag = new Tag(null, "web-app");
        Tag artificialIntelligenceTag = new Tag(null, "artificial-intelligence");
        Tag machineLearningTag = new Tag(null, "machine-learning");
        Tag mobileAppTag = new Tag(null, "mobile-app");
        Tag circuitsTag = new Tag(null, "circuits");
        Tag platformTag = new Tag(null, "platform");
        Arrays.asList(uncategorizedTag,
                webAppTag,
                artificialIntelligenceTag,
                machineLearningTag,
                mobileAppTag,
                circuitsTag,
                platformTag).forEach(tag -> {
            tagService.saveOrUpdate(tag);
            logSuccessInsertOperation("Created tag with id #" + tag.getId(), adminUser);
        });

        // Create topics
        Topic topic1 = new Topic(null,
                "Topic #1",
                "This is the description for topic #1",
                new HashSet<>(Arrays.asList(webAppTag, mobileAppTag, platformTag)),
                Timestamp.valueOf("2021-01-01 12:00:00"),
                teacherUser1,
                master);
        Topic topic2 = new Topic(null,
                "Topic #2",
                "This is the description for topic #2",
                new HashSet<>(Arrays.asList(mobileAppTag, circuitsTag)),
                Timestamp.valueOf("2021-01-02 12:00:00"),
                teacherUser1,
                license);
        Topic topic3 = new Topic(null,
                "Topic #3",
                "This is the description for topic #3",
                new HashSet<>(Arrays.asList(webAppTag, platformTag)),
                Timestamp.valueOf("2021-01-03 12:00:00"),
                teacherUser1,
                master);
        Topic topic4 = new Topic(null,
                "Topic #4",
                "This is the description for topic #4",
                new HashSet<>(Arrays.asList(mobileAppTag, artificialIntelligenceTag)),
                Timestamp.valueOf("2021-01-04 12:00:00"),
                teacherUser2,
                master);
        Topic topic5 = new Topic(null,
                "Topic #5",
                "This is the description for topic #5",
                new HashSet<>(Arrays.asList(machineLearningTag, webAppTag)),
                Timestamp.valueOf("2021-01-05 12:00:00"),
                teacherUser2,
                master);
        Topic topic6 = new Topic(null,
                "Topic #6",
                "This is the description for topic #6",
                new HashSet<>(Arrays.asList(mobileAppTag, webAppTag)),
                Timestamp.valueOf("2021-01-06 12:00:00"),
                teacherUser2,
                license);
        Topic topic7 = new Topic(null,
                "Topic #7",
                "This is the description for topic #7",
                new HashSet<>(Collections.singletonList(webAppTag)),
                Timestamp.valueOf("2021-01-07 12:00:00"),
                teacherUser3,
                license);
        Topic topic8 = new Topic(null,
                "Topic #8",
                "This is the description for topic #8",
                new HashSet<>(Collections.singletonList(mobileAppTag)),
                Timestamp.valueOf("2021-01-08 12:00:00"),
                teacherUser3,
                license);
        Topic topic9 = new Topic(null,
                "Topic #9",
                "This is the description for topic #9",
                new HashSet<>(Collections.singletonList(platformTag)),
                Timestamp.valueOf("2021-01-09 12:00:00"),
                teacherUser3,
                master);
        Arrays.asList(topic1, topic2, topic3,
                topic4, topic5, topic6,
                topic7, topic8, topic9).forEach(topic -> {
            topicService.saveOrUpdate(topic);
            logSuccessInsertOperation("Created topic with id #" + topic.getId(), topic.getUser());
        });

        // Create topic requests
        StudentTopic studentTopic1 = new StudentTopic(null,
                studentLicense1,
                topic6,
                Timestamp.valueOf("2021-05-01 12:00:00"),
                StudentTopic.STATUS_CANCELED,
                StudentTopic.REASON_ALREADY_APPROVED_TOPIC);
        StudentTopic studentTopic2 = new StudentTopic(null,
                studentLicense1,
                topic2,
                Timestamp.valueOf("2021-05-02 12:00:00"),
                StudentTopic.STATUS_APPROVED,
                null);
        StudentTopic studentTopic3 = new StudentTopic(null,
                studentLicense2,
                topic2,
                Timestamp.valueOf("2021-05-03 12:00:00"),
                StudentTopic.STATUS_WAITING,
                null);
        StudentTopic studentTopic4 = new StudentTopic(null,
                studentLicense2,
                topic7,
                Timestamp.valueOf("2021-05-04 12:00:00"),
                StudentTopic.STATUS_WAITING,
                null);
        StudentTopic studentTopic5 = new StudentTopic(null,
                studentLicense3,
                topic6,
                Timestamp.valueOf("2021-05-05 12:00:00"),
                StudentTopic.STATUS_DECLINED,
                StudentTopic.REASON_REQUEST_DECLINED);
        StudentTopic studentTopic6 = new StudentTopic(null,
                studentLicense3,
                topic7,
                Timestamp.valueOf("2021-05-06 12:00:00"),
                StudentTopic.STATUS_WAITING,
                null);
        StudentTopic studentTopic7 = new StudentTopic(null,
                studentMaster1,
                topic1,
                Timestamp.valueOf("2021-05-07 12:00:00"),
                StudentTopic.STATUS_DECLINED,
                StudentTopic.REASON_REQUEST_DECLINED);
        StudentTopic studentTopic8 = new StudentTopic(null,
                studentMaster1,
                topic9,
                Timestamp.valueOf("2021-05-08 12:00:00"),
                StudentTopic.STATUS_WAITING,
                null);
        StudentTopic studentTopic9 = new StudentTopic(null,
                studentMaster2,
                topic1,
                Timestamp.valueOf("2021-05-09 12:00:00"),
                StudentTopic.STATUS_DECLINED,
                StudentTopic.REASON_REQUEST_DECLINED);
        StudentTopic studentTopic10 = new StudentTopic(null,
                studentMaster2,
                topic9,
                Timestamp.valueOf("2021-05-10 12:00:00"),
                StudentTopic.STATUS_APPROVED,
                null);
        StudentTopic studentTopic11 = new StudentTopic(null,
                studentMaster3,
                topic3,
                Timestamp.valueOf("2021-05-11 12:00:00"),
                StudentTopic.STATUS_CANCELED,
                StudentTopic.REASON_STUDENT_CANCELED_APPROVED_TOPIC);
        StudentTopic studentTopic12 = new StudentTopic(null,
                studentMaster3,
                topic9,
                Timestamp.valueOf("2021-05-12 12:00:00"),
                StudentTopic.STATUS_CANCELED,
                StudentTopic.REASON_TEACHER_CANCELED_APPROVED_TOPIC);
        Arrays.asList(studentTopic1, studentTopic2,
                studentTopic3, studentTopic4,
                studentTopic5, studentTopic6,
                studentTopic7, studentTopic8,
                studentTopic9, studentTopic10,
                studentTopic11, studentTopic12).forEach(studentTopic -> {
            studentTopicService.saveOrUpdate(studentTopic);
            logSuccessInsertOperation("Created topic request with id #" + studentTopic.getId(), studentTopic.getUser());
            sendNotification(StudentTopic.STATUS_WAITING, Notification.TYPE_STUDENT_REQUEST, studentTopic, null);

            switch (studentTopic.getStatus()) {
                case StudentTopic.STATUS_CANCELED:
                    if (!studentTopic.getReason().equals(StudentTopic.REASON_STUDENT_CANCELED_APPROVED_TOPIC) && studentTopic.getTopic().getUser() != studentTopic.getUser()) {
                        sendNotification(StudentTopic.STATUS_CANCELED, Notification.TYPE_STUDENT_REQUEST, studentTopic, null);
                    }
                    break;
                case StudentTopic.STATUS_APPROVED:
                    sendNotification(StudentTopic.STATUS_APPROVED, Notification.TYPE_TEACHER_RESPONSE, studentTopic, null);
                    break;
                case StudentTopic.STATUS_DECLINED:
                    sendNotification(StudentTopic.STATUS_DECLINED, Notification.TYPE_TEACHER_RESPONSE, studentTopic, null);
                    break;
            }

        });

        // Create topic comments
        TopicComment topicComment1 = new TopicComment(null,
                studentLicense1,
                topic2,
                "What language should we use?",
                Timestamp.valueOf("2021-05-03 12:00:00"));
        TopicComment topicComment2 = new TopicComment(null,
                studentLicense2,
                topic2,
                "What technologies should we use?",
                Timestamp.valueOf("2021-05-04 12:00:00"));
        TopicComment topicComment3 = new TopicComment(null,
                studentLicense3,
                topic6,
                "What do you mean by S-IT?",
                Timestamp.valueOf("2021-06-03 12:00:00"));
        TopicComment topicComment4 = new TopicComment(null,
                studentMaster1,
                topic1,
                "What tools do we need?",
                Timestamp.valueOf("2021-06-03 12:00:01"));
        TopicComment topicComment5 = new TopicComment(null,
                studentMaster2,
                topic1,
                "How do we do this?",
                Timestamp.valueOf("2021-06-04 12:00:01"));
        TopicComment topicComment6 = new TopicComment(null,
                studentMaster3,
                topic3,
                "I didn't understand the requirements?",
                Timestamp.valueOf("2021-06-04 12:00:06"));
        Arrays.asList(topicComment1, topicComment2, topicComment3,
                topicComment4, topicComment5, topicComment6).forEach(topicComment -> {
            topicCommentService.saveOrUpdate(topicComment);
            logSuccessInsertOperation("Created topic comment with id #" + topicComment.getId(), topicComment.getUser());
            sendNotification(null, Notification.TYPE_COMMENT, null, topicComment);
        });
    }

    private void logSuccessInsertOperation(String details,
                                           User user) {
        EventLog eventLog = new EventLog(null,
                EventLog.OPERATION_INSERT,
                EventLog.STATUS_SUCCESS,
                details,
                new Timestamp(System.currentTimeMillis()),
                user);
        eventLogService.saveOrUpdate(eventLog);
    }

    private void sendNotification(String description,
                                  String type,
                                  StudentTopic studentTopic,
                                  TopicComment topicComment) {
        Notification notification = new Notification(null,
                description,
                type,
                new Timestamp(System.currentTimeMillis()),
                false,
                true,
                studentTopic,
                topicComment);
        notificationService.saveOrUpdate(notification);
    }

    private void cleanDatabase() {
        notificationService.deleteAll();
        eventLogService.deleteAll();
        studentTopicService.deleteAll();
        topicCommentService.deleteAll();
        topicService.deleteAll();
        userService.deleteAll();
        categoryService.deleteAll();
        roleService.deleteAll();
    }
}
