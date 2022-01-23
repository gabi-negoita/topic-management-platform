package com.mtapo.app.service;

import com.mtapo.app.entity.Category;
import com.mtapo.app.entity.StudentTopic;
import com.mtapo.app.entity.Topic;
import com.mtapo.app.entity.User;
import com.mtapo.app.repository.TopicRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TopicService {

    private final TopicRepository repository;

    @Autowired
    public TopicService(TopicRepository repository) {
        this.repository = repository;
    }

    public List<Topic> findAll() {
        log.debug("findAll()");
        return repository.findAll();
    }

    public Page<Topic> findAll(Pageable paging) {
        log.debug("findAll(" + paging + ")");
        return repository.findAll(paging);
    }

    public Optional<Topic> findById(Integer id) {
        log.debug("findById(" + id + ")");
        return repository.findById(id);
    }

    public Optional<Topic> findByUser(User user) {
        log.debug("findByUser(" + user + ")");
        return repository.findTopicOfUser(user, StudentTopic.STATUS_APPROVED);
    }

    public Integer getCountByCategoryName(String categoryName) {
        return repository.getCountByCategoryName(categoryName);
    }

    public List<Topic> findAllByCategoryAndTags(String categoryName,
                                                List<String> tags) {

        if (categoryName == null || categoryName.isEmpty()) {
            categoryName = null;
        }

        if (tags == null || tags.isEmpty()) {
            tags = null;
        }

        log.debug(String.format("findAllByCategoryAndTags(%s, %s)", categoryName, tags));
        return repository.findAllByCategoryAndTags(categoryName, tags);
    }

    public List<Topic> findAllByCategoryUserAndTags(String categoryName,
                                                    Integer userId,
                                                    List<String> tags) {
        log.debug(String.format("findAllByCategoryUserAndTags(%s, %s, %s)", categoryName, userId, tags));
        return repository.findAllByCategoryUserAndTags(categoryName, userId, tags);
    }

    public List<Topic> findAllByCategoryAndUser(String categoryName,
                                                Integer userId) {
        log.debug(String.format("findAllByCategoryAndUser(%s, %s)", categoryName, userId));
        return repository.findAllByCategoryAndUser(categoryName, userId);
    }

    public List<Topic> findAllByCategory(Category category) {
        log.debug("findAllByCategory(" + category + ")");
        return this.repository.findAllByCategory(category);
    }

    public List<Topic> findAllByUserId(Integer userId) {
        log.debug("findAllByUserId(" + userId + ")");
        return this.repository.findAllByUserId(userId);
    }

    public Page<Topic> findAllByTeacherAndCategory(String teacher,
                                                   String category,
                                                   Pageable paging) {
        log.debug(String.format("findAllByTeacherAndCategory(%s, %s, %s)", teacher, category, paging));
        return repository.findAllByTeacherAndCategory(teacher, category, paging);
    }

    public Page<Topic> findAllByTeacherCategoryAndTags(String teacher,
                                                       String category,
                                                       List<String> tags,
                                                       Pageable paging) {
        log.debug(String.format("findAllByTeacherCategoryAndTags(%s, %s, %s, %s)", teacher, category, tags, paging));
        return repository.findAllByTeacherCategoryAndTags(teacher, category, tags, paging);
    }

    public boolean existsById(Integer id) {
        log.debug("existsById(" + id + ")");
        return repository.existsById(id);
    }

    public Topic saveOrUpdate(Topic topic) {
        log.debug("saveOrUpdate(" + topic + ")");
        return repository.save(topic);
    }

    public void deleteById(Integer id) {
        log.debug("deleteById(" + id + ")");
        repository.deleteById(id);
    }

    public void deleteAll() {
        log.debug("deleteAll()");
        repository.deleteAll();
    }

}
