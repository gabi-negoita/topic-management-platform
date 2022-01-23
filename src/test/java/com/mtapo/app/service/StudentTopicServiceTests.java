package com.mtapo.app.service;

import com.mtapo.app.entity.Category;
import com.mtapo.app.entity.StudentTopic;
import com.mtapo.app.entity.Topic;
import com.mtapo.app.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.TestPropertySource;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
@DisplayName("Given the student-topic service")
class StudentTopicServiceTests {

    @Autowired
    CategoryService categoryService;

    @Autowired
    UserService userService;

    @Autowired
    TopicService topicService;

    @Autowired
    StudentTopicService studentTopicService;

    @BeforeEach
    void populateDataBase() {
        log.debug("Populating database...");

        Category category = new Category(null, "category");
        categoryService.saveOrUpdate(category);

        User user = new User(null,
                "John",
                "Doe",
                "john.doe@email.com",
                "pass",
                null,
                category);
        userService.saveOrUpdate(user);

        Topic topic = new Topic(null,
                "Topic",
                "This is a topic",
                null,
                new Timestamp(System.currentTimeMillis()),
                null,
                category);
        topicService.saveOrUpdate(topic);

        StudentTopic studentTopic1 = new StudentTopic(null,
                user,
                topic,
                new Timestamp(System.currentTimeMillis()),
                StudentTopic.STATUS_CANCELED,
                StudentTopic.REASON_STUDENT_CANCELED_APPROVED_TOPIC);
        studentTopicService.saveOrUpdate(studentTopic1);

        StudentTopic studentTopic2 = new StudentTopic(null,
                user,
                topic,
                new Timestamp(System.currentTimeMillis()),
                StudentTopic.STATUS_WAITING,
                null);
        studentTopicService.saveOrUpdate(studentTopic2);

        StudentTopic studentTopic3 = new StudentTopic(null,
                user,
                topic,
                new Timestamp(System.currentTimeMillis()),
                StudentTopic.STATUS_APPROVED,
                null);
        studentTopicService.saveOrUpdate(studentTopic3);
    }

    @AfterEach
    void flushDataBase() {
        log.debug("Flushing database...");

        studentTopicService.deleteAll();
        topicService.deleteAll();
        userService.deleteAll();
        categoryService.deleteAll();
    }

    @Test
    @DisplayName("When calling findAll then all student-topic instances should be retrieved")
    void findAll() {
        List<StudentTopic> studentTopics = studentTopicService.findAll();
        assertEquals(3, studentTopics.size(), "Student-Topics not retrieved");
    }

    @Test
    @DisplayName("When calling findById then the student-topic with the specified id should be retrieved")
    void findById() {
        List<StudentTopic> studentTopics = studentTopicService.findAll();

        Optional<StudentTopic> studentTopicOptional = studentTopicService.findById(studentTopics.get(0).getId());
        assertTrue(studentTopicOptional.isPresent(), "Student-Topic not retrieved");

        Optional<StudentTopic> nonExistingStudentTopic = studentTopicService.findById(999);
        assertFalse(nonExistingStudentTopic.isPresent(), "Non-existing student-topic retrieved");
    }

    @Test
    @DisplayName("When calling findAssingedByUserId then the student-topic with the specified id should be retrieved")
    void findAssignedByUserId() {
        Optional<User> userOptional = userService.findByEmail("john.doe@email.com");
        assertTrue(userOptional.isPresent(), "User not retrieved");

        Optional<StudentTopic> studentTopicOptional = studentTopicService.findAssignedByUserId(userOptional.get().getId());
        assertTrue(studentTopicOptional.isPresent(), "Student-Topic not retrieved");
    }

    @Test
    @DisplayName("When calling saveOrUpdate then the student-topic should be saved/updated accordingly")
    void saveOrUpdate() {
        Optional<User> user = userService.findByEmail("john.doe@email.com");
        assertTrue(user.isPresent(), "User not retrieved");

        List<Topic> topics = topicService.findAll();

        StudentTopic newStudentTopic = new StudentTopic(null,
                user.get(),
                topics.get(0),
                new Timestamp(System.currentTimeMillis()),
                StudentTopic.STATUS_WAITING,
                null);

        StudentTopic savedStudentTopic = studentTopicService.saveOrUpdate(newStudentTopic);
        assertNotNull(savedStudentTopic, "Retrieved saved student-topic is null");

        List<StudentTopic> studentTopicsAfterSave = studentTopicService.findAll();
        assertEquals(4, studentTopicsAfterSave.size(), "Student-Topic not saved");

        savedStudentTopic.setStatus(StudentTopic.STATUS_APPROVED);

        StudentTopic updatedStudentTopic = studentTopicService.saveOrUpdate(savedStudentTopic);
        assertNotNull(updatedStudentTopic, "Retrieved updated student-topic is null");
        assertEquals(StudentTopic.STATUS_APPROVED, updatedStudentTopic.getStatus(), "Student-Topic status not updated");

        List<StudentTopic> studentTopicsAfterUpdate = studentTopicService.findAll();
        assertEquals(4, studentTopicsAfterUpdate.size(), "Student-Topic saved instead of updating");
    }

    @Test
    @DisplayName("When calling deleteAll then all student-topic instances should be deleted")
    void deleteAll() {
        List<StudentTopic> studentTopics = studentTopicService.findAll();
        assertFalse(studentTopics.isEmpty(), "Student-Topics not retrieved");

        studentTopicService.deleteAll();

        List<StudentTopic> studentTopicsAfterDelete = studentTopicService.findAll();
        assertTrue(studentTopicsAfterDelete.isEmpty(), "Student-Topics not deleted");
    }

    @Test
    @DisplayName("When calling deleteById then the student-topic with the specified id should be deleted")
    void deleteById() {
        List<StudentTopic> studentTopics = studentTopicService.findAll();
        assertEquals(3, studentTopics.size(), "Student-Topics not retrieved");

        studentTopicService.deleteById(studentTopics.get(0).getId());

        Optional<StudentTopic> studentTopicOptional = studentTopicService.findById(studentTopics.get(0).getId());
        assertFalse(studentTopicOptional.isPresent(), "Student-Topic not deleted");

        List<StudentTopic> studentTopicsAfterDelete = studentTopicService.findAll();
        assertEquals(2, studentTopicsAfterDelete.size(), "Student-Topic not deleted");

        assertThrows(EmptyResultDataAccessException.class,
                () -> studentTopicService.deleteById(999),
                "Non-existing student-topic deleted");
    }

    @Test
    @DisplayName("When calling findAllByUserId then all Topics within the specified user should be retrieved")
    void findAllByUserId() {
        Optional<User> userOptional = userService.findByEmail("john.doe@email.com");
        assertTrue(userOptional.isPresent(), "User not retrieved");

        List<StudentTopic> studentTopics = studentTopicService.findAllByUserId(userOptional.get().getId());

        assertEquals(3, studentTopics.size(), "Student-Topics not retrieved");
    }
}
