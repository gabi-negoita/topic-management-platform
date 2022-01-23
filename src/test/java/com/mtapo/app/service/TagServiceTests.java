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
import org.springframework.test.context.TestPropertySource;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
@DisplayName("Given the category service")
class TagServiceTests {

    @Autowired
    TagService tagService;

    @Autowired
    TopicService topicService;

    @Autowired
    CategoryService categoryService;

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

        User user1 = new User(null,
                "1",
                "1",
                "1.1@1.com",
                "pass",
                null,
                master);
        userService.saveOrUpdate(user1);

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
                user1,
                license);
        topicService.saveOrUpdate(topic2);

        Topic topic3 = new Topic(null,
                "Topic #3",
                "This is the topic #3",
                new HashSet<>(Arrays.asList(app, mobile)),
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
}