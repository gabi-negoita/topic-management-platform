package com.mtapo.app.service;

import com.mtapo.app.entity.Tag;
import com.mtapo.app.repository.TagRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class TagService {

    private final TagRepository repository;

    @Autowired
    public TagService(TagRepository repository) {
        this.repository = repository;
    }

    public Integer countUsesByName(String name) {
        log.debug("countUsesByName(" + name + ")");
        return repository.countUsesByName(name);
    }

    public Optional<Tag> findByName(String name) {
        log.debug("findByName(" + name + ")");
        return repository.findByName(name);
    }

    public List<Tag> findAll() {
        log.debug("findAll()");
        return repository.findAll();
    }

    public Set<Tag> findAllByUserId(Integer userId) {
        log.debug("findAllByUserId(" + userId + ")");
        return repository.findAllByUserId(userId);
    }

    public Tag saveOrUpdate(Tag tag) {
        log.debug("saveOrUpdate(" + tag + ")");
        return repository.save(tag);
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
