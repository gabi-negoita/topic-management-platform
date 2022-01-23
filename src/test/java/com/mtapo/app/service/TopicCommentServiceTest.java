package com.mtapo.app.service;

import com.mtapo.app.entity.Category;
import com.mtapo.app.entity.Topic;
import com.mtapo.app.entity.TopicComment;
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
@DisplayName("Given the topic-comment service")
class TopicCommentServiceTest {

    @Autowired
    TopicCommentService topicCommentService;

    @Autowired
    UserService userService;

    @Autowired
    TopicService topicService;

    @Autowired
    CategoryService categoryService;

    @BeforeEach
    void populateDataBase() {
        log.debug("Populating database...");

        Category category = new Category(null, Category.LICENSE);
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

        TopicComment topicComment1 = new TopicComment(null,
                user,
                topic,
                "Comment #1",
                new Timestamp(System.currentTimeMillis()));
        topicCommentService.saveOrUpdate(topicComment1);

        TopicComment topicComment2 = new TopicComment(null,
                user,
                topic,
                "Comment #2",
                new Timestamp(System.currentTimeMillis()));
        topicCommentService.saveOrUpdate(topicComment2);

        TopicComment topicComment3 = new TopicComment(null,
                user,
                topic,
                "Comment #3",
                new Timestamp(System.currentTimeMillis()));
        topicCommentService.saveOrUpdate(topicComment3);
    }

    @AfterEach
    void flushDataBase() {
        log.debug("Flushing database...");

        topicCommentService.deleteAll();
        topicService.deleteAll();
        userService.deleteAll();
        categoryService.deleteAll();
    }

    @Test
    void findAll() {
        List<TopicComment> topicComments = topicCommentService.findAll();
        assertEquals(3, topicComments.size(), "Topic-Comments not retrieved");
    }

    @Test
    void findById() {
        List<TopicComment> topicComments = topicCommentService.findAll();

        Optional<TopicComment> topicComment = topicCommentService.findById(topicComments.get(0).getId());
        assertTrue(topicComment.isPresent(), "Topic-Comment not retrieved");

        Optional<TopicComment> nonExistingTopicComment = topicCommentService.findById(999);
        assertFalse(nonExistingTopicComment.isPresent(), "Non-existing topic-comment retrieved");
    }

    @Test
    void findAllByTopicId() {
        List<TopicComment> topicComment = topicCommentService.findAllByTopicId(1);
        assertTrue(topicComment.isEmpty(), "Topic-Comment not retrieved");

        List<TopicComment> nonExistingTopicComment = topicCommentService.findAllByTopicId(999);
        assertTrue(nonExistingTopicComment.isEmpty(), "Non-existing topic-comment retrieved");
    }

    @Test
    void findAllByUserId() {
        List<TopicComment> topicComment = topicCommentService.findAllByUserId(1);
        assertTrue(topicComment.isEmpty(), "Topic-Comment not retrieved");

        List<TopicComment> nonExistingTopicComment = topicCommentService.findAllByUserId(9);
        assertTrue(nonExistingTopicComment.isEmpty(), "Non-existing topic-comment retrieved");
    }

    @Test
    void saveOrUpdate() {
        TopicComment newTopicComment = new TopicComment(null,
                null,
                null,
                "New topic comment",
                new Timestamp(System.currentTimeMillis()));

        TopicComment savedTopicComment = topicCommentService.saveOrUpdate(newTopicComment);
        assertNotNull(savedTopicComment, "Topic-Comment not saved");

        List<TopicComment> topicCommentsAfterSave = topicCommentService.findAll();
        assertEquals(4, topicCommentsAfterSave.size(), "Topic-Comment not saved");

        savedTopicComment.setComment("Updated comment");

        TopicComment updatedTopicComment = topicCommentService.saveOrUpdate(savedTopicComment);
        assertNotNull(updatedTopicComment, "Topic-Comment not updated");
        assertEquals("Updated comment", updatedTopicComment.getComment(), "Topic-Comment comment not updated");

        List<TopicComment> topicCommentsAfterUpdate = topicCommentService.findAll();
        assertEquals(4, topicCommentsAfterUpdate.size(), "Topic-Comment saved instead of updating");
    }

    @Test
    void deleteAll() {
        List<TopicComment> topicComments = topicCommentService.findAll();
        assertFalse(topicComments.isEmpty(), "Topic-Comments not retrieved");

        topicCommentService.deleteAll();

        List<TopicComment> topicCommentsAfterDelete = topicCommentService.findAll();
        assertTrue(topicCommentsAfterDelete.isEmpty(), "Topic-Comments not retrieved");
    }

    @Test
    void deleteById() {
        List<TopicComment> topicComments = topicCommentService.findAll();
        assertEquals(3, topicComments.size(), "Topic-Comments not retrived");

        topicCommentService.deleteById(topicComments.get(0).getId());

        Optional<TopicComment> topicCommentOptional = topicCommentService.findById(topicComments.get(0).getId());
        assertFalse(topicCommentOptional.isPresent(), "Topic-Comment not deleted");

        List<TopicComment> topicCommentsAfterDelete = topicCommentService.findAll();
        assertEquals(2, topicCommentsAfterDelete.size(), "Topic-Comments not retrived");

        assertThrows(EmptyResultDataAccessException.class,
                () -> topicCommentService.deleteById(999),
                "Non-existing student-topic deleted");
    }

}