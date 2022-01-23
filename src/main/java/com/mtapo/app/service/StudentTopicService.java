package com.mtapo.app.service;

import com.mtapo.app.entity.StudentTopic;
import com.mtapo.app.entity.User;
import com.mtapo.app.repository.StudentTopicRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class StudentTopicService {

    private final StudentTopicRepository repository;

    @Autowired
    public StudentTopicService(StudentTopicRepository repository) {
        this.repository = repository;
    }

    public List<StudentTopic> findAll() {
        log.debug("findAll()");
        Page<StudentTopic> page = repository.findAll(Pageable.unpaged());
        return page.getContent();
    }

    public List<StudentTopic> findAllByUserIdAndStatusAndReason(Integer userId,
                                                                String status,
                                                                String reason) {
        return repository.findAllByUserIdAndStatusAndReason(userId, status, reason);
    }

    public Optional<StudentTopic> findById(Integer id) {
        log.debug("findById(" + id + ")");
        return repository.findById(id);
    }

    public Optional<StudentTopic> findByTopicIdAndUserId(Integer topicId,
                                                         Integer userId) {
        log.debug(String.format("findByTopicIdAndUserId(%s, %s)", topicId, userId));
        return repository.findByTopicIdAndUserId(topicId, userId);
    }

    public Optional<StudentTopic> findAssignedByUserId(Integer id) {
        log.debug(String.format("findAssignedByUserId(%s)", id));
        return repository.findAssignedByUserId(id);
    }

    public Optional<StudentTopic> findApprovedByUserId(Integer id) {
        log.debug(String.format("findApprovedByUserId(%s)", id));
        return repository.findApprovedByUserId(id);
    }

    public StudentTopic saveOrUpdate(StudentTopic studentTopic) {
        log.debug("saveOrUpdate(" + studentTopic + ")");
        return repository.save(studentTopic);
    }

    @Transactional
    public Iterable<StudentTopic> saveAll(List<StudentTopic> studentTopicList) {
        log.debug("saveAll(" + studentTopicList + ")");
        return repository.saveAll(studentTopicList);
    }

    public void deleteById(Integer id) {
        log.debug("deleteById(" + id + ")");
        repository.deleteById(id);
    }

    public void deleteAll() {
        log.debug("deleteAll()");
        repository.deleteAll();
    }

    public List<StudentTopic> findAllByUserId(Integer userId) {
        log.debug("findAllByUserId(" + userId + ")");
        return repository.findAllByUserId(userId);
    }

    public List<StudentTopic> findAllRequestsForTopicOfUser(User user,
                                                            String search,
                                                            Pageable pageable) {
        log.debug(String.format("findAllRequestsForTopicOfUser(%s, %s, %s)", user, search, pageable));
        return repository.findAllRequestsForTopicOfUser(user, search, pageable);
    }

}
