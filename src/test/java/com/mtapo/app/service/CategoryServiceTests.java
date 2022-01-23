package com.mtapo.app.service;

import com.mtapo.app.entity.Category;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
@DisplayName("Given the category service")
class CategoryServiceTests {

    @Autowired
    CategoryService categoryService;

    @BeforeEach
    void populateDataBase() {
        log.debug("Populating database...");

        Category license = new Category(null, "license");
        Category master = new Category(null, "master");

        categoryService.saveOrUpdate(license);
        categoryService.saveOrUpdate(master);
    }

    @AfterEach
    void flushDataBase() {
        log.debug("Flushing database...");

        categoryService.deleteAll();
    }

    @Test
    @DisplayName("When calling findAll then all categories should be retrieved")
    void findAll() {
        List<Category> categories = categoryService.findAll();
        assertEquals(2, categories.size(), "Categories not retrieved correctly");
    }

    @Test
    @DisplayName("When calling findById then a category with the specified id should be retrieved")
    void findById() {
        List<Category> categories = categoryService.findAll();

        Optional<Category> categoryOptional = categoryService.findById(categories.get(0).getId());
        assertTrue(categoryOptional.isPresent(), "Category not retrieved");

        Optional<Category> nonExistingCategory = categoryService.findById(999);
        assertFalse(nonExistingCategory.isPresent(), "Non-existing category retrieved");
    }

    @Test
    @DisplayName("When calling findByName then a category with the specified name should be retrieved")
    void findByName() {
        Optional<Category> categoryOptional = categoryService.findByName(Category.LICENSE);
        assertTrue(categoryOptional.isPresent(), "Category not retrieved");

        Category category = categoryOptional.get();
        assertEquals(Category.LICENSE, category.getName(), "Category name not retrieved correctly");

        Optional<Category> invalidCategory = categoryService.findByName("Invalid");
        assertFalse(invalidCategory.isPresent(), "Non-existing category retrieved");
    }

    @Test
    @DisplayName("When calling saveOrUpdate then the category should be saved/updated accordingly")
    void saveOrUpdate() {
        Category newCategory = new Category(
                null,
                "new");

        Category savedCategory = categoryService.saveOrUpdate(newCategory);
        assertNotNull(savedCategory, "Retrieved saved category is null");

        List<Category> categoriesAfterSave = categoryService.findAll();
        assertEquals(3, categoriesAfterSave.size(), "Category not saved");

        savedCategory.setName("New Category");

        Category updatedCategory = categoryService.saveOrUpdate(savedCategory);
        assertNotNull(updatedCategory, "Retrieved updated category is null");
        assertEquals("New Category", updatedCategory.getName(), "Category name not updated");

        List<Category> categoriesAfterUpdate = categoryService.findAll();
        assertEquals(3, categoriesAfterUpdate.size(), "Category saved instead of updating");
    }

    @Test
    @DisplayName("When calling deleteAll then all categories should be deleted")
    void deleteAll() {
        List<Category> categories = categoryService.findAll();
        assertFalse(categories.isEmpty(), "Categories not retrieved");

        categoryService.deleteAll();

        List<Category> categoriesAfterDelete = categoryService.findAll();
        assertTrue(categoriesAfterDelete.isEmpty(), "Categories not deleted");
    }
}