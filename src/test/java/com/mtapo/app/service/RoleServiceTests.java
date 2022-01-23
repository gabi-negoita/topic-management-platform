package com.mtapo.app.service;

import com.mtapo.app.entity.Role;
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
@DisplayName("Given the role service")
class RoleServiceTests {

    @Autowired
    RoleService roleService;

    @BeforeEach
    void populateDataBase() {
        log.debug("Populating database...");

        Role student = new Role(null, Role.STUDENT);
        Role teacher = new Role(null, Role.TEACHER);
        Role admin = new Role(null, Role.ADMIN);

        roleService.saveOrUpdate(student);
        roleService.saveOrUpdate(teacher);
        roleService.saveOrUpdate(admin);
    }

    @AfterEach
    void flushDataBase() {
        log.debug("Flushing database...");

        roleService.deleteAll();
    }

    @Test
    @DisplayName("When calling findAll then all roles should be retrieved")
    void findAll() {
        List<Role> roles = roleService.findAll();
        assertEquals(3, roles.size(), "Roles not retrieved correctly");
    }

    @Test
    @DisplayName("When calling findById then the role with the specified id should be retrieved")
    void findById() {
        List<Role> roles = roleService.findAll();

        Optional<Role> roleOptional = roleService.findById(roles.get(0).getId());
        assertTrue(roleOptional.isPresent(), "Role not retrieved");

        Optional<Role> nonExistingRole = roleService.findById(999);
        assertFalse(nonExistingRole.isPresent(), "Non-existing role retrieved");
    }

    @Test
    @DisplayName("When calling findByName then the role with the specified name should be retrieved")
    void findByName() {
        Optional<Role> studentOptional = roleService.findByName(Role.STUDENT);
        assertTrue(studentOptional.isPresent(), "Role not retrieved");
        assertEquals(Role.STUDENT, studentOptional.get().getName(), "Student role not retrieved correctly");

        Optional<Role> teacherOptional = roleService.findByName(Role.TEACHER);
        assertTrue(teacherOptional.isPresent(), "Role not retrieved");
        assertEquals(Role.TEACHER, teacherOptional.get().getName(), "Teacher role not retrieved correctly");

        Optional<Role> adminOptional = roleService.findByName(Role.ADMIN);
        assertTrue(adminOptional.isPresent(), "Role not retrieved");
        assertEquals(Role.ADMIN, adminOptional.get().getName(), "Admin role not retrieved correctly");
    }

    @Test
    @DisplayName("When calling saveOrUpdate then the role should be saved/updated accordingly")
    void saveOrUpdate() {
        Role newRole = new Role(
                null,
                "new");

        Role savedRole = roleService.saveOrUpdate(newRole);
        assertNotNull(savedRole, "Retrieved saved role is null");

        List<Role> rolesAfterSave = roleService.findAll();
        assertEquals(4, rolesAfterSave.size(), "Role not saved");

        savedRole.setName("Updated name");

        Role updatedRole = roleService.saveOrUpdate(savedRole);
        assertNotNull(updatedRole, "Retrieved updated role is null");
        assertEquals("Updated name", updatedRole.getName(), "Role name not updated");

        List<Role> rolesAfterUpdate = roleService.findAll();
        assertEquals(4, rolesAfterUpdate.size(), "Role saved instead of updating");
    }

    @Test
    @DisplayName("When calling deleteAll then all roles should be deleted")
    void deleteAll() {
        List<Role> roles = roleService.findAll();
        assertFalse(roles.isEmpty(), "Roles not retrieved");

        roleService.deleteAll();

        List<Role> rolesAfterDelete = roleService.findAll();
        assertTrue(rolesAfterDelete.isEmpty(), "Roles not deleted");
    }
}