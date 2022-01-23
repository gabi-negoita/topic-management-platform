package com.mtapo.app.service;

import com.mtapo.app.entity.Notification;
import com.mtapo.app.entity.User;
import com.mtapo.app.repository.NotificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class NotificationService {

    private final NotificationRepository repository;

    @Autowired
    public NotificationService(NotificationRepository repository) {
        this.repository = repository;
    }

    public List<Notification> findAll() {
        log.debug("findAll()");
        return repository.findAll();
    }

    public Optional<Notification> findById(Integer id) {
        log.debug("findById(" + id + ")");
        return repository.findById(id);
    }

    public Notification saveOrUpdate(Notification category) {
        log.debug("saveOrUpdate(" + category + ")");
        return repository.save(category);
    }

    public void deleteById(Integer id) {
        log.debug("deleteById(" + id + ")");
        repository.deleteById(id);
    }

    public void deleteAll() {
        log.debug("deleteAll()");
        repository.deleteAll();
    }

    public List<Notification> findRequestsFromStudentToTeacher(User user) {
        log.debug("findRequestsFromStudentToTeacher(" + user + ")");
        return repository.findRequestsFromStudentToTeacher(user);
    }

    public List<Notification> findRequestsFromTeacherToStudent(User user) {
        log.debug("findRequestsFromTeacherToStudent: " + user);
        return repository.findRequestsFromTeacherToStudent(user);
    }

    public List<Notification> findApplicationCancelFromStudentToTeacher(User user) {
        log.debug("findApplicationCancelFromStudentToTeacher(" + user + ")");
        return repository.findApplicationCancelFromStudentToTeacher(user);
    }

    public List<Notification> findCommentsForTopicsOfTeacher(User user) {
        log.debug("findCommentsForTopicsOfTeacher: " + user);
        return repository.findCommentsForTopicsOfTeacher(user);
    }

    public List<Notification> findCommentsForTopicOfStudent(User user) {
        log.debug("findCommentsForTopicOfStudent: " + user);
        return repository.findCommentsForTopicOfStudent(user);
    }
}
