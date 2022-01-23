package com.mtapo.app;

import com.mtapo.app.entity.*;
import com.mtapo.app.service.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
@DisplayName("[Integration tests] Given the services")
class MTAPOApplicationTests {

    @Autowired
    CategoryService categoryService;

    @Autowired
    RoleService roleService;

    @Autowired
    StudentTopicService studentTopicService;

    @Autowired
    TopicCommentService topicCommentService;

    @Autowired
    TopicService topicService;

    @Autowired
    UserService userService;

    @Autowired
    NotificationService sthService;

    @AfterEach
    void flushDataBase() {
        log.debug("Flushing database...");

        topicCommentService.deleteAll();
        studentTopicService.deleteAll();
        userService.deleteAll();
        topicService.deleteAll();
        roleService.deleteAll();
        categoryService.deleteAll();
    }

    @Test
    @DisplayName("When calling findAll then all should return an empty list")
    void allRepositoriesShouldBeEmpty() {
        List<Category> categories = categoryService.findAll();
        assertTrue(categories.isEmpty());

        List<Role> roles = roleService.findAll();
        assertTrue(roles.isEmpty());

        List<StudentTopic> studentTopics = studentTopicService.findAll();
        assertTrue(studentTopics.isEmpty());

        List<TopicComment> topicComments = topicCommentService.findAll();
        assertTrue(topicComments.isEmpty());

        List<Topic> topics = topicService.findAll();
        assertTrue(topics.isEmpty());

        List<User> users = userService.findAll();
        assertTrue(users.isEmpty());
    }

    @Test
    @DisplayName("When deleting an user then the student-topic instances should be deleted")
    void allStudentTopicsShouldBeDeletedOnUserDeleted() {
        Category category = new Category(null, Category.LICENSE);
        categoryService.saveOrUpdate(category);

        User user = new User(null,
                "John",
                "Doe",
                "john.doe@email.com",
                "pass",
                null,
                category);
        User savedUser = userService.saveOrUpdate(user);
        assertNotNull(savedUser, "User not saved");

        Topic topic1 = new Topic(null,
                "Topic #1",
                "This is topic #1",
                null,
                new Timestamp(System.currentTimeMillis()),
                null,
                category);
        Topic savedTopic1 = topicService.saveOrUpdate(topic1);
        assertNotNull(savedTopic1, "Topic #1 not saved");

        Topic topic2 = new Topic(null,
                "Topic #2",
                "This is topic #2",
                null,
                new Timestamp(System.currentTimeMillis()),
                null,
                category);
        Topic savedTopic2 = topicService.saveOrUpdate(topic2);
        assertNotNull(savedTopic2, "Topic #2 not saved");

        Topic topic3 = new Topic(null,
                "Topic #3",
                "This is topic #3",
                null,
                new Timestamp(System.currentTimeMillis()),
                null,
                category);
        Topic savedTopic3 = topicService.saveOrUpdate(topic3);
        assertNotNull(savedTopic3, "Topic #3 not saved");

        StudentTopic studentTopic1 = new StudentTopic(null,
                savedUser,
                savedTopic1,
                new Timestamp(System.currentTimeMillis()),
                StudentTopic.STATUS_CANCELED,
                StudentTopic.REASON_STUDENT_CANCELED_APPROVED_TOPIC);
        StudentTopic savedStudentTopic1 = studentTopicService.saveOrUpdate(studentTopic1);
        assertNotNull(savedStudentTopic1, "Student-Topic #1 not saved");

        StudentTopic studentTopic2 = new StudentTopic(null,
                savedUser,
                savedTopic2,
                new Timestamp(System.currentTimeMillis()),
                StudentTopic.STATUS_APPROVED,
                null);
        StudentTopic savedStudentTopic2 = studentTopicService.saveOrUpdate(studentTopic2);
        assertNotNull(savedStudentTopic2, "Student-Topic #2 not saved");

        StudentTopic studentTopic3 = new StudentTopic(null,
                savedUser,
                savedTopic3,
                new Timestamp(System.currentTimeMillis()),
                StudentTopic.STATUS_DECLINED,
                StudentTopic.REASON_REQUEST_DECLINED);
        StudentTopic savedStudentTopic3 = studentTopicService.saveOrUpdate(studentTopic3);
        assertNotNull(savedStudentTopic3, "Student-Topic #3 not saved");

        List<Category> categoriesBefore = categoryService.findAll();
        assertEquals(1, categoriesBefore.size(), "Categories not saved");

        List<User> usersBefore = userService.findAll();
        assertEquals(1, usersBefore.size(), "Users not saved");

        List<Topic> topicsBefore = topicService.findAll();
        assertEquals(3, topicsBefore.size(), "Topics not saved");

        List<StudentTopic> studentTopicsBefore = studentTopicService.findAll();
        assertEquals(3, studentTopicsBefore.size(), "Student-Topics not saved");

        userService.deleteById(savedUser.getId());

        Optional<User> deletedUser = userService.findById(savedUser.getId());
        assertFalse(deletedUser.isPresent(), "User not deleted");

        List<Category> categories = categoryService.findAll();
        assertEquals(1, categories.size(), "Categories deleted or not retrieved correctly");

        List<Topic> topics = topicService.findAll();
        assertEquals(3, topics.size(), "Topics deleted or not retrieved correctly");

        List<StudentTopic> studentTopics = studentTopicService.findAll();
        assertTrue(studentTopics.isEmpty(), "Student-Topics not deleted");
    }

    @Test
    @DisplayName("When deleting a topic then the student-topic instances should be deleted")
    void allStudentTopicsShouldBeDeletedOnTopicDeleted() {
        Category category = new Category(null, Category.LICENSE);
        categoryService.saveOrUpdate(category);

        Topic topic = new Topic(null,
                "Topic #1",
                "This is topic #1",
                null,
                new Timestamp(System.currentTimeMillis()),
                null,
                category);
        Topic savedTopic = topicService.saveOrUpdate(topic);
        assertNotNull(savedTopic, "Topic not saved");

        User user1 = new User(null,
                "John1",
                "Doe1",
                "john.doe1@email.com",
                "pass",
                null,
                category);
        User savedUser1 = userService.saveOrUpdate(user1);
        assertNotNull(savedUser1, "User #1 not saved");

        User user2 = new User(null,
                "John2",
                "Doe2",
                "john.doe2@email.com",
                "pass",
                null,
                category);
        User savedUser2 = userService.saveOrUpdate(user2);
        assertNotNull(savedUser2, "User #2 not saved");

        User user3 = new User(null,
                "John3",
                "Doe3",
                "john.doe3@email.com",
                "pass",
                null,
                category);
        User savedUser3 = userService.saveOrUpdate(user3);
        assertNotNull(savedUser3, "User #3 not saved");

        StudentTopic studentTopic1 = new StudentTopic(null,
                user1,
                topic,
                new Timestamp(System.currentTimeMillis()),
                StudentTopic.STATUS_CANCELED,
                StudentTopic.REASON_STUDENT_CANCELED_APPROVED_TOPIC);
        StudentTopic savedStudentTopic1 = studentTopicService.saveOrUpdate(studentTopic1);
        assertNotNull(savedStudentTopic1, "Student-Topic #1 not saved");

        StudentTopic studentTopic2 = new StudentTopic(null,
                user2,
                topic,
                new Timestamp(System.currentTimeMillis()),
                StudentTopic.STATUS_APPROVED,
                null);
        StudentTopic savedStudentTopic2 = studentTopicService.saveOrUpdate(studentTopic2);
        assertNotNull(savedStudentTopic2, "Student-Topic #2 not saved");

        StudentTopic studentTopic3 = new StudentTopic(null,
                user3,
                topic,
                new Timestamp(System.currentTimeMillis()),
                StudentTopic.STATUS_DECLINED,
                StudentTopic.REASON_REQUEST_DECLINED);
        StudentTopic savedStudentTopic3 = studentTopicService.saveOrUpdate(studentTopic3);
        assertNotNull(savedStudentTopic3, "Student-Topic #3 not saved");

        List<Category> categoriesBefore = categoryService.findAll();
        assertEquals(1, categoriesBefore.size(), "Categories not saved");

        List<User> usersBefore = userService.findAll();
        assertEquals(3, usersBefore.size(), "Users not saved");

        List<Topic> topicsBefore = topicService.findAll();
        assertEquals(1, topicsBefore.size(), "Topics not saved");

        List<StudentTopic> studentTopicsBefore = studentTopicService.findAll();
        assertEquals(3, studentTopicsBefore.size(), "Student-Topics not saved");

        topicService.deleteById(savedTopic.getId());

        Optional<Topic> deletedTopic = topicService.findById(savedTopic.getId());
        assertFalse(deletedTopic.isPresent(), "Topic not deleted");

        List<User> users = userService.findAll();
        assertEquals(3, users.size(), "Users deleted or not retrieved correctly");

        List<Category> categories = categoryService.findAll();
        assertEquals(1, categories.size(), "Categories deleted or not retrieved correctly");

        List<StudentTopic> studentTopics = studentTopicService.findAll();
        assertTrue(studentTopics.isEmpty(), "Student-Topics not deleted");
    }

    @Test
    @DisplayName("When deleting an user then the topic-comments instances should be deleted")
    void allTopicCommentsShouldBeDeletedOnUserDeleted() {
        Category category = new Category(null, Category.LICENSE);
        categoryService.saveOrUpdate(category);

        User user = new User(null,
                "John",
                "Doe",
                "john.doe@email.com",
                "pass",
                null,
                category);
        User savedUser = userService.saveOrUpdate(user);
        assertNotNull(savedUser, "User not saved");

        Topic topic = new Topic(null,
                "Topic",
                "This is a topic",
                null,
                new Timestamp(System.currentTimeMillis()),
                null,
                category);
        Topic savedTopic = topicService.saveOrUpdate(topic);
        assertNotNull(savedTopic, "Topic not saved");

        TopicComment topicComment1 = new TopicComment(null,
                user,
                topic,
                "Comment #1",
                new Timestamp(System.currentTimeMillis()));
        TopicComment savedTopicComment1 = topicCommentService.saveOrUpdate(topicComment1);
        assertNotNull(savedTopicComment1, "Topic-Comment #1 not saved");

        TopicComment topicComment2 = new TopicComment(null,
                user,
                topic,
                "Comment #2",
                new Timestamp(System.currentTimeMillis()));
        TopicComment savedTopicComment2 = topicCommentService.saveOrUpdate(topicComment2);
        assertNotNull(savedTopicComment2, "Topic-Comment #2 not saved");

        TopicComment topicComment3 = new TopicComment(null,
                user,
                topic,
                "Comment #3",
                new Timestamp(System.currentTimeMillis()));
        TopicComment savedTopicComment3 = topicCommentService.saveOrUpdate(topicComment3);
        assertNotNull(savedTopicComment3, "Topic-Comment #3 not saved");

        List<Category> categoriesBefore = categoryService.findAll();
        assertEquals(1, categoriesBefore.size(), "Categories not saved");

        List<User> usersBefore = userService.findAll();
        assertEquals(1, usersBefore.size(), "Users not saved");

        List<Topic> topicsBefore = topicService.findAll();
        assertEquals(1, topicsBefore.size(), "Topics not saved");

        List<TopicComment> topicCommentsBefore = topicCommentService.findAll();
        assertEquals(3, topicCommentsBefore.size(), "Topic-Comments not saved");

        userService.deleteById(savedUser.getId());

        Optional<User> deletedUser = userService.findById(savedUser.getId());
        assertFalse(deletedUser.isPresent(), "User not deleted");

        List<Category> categories = categoryService.findAll();
        assertEquals(1, categories.size(), "Categories deleted or not retrieved correctly");

        List<Topic> topics = topicService.findAll();
        assertEquals(1, topics.size(), "Topics deleted or not retrieved correctly");

        List<TopicComment> topicComments = topicCommentService.findAll();
        assertTrue(topicComments.isEmpty(), "Topic-Comments not deleted");
    }

    @Test
    @DisplayName("When deleting a topic then the topic-comments instances should be deleted")
    void allTopicCommentsShouldBeDeletedOnTopicDeleted() {
        Category category = new Category(null, Category.LICENSE);
        categoryService.saveOrUpdate(category);

        User user = new User(null,
                "John",
                "Doe",
                "john.doe@email.com",
                "pass",
                null,
                category);
        User savedUser = userService.saveOrUpdate(user);
        assertNotNull(savedUser, "User not saved");

        Topic topic = new Topic(null,
                "Topic",
                "This is a topic",
                null,
                new Timestamp(System.currentTimeMillis()),
                null,
                category);
        Topic savedTopic = topicService.saveOrUpdate(topic);
        assertNotNull(savedTopic, "Topic not saved");

        TopicComment topicComment1 = new TopicComment(null,
                user,
                topic,
                "Comment #1",
                new Timestamp(System.currentTimeMillis()));
        TopicComment savedTopicComment1 = topicCommentService.saveOrUpdate(topicComment1);
        assertNotNull(savedTopicComment1, "Topic-Comment #1 not saved");

        TopicComment topicComment2 = new TopicComment(null,
                user,
                topic,
                "Comment #2",
                new Timestamp(System.currentTimeMillis()));
        TopicComment savedTopicComment2 = topicCommentService.saveOrUpdate(topicComment2);
        assertNotNull(savedTopicComment2, "Topic-Comment #2 not saved");

        TopicComment topicComment3 = new TopicComment(null,
                user,
                topic,
                "Comment #3",
                new Timestamp(System.currentTimeMillis()));
        TopicComment savedTopicComment3 = topicCommentService.saveOrUpdate(topicComment3);
        assertNotNull(savedTopicComment3, "Topic-Comment #3 not saved");

        List<Category> categoriesBefore = categoryService.findAll();
        assertEquals(1, categoriesBefore.size(), "Categories not saved");

        List<User> usersBefore = userService.findAll();
        assertEquals(1, usersBefore.size(), "Users not saved");

        List<Topic> topicsBefore = topicService.findAll();
        assertEquals(1, topicsBefore.size(), "Topics not saved");

        List<TopicComment> topicCommentsBefore = topicCommentService.findAll();
        assertEquals(3, topicCommentsBefore.size(), "Topic-Comments not saved");

        topicService.deleteById(savedTopic.getId());

        Optional<Topic> deletedTopic = topicService.findById(savedTopic.getId());
        assertFalse(deletedTopic.isPresent(), "Topic not deleted");

        List<Category> categories = categoryService.findAll();
        assertEquals(1, categories.size(), "Categories deleted or not retrieved correctly");

        List<User> users = userService.findAll();
        assertEquals(1, users.size(), "Users deleted or not retrieved correctly");

        List<TopicComment> topicComments = topicCommentService.findAll();
        assertTrue(topicComments.isEmpty(), "Topic-Comments not deleted");
    }
}