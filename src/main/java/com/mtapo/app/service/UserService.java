package com.mtapo.app.service;

import com.mtapo.app.entity.User;
import com.mtapo.app.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserService {

    private final UserRepository repository;

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public boolean existsById(Integer id) {
        log.debug("existsById(" + id + ")");
        return repository.existsById(id);
    }

    public List<User> findAll() {
        log.debug("findAll()");
        return repository.findAll();
    }

    public List<User> findAllByTopicId(Integer id) {
        log.debug("findAllByTopicId(" + id + ")");
        return repository.findAllByTopicId(id);
    }

    public Integer getCountByTopicIdAndStatus(Integer id,
                                              String status) {
        log.debug("getCountByTopicIdAndStatus(" + id + ", " + status + ")");
        return repository.getCountByTopicIdAndStatus(id, status);
    }

    public Page<User> findAllByNameOrEmail(String keywords,
                                           Pageable paging) {
        log.debug(String.format("findAllByNameOrEmail(%s, %s)", keywords, paging));
        return repository.findAllByNameOrEmail(
                keywords, paging);
    }

    public Optional<User> findById(Integer id) {
        log.debug("findById(" + id + ")");
        return repository.findById(id);
    }

    public Optional<User> findByEmail(String email) {
        log.debug("findByEmail(" + email + ")");
        return repository.findByEmail(email);
    }

    public List<String> getActivityDatesByUserId(Integer id) {
        log.debug("getActivityDatesByUserId(" + id + ")");
        return repository.getActivityDatesByUserId(id);
    }

    public List<Integer> getActivityActionsByUserId(Integer id) {
        log.debug("getActivityActionsByUserId(" + id + ")");
        return repository.getActivityActionsByUserId(id);
    }

    public User saveOrUpdate(User user) {
        log.debug("saveOrUpdate(" + user + ")");
        return repository.save(user);
    }

    public void deleteById(Integer id) {
        log.debug("deleteById(" + id + ")");
        repository.deleteById(id);
    }

    public void deleteAll() {
        log.debug("deleteAll()");
        repository.deleteAll();
    }

    public Integer getCountByRoleId(Integer roleId) {
        log.debug("getCountByRoleId(" + roleId + ")");
        return repository.getCountByRoleName(roleId);
    }

    public List<User> getUsersByRole(String role) {
        log.debug("getUsersByRole(" + role + ")");
        return repository.getUsersByRole(role);
    }
}
