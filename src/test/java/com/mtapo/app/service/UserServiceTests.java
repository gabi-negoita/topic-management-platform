package com.mtapo.app.service;

import com.mtapo.app.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.TestPropertySource;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
@DisplayName("Given the user service")
class UserServiceTests {

    @Autowired
    UserService userService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    RoleService roleService;

    @Autowired
    TopicService topicService;

    @Autowired
    StudentTopicService studentTopicService;

    @BeforeEach
    void populateDataBase() {
        log.debug("Populating database...");

        Category license = new Category(null, Category.LICENSE);
        categoryService.saveOrUpdate(license);

        Category master = new Category(null, Category.MASTER);
        categoryService.saveOrUpdate(master);

        Role student = new Role(null, "student");
        roleService.saveOrUpdate(student);

        Role teacher = new Role(null, "teacher");
        roleService.saveOrUpdate(teacher);

        Set<Role> students = new HashSet<>();
        students.add(student);

        Set<Role> teachers = new HashSet<>();
        teachers.add(teacher);

        Set<Role> studentsAndTeachers = new HashSet<>();
        studentsAndTeachers.add(student);
        studentsAndTeachers.add(teacher);

        User user1 = new User(null,
                "John",
                "Cena",
                "john.cena@email.com",
                "pass",
                teachers,
                license);
        userService.saveOrUpdate(user1);

        User user2 = new User(null,
                "Mike",
                "Tyson",
                "mike.tyson@email.com",
                "pass",
                students,
                master);
        userService.saveOrUpdate(user2);

        User user3 = new User(null,
                "Leonardo",
                "DiCaprio",
                "leo.dic@email.com",
                "pass",
                studentsAndTeachers,
                license);
        userService.saveOrUpdate(user3);

        Topic topic = new Topic(null,
                "Topic",
                "This is a topic",
                null,
                new Timestamp(System.currentTimeMillis()),
                null,
                license);
        topicService.saveOrUpdate(topic);

        StudentTopic studentTopic1 = new StudentTopic(null,
                user1,
                topic,
                new Timestamp(System.currentTimeMillis()),
                StudentTopic.STATUS_APPROVED,
                null);
        studentTopicService.saveOrUpdate(studentTopic1);

        StudentTopic studentTopic2 = new StudentTopic(null,
                user2,
                topic,
                new Timestamp(System.currentTimeMillis()),
                StudentTopic.STATUS_APPROVED,
                null);
        studentTopicService.saveOrUpdate(studentTopic2);

        StudentTopic studentTopic3 = new StudentTopic(null,
                user3,
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
        roleService.deleteAll();
        categoryService.deleteAll();
    }

    @Test
    @DisplayName("When calling existsById on an existing user then the returned value should be true")
    void exists() {
        List<User> users = userService.findAll();

        boolean exists = userService.existsById(users.get(0).getId());
        assertTrue(exists, "Existing user not retrieved");

        exists = userService.existsById(999);
        assertFalse(exists, "Non-existing user retrieved");
    }

    @Test
    @DisplayName("When calling findAll then all users should be retrieved")
    void findAll() {
        List<User> users = userService.findAll();
        assertEquals(3, users.size(), "Users not retrieved correctly");
    }

    @Test
    @DisplayName("When calling findAllByNameOrEmail then all users with the specified keywords should be retrieved")
    void findAllByNameOrEmail() {
        Pageable paging = PageRequest.of(0, 5, Sort.by("lastName"));

        Page<User> users = userService.findAllByNameOrEmail(null, paging);
        assertEquals(3, users.getContent().size(), "Users not retrieved");

        Page<User> usersByEmptyString = userService.findAllByNameOrEmail(null, paging);
        assertEquals(3, usersByEmptyString.getContent().size(), "Users not retrieved");

        Page<User> usersByFirstName = userService.findAllByNameOrEmail("jOHN", paging);
        assertEquals(1, usersByFirstName.getContent().size(), "Users not retrieved");

        Page<User> usersByLastName = userService.findAllByNameOrEmail("cen", paging);
        assertEquals(1, usersByLastName.getContent().size(), "Users not retrieved");

        Page<User> usersByEmail = userService.findAllByNameOrEmail(".cen", paging);
        assertEquals(1, usersByEmail.getContent().size(), "Users not retrieved");
    }

    @Test
    @DisplayName("When calling findAllByTopicId then all users with the specified topic id should be retrieved")
    void findAllByTopicId() {

        List<Topic> topics = topicService.findAll();

        List<User> users = userService.findAllByTopicId(topics.get(0).getId());
        assertEquals(3, users.size(), "Users not retrieved");

        List<User> nonExistingUsers = userService.findAllByTopicId(999);
        assertTrue(nonExistingUsers.isEmpty(), "Non-existing users retrieved");
    }

    @Test
    @DisplayName("When calling findById then a user with the specified id should be retrieved")
    void findById() {
        List<User> users = userService.findAll();

        Optional<User> userOptional = userService.findById(users.get(0).getId());
        assertTrue(userOptional.isPresent(), "User not retrieved");

        Optional<User> nonExistingUser = userService.findById(999);
        assertFalse(nonExistingUser.isPresent(), "Non-existing user retrieved");
    }

    @Test
    @DisplayName("When calling findByEmail then the user with the specified email should be retrieved")
    void findByEmail() {
        String email = "john.cena@email.com";

        Optional<User> user = userService.findByEmail(email);
        assertTrue(user.isPresent(), "User not retrieved");

        Optional<User> nonExistingUser = userService.findByEmail("invalid@email.com");
        assertFalse(nonExistingUser.isPresent(), "Non-existing user retrieved");
    }

    @Test
    @DisplayName("When calling saveOrUpdate then the user should be saved/updated accordingly")
    void saveOrUpdate() {
        User newUser = new User(
                null,
                "new",
                "user",
                "new.user@email.com",
                "pass",
                null,
                null);

        User savedUser = userService.saveOrUpdate(newUser);
        assertNotNull(savedUser, "Retrieved saved user is null");

        List<User> usersAfterSave = userService.findAll();
        assertEquals(4, usersAfterSave.size(), "User not saved");

        savedUser.setEmail("john.doe@email.com");

        User updatedUser = userService.saveOrUpdate(savedUser);
        assertNotNull(savedUser, "Retrieved updated user is null");
        assertEquals("john.doe@email.com", updatedUser.getEmail(), "User email not updated");

        List<User> usersAfterUpdate = userService.findAll();
        assertEquals(4, usersAfterUpdate.size(), "User saved instead of updating");
    }

    @Test
    @DisplayName("When calling deleteAll then all users should be deleted")
    void deleteAll() {
        List<User> users = userService.findAll();
        assertFalse(users.isEmpty(), "Users not retrieved");

        userService.deleteAll();

        List<User> usersAfterDelete = userService.findAll();
        assertTrue(usersAfterDelete.isEmpty(), "Users not deleted");
    }

    @Test
    @DisplayName("When calling deleteById then the user with the specified id should be deleted")
    void deleteById() {
        List<User> users = userService.findAll();
        assertEquals(3, users.size(), "Users not retrieved");

        userService.deleteById(users.get(0).getId());

        Optional<User> userOptional = userService.findById(users.get(0).getId());
        assertFalse(userOptional.isPresent(), "User not deleted");

        List<User> usersAfterDelete = userService.findAll();
        assertEquals(2, usersAfterDelete.size(), "Users not retrieved");

        assertThrows(EmptyResultDataAccessException.class,
                () -> userService.deleteById(999),
                "Non-existing student-topic deleted");
    }

    @Test
    @DisplayName("When calling getCountByRoleId then the count of users with the specified role should be retrieved")
    void getCountByRoleId() {
        Optional<Role> student = roleService.findByName(Role.STUDENT);
        assertTrue(student.isPresent(), "Student role not retrieved");

        Integer studentCount = userService.getCountByRoleId(student.get().getId());
        assertEquals(2, studentCount, "Student count not retrieved correctly");

        Optional<Role> teacher = roleService.findByName(Role.TEACHER);
        assertTrue(teacher.isPresent(), "Teacher role not retrieved");

        Integer teacherCount = userService.getCountByRoleId(student.get().getId());
        assertEquals(2, teacherCount, "Teacher count not retrieved correctly");
    }

    @Test
    @DisplayName("When calling getCountByTopicIdAndStatus then the count of users that have the specified status at the specified topic should be retrieved")
    void getCountByTopicIdAndStatus() {

        Integer topicId = topicService.findAll().get(0).getId();
        Integer approvedCount = userService.getCountByTopicIdAndStatus(topicId, StudentTopic.STATUS_APPROVED);
        assertEquals(3, approvedCount, "Student were not retrieved correctly");

        Integer waitingCount = userService.getCountByTopicIdAndStatus(topicId, StudentTopic.STATUS_WAITING);
        assertEquals(0, waitingCount, "Student were not retrieved correctly");

        Integer canceledCount = userService.getCountByTopicIdAndStatus(topicId, StudentTopic.STATUS_CANCELED);
        assertEquals(0, canceledCount, "Student were not retrieved correctly");

        Integer declinedCount = userService.getCountByTopicIdAndStatus(topicId, StudentTopic.STATUS_DECLINED);
        assertEquals(0, declinedCount, "Student were not retrieved correctly");
    }

    @Test
    @DisplayName("When calling getUsersByRole then the list of users with the specified role should be retrieved")
    void getUsersByRole() {
        List<User> teachers = userService.getUsersByRole(Role.TEACHER);
        assertEquals(2, teachers.size(), "Teachers were not retrieved correctly");

        List<User> students = userService.getUsersByRole(Role.STUDENT);
        assertEquals(2, students.size(), "Students were not retrieved correctly");

        List<User> admins = userService.getUsersByRole(Role.ADMIN);
        assertEquals(0, admins.size(), "Admins were not retrieved correctly");
    }
}