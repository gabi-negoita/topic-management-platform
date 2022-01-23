package com.mtapo.app.service;

import com.mtapo.app.entity.Category;
import com.mtapo.app.entity.Tag;
import com.mtapo.app.entity.Topic;
import com.mtapo.app.entity.User;
import com.mtapo.app.repository.TagRepository;
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
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
@DisplayName("Given the topic service")
class TopicServiceTests {

    @Autowired
    CategoryService categoryService;

    @Autowired
    TopicService topicService;

    @Autowired
    UserService userService;

    @Autowired
    TagRepository topicTagRepository;

    @BeforeEach
    void populateDataBase() {
        log.debug("Populating database...");

        Category license = new Category(null, Category.LICENSE);
        categoryService.saveOrUpdate(license);

        Category master = new Category(null, Category.MASTER);
        categoryService.saveOrUpdate(master);

        Tag web = topicTagRepository.save(new Tag(null, "web"));
        Tag mobile = topicTagRepository.save(new Tag(null, "mobile"));
        Tag app = topicTagRepository.save(new Tag(null, "app"));

        User user = new User(null,
                "john",
                "doe",
                "john.doe@email.com",
                "pass",
                null,
                license);
        userService.saveOrUpdate(user);

        Topic topic1 = new Topic(null,
                "Topic #1",
                "This is the topic #1",
                new HashSet<>(Arrays.asList(web, app)),
                new Timestamp(System.currentTimeMillis()),
                user,
                license);
        topicService.saveOrUpdate(topic1);

        Topic topic2 = new Topic(null,
                "Topic #2",
                "This is the topic #2",
                new HashSet<>(Arrays.asList(web, mobile)),
                new Timestamp(System.currentTimeMillis()),
                user,
                license);
        topicService.saveOrUpdate(topic2);

        Topic topic3 = new Topic(null,
                "Topic #3",
                "This is the topic #3",
                new HashSet<>(Collections.singletonList(mobile)),
                new Timestamp(System.currentTimeMillis()),
                user,
                master);
        topicService.saveOrUpdate(topic3);
    }

    @AfterEach
    void flushDataBase() {
        log.debug("Flushing database...");

        topicService.deleteAll();
        userService.deleteAll();
        categoryService.deleteAll();
    }

    @Test
    @DisplayName("When calling existsById on an existing topic then the returned value should be true")
    void exists() {
        List<Topic> topics = topicService.findAll();

        boolean exists = topicService.existsById(topics.get(0).getId());
        assertTrue(exists, "User doesn't exists when it should");

        boolean doesntExist = topicService.existsById(999);
        assertFalse(doesntExist, "User exists when it shouldn't");
    }

    @Test
    @DisplayName("When calling findAll then all topics should be retrieved")
    void findAll() {
        List<Topic> topics = topicService.findAll();
        assertEquals(3, topics.size(), "Topics not retrieved");
    }

    @Test
    @DisplayName("When calling findById then a topic with the specified id should be retrieved")
    void findById() {
        List<Topic> topics = topicService.findAll();

        Optional<Topic> topicOptional = topicService.findById(topics.get(0).getId());
        assertTrue(topicOptional.isPresent(), "Topic not retrieved");

        Optional<Topic> nonExistingTopic = topicService.findById(999);
        assertFalse(nonExistingTopic.isPresent(), "Non-existing topic retrieved");
    }

    @Test
    @DisplayName("When calling findAllByUserId then all topics with the specified used id should be retrieved")
    void findAllByUserId() {
        List<User> users = userService.findAll();

        List<Topic> topics = topicService.findAllByUserId(users.get(0).getId());
        assertEquals(3, topics.size(), "Topics not retrieved");

        List<Topic> nonExistingTopics = topicService.findAllByUserId(999);
        assertTrue(nonExistingTopics.isEmpty(), "Non-existing topics retrieved");
    }

    @Test
    @DisplayName("When calling findAllByUserId then all topics with the specified fields should be retrieved")
    void findAllByCategoryAndTags() {
        List<String> tags = new ArrayList<>();

        List<Topic> all = topicService.findAllByCategoryAndTags(null, tags);
        assertEquals(3, all.size(), "Topics not retrieved correctly");

        List<Topic> allByLicense = topicService.findAllByCategoryAndTags(Category.LICENSE, tags);
        assertEquals(2, allByLicense.size(), "Topics not retrieved correctly");

        List<Topic> allByMaster = topicService.findAllByCategoryAndTags(Category.MASTER, tags);
        assertEquals(1, allByMaster.size(), "Topics not retrieved correctly");

        tags.add("app");
        List<Topic> allByApp = topicService.findAllByCategoryAndTags(null, tags);
        assertEquals(1, allByApp.size(), "Topics not retrieved correctly");

        tags.remove("app");
        tags.add("mobile");
        List<Topic> allByMobile = topicService.findAllByCategoryAndTags(null, tags);
        assertEquals(2, allByMobile.size(), "Topics not retrieved correctly");

        tags.add("web");
        List<Topic> allByWebAndMobile = topicService.findAllByCategoryAndTags(null, tags);
        assertEquals(3, allByWebAndMobile.size(), "Topics not retrieved correctly");
    }

    @Test
    @DisplayName("When calling findByCategoryUserAndTags then all topics with the specified fields should be retrieved")
    void findAllByCategoryUserAndTags() {
        Optional<User> user = userService.findByEmail("john.doe@email.com");
        assertTrue(user.isPresent(), "User not retrieved");

        List<String> tags = new ArrayList<>();

        tags.add("app");
        List<Topic> allByApp = topicService.findAllByCategoryUserAndTags(null, user.get().getId(), tags);
        assertEquals(1, allByApp.size(), "Topics not retrieved correctly");

        tags.remove("app");
        tags.add("mobile");
        List<Topic> allByMobile = topicService.findAllByCategoryUserAndTags(null, user.get().getId(), tags);
        assertEquals(2, allByMobile.size(), "Topics not retrieved correctly");

        tags.add("web");
        List<Topic> allByWebAndMobile = topicService.findAllByCategoryUserAndTags(null, user.get().getId(), tags);
        assertEquals(3, allByWebAndMobile.size(), "Topics not retrieved correctly");
    }

    @Test
    @DisplayName("When calling findAllByTeacherAndCategory then all topics with the specified fields should be retrieved")
    void findAllByTeacherAndCategory() {
        Pageable paging = PageRequest.of(0, 5, Sort.by("createdDate"));

        String category = Category.MASTER;
        String teacher = "john doe";

        Page<Topic> allTopics = topicService.findAllByTeacherAndCategory(null, null, paging);
        assertEquals(3, allTopics.getTotalElements(), "Topics not retrieved correctly");

        Page<Topic> allByCategory = topicService.findAllByTeacherAndCategory(null, category, paging);
        assertEquals(1, allByCategory.getTotalElements(), "Topics not retrieved correctly");

        Page<Topic> allByTeacher = topicService.findAllByTeacherAndCategory(teacher, null, paging);
        assertEquals(3, allByTeacher.getTotalElements(), "Topics not retrieved correctly");

        Page<Topic> allByTeacherAndCategory = topicService.findAllByTeacherAndCategory(teacher, category, paging);
        assertEquals(1, allByTeacherAndCategory.getTotalElements(), "Topics not retrieved correctly");
    }

    @Test
    @DisplayName("When calling findAllByTeacherCategoryAndTags then all topics with the specified fields should be retrieved")
    void findAllByTeacherCategoryAndTags() {
        Pageable paging = PageRequest.of(0, 5, Sort.by("createdDate"));
        List<String> tags = new ArrayList<>();

        tags.add("app");
        Page<Topic> allByApp = topicService.findAllByTeacherCategoryAndTags(null, null, tags, paging);
        assertEquals(1, allByApp.getTotalElements(), "Topics not retrieved correctly");

        tags.remove("app");
        tags.add("mobile");
        Page<Topic> allByMobile = topicService.findAllByTeacherCategoryAndTags(null, null, tags, paging);
        assertEquals(2, allByMobile.getTotalElements(), "Topics not retrieved correctly");

        tags.add("web");
        Page<Topic> allByWebAndMobile = topicService.findAllByTeacherCategoryAndTags(null, null, tags, paging);
        assertEquals(3, allByWebAndMobile.getTotalElements(), "Topics not retrieved correctly");
    }

    @Test
    @DisplayName("When calling findAllByCategory then all topic within the specified category should be retrieved")
    void findByCategory() {
        List<Category> categories = categoryService.findAll();

        Optional<Category> master = categories.stream()
                .filter(category -> category.getName().equals(Category.MASTER))
                .findFirst();
        assertTrue(master.isPresent(), "Category not retrieved");

        List<Topic> masterTopics = topicService.findAllByCategory(master.get());
        masterTopics.forEach(topic -> assertEquals(Category.MASTER, topic.getCategory().getName(), "Topic not retrieved correctly"));

        Optional<Category> license = categories.stream()
                .filter(category -> category.getName().equals(Category.LICENSE))
                .findFirst();
        assertTrue(license.isPresent(), "Category not retrieved");

        List<Topic> licenseTopics = topicService.findAllByCategory(license.get());
        licenseTopics.forEach(topic -> assertEquals(Category.LICENSE, topic.getCategory().getName(), "Topic not retrieved correctly"));
    }

    @Test
    @DisplayName("When calling saveOrUpdate then the topic should be saved/updated accordingly")
    void saveOrUpdate() {
        List<Category> categories = categoryService.findAll();
        Topic newTopic = new Topic(null,
                "new",
                "topic",
                null,
                new Timestamp(System.currentTimeMillis()),
                null,
                categories.get(0));

        Topic savedTopic = topicService.saveOrUpdate(newTopic);
        assertNotNull(savedTopic, "Retrieved saved topic is null");

        List<Topic> topicsAfterSave = topicService.findAll();
        assertEquals(4, topicsAfterSave.size(), "Topic not saved");

        savedTopic.setTitle("Topic #5");

        Topic updatedTopic = topicService.saveOrUpdate(savedTopic);
        assertNotNull(updatedTopic, "Retrieved updated topic is null");
        assertEquals("Topic #5", updatedTopic.getTitle(), "Topic title not updated");

        List<Topic> topicsAfterUpdate = topicService.findAll();
        assertEquals(4, topicsAfterUpdate.size(), "Topic saved instead of updating");
    }

    @Test
    @DisplayName("When calling deleteAll then all topics should be deleted")
    void deleteAll() {
        List<Topic> topics = topicService.findAll();
        assertFalse(topics.isEmpty(), "Topic not retrieved");

        topicService.deleteAll();

        List<Topic> topicsAfterDelete = topicService.findAll();
        assertTrue(topicsAfterDelete.isEmpty(), "Topics not deleted");
    }

    @Test
    @DisplayName("When calling deleteById then the topic with the specified id should be deleted")
    void deleteById() {
        List<Topic> topics = topicService.findAll();
        assertEquals(3, topics.size(), "Topics not retrieved");

        topicService.deleteById(topics.get(0).getId());

        Optional<Topic> topic = topicService.findById(topics.get(0).getId());
        assertFalse(topic.isPresent(), "Topic not deleted");

        List<Topic> topicsAfterDelete = topicService.findAll();
        assertEquals(2, topicsAfterDelete.size(), "Topic not deleted");

        assertThrows(EmptyResultDataAccessException.class,
                () -> topicService.deleteById(999),
                "Non-existing topic deleted");
    }
}