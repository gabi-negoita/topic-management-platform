package com.mtapo.app.service;

import com.mtapo.app.entity.TopicComment;
import com.mtapo.app.repository.TopicCommentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TopicCommentService {
    private final TopicCommentRepository repository;

    @Autowired
    public TopicCommentService(TopicCommentRepository repository) {
        this.repository = repository;
    }

    public List<TopicComment> findAll() {
        log.debug("findAll()");
        return repository.findAll();
    }

    public Optional<TopicComment> findById(Integer id) {
        log.debug("findById(" + id + ")");
        return repository.findById(id);
    }

    public List<TopicComment> findAllByTopicId(Integer id) {
        log.debug("findByTopicId(" + id + ")");
        return repository.findAllByTopicId(id);
    }

    public List<TopicComment> findAllByUserId(Integer id) {
        log.debug("findAllByUserId(" + id + ")");
        return repository.findAllByUserId(id);
    }

    public TopicComment saveOrUpdate(TopicComment topicComment) {
        log.debug("saveOrUpdate(" + topicComment + ")");
        return repository.save(topicComment);
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
