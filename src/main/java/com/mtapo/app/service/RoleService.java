package com.mtapo.app.service;

import com.mtapo.app.entity.Role;
import com.mtapo.app.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class RoleService {

    private final RoleRepository repository;

    @Autowired
    public RoleService(RoleRepository repository) {
        this.repository = repository;
    }

    public List<Role> findAll() {
        log.debug("findAll()");
        return repository.findAll();
    }

    public Optional<Role> findById(Integer id) {
        log.debug("findById(" + id + ")");
        return repository.findById(id);
    }

    public Optional<Role> findByName(String name) {
        log.debug("findByName(" + name + ")");
        return repository.findByName(name);
    }

    public Role saveOrUpdate(Role role) {
        log.debug("saveOrUpdate(" + role + ")");
        return repository.save(role);
    }

    public void deleteAll() {
        log.debug("deleteAll()");
        repository.deleteAll();
    }
}
