package com.mtapo.app.service;

import com.mtapo.app.entity.Category;
import com.mtapo.app.entity.User;
import com.mtapo.app.repository.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CategoryService {

    private final CategoryRepository repository;

    @Autowired
    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    public List<Category> findAll() {
        log.debug("findAll()");
        return repository.findAll();
    }

    public Optional<Category> findById(Integer id) {
        log.debug("findById(" + id + ")");
        return repository.findById(id);
    }

    public Optional<Category> findByName(String name) {
        log.debug("findByName(" + name + ")");
        return repository.findByName(name);
    }

    public Optional<Category> findByUser(User user) {
        log.debug("findByUser(" + user + ")");
        return repository.findByUser(user);
    }

    public Category saveOrUpdate(Category category) {
        log.debug("saveOrUpdate(" + category + ")");
        return repository.save(category);
    }

    public void deleteAll() {
        log.debug("deleteAll()");
        repository.deleteAll();
    }
}
