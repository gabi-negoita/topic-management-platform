package com.mtapo.app.service;

import com.mtapo.app.entity.EventLog;
import com.mtapo.app.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.TestPropertySource;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
@DisplayName("Given the event log service")
class EventLogServiceTests {

    @Autowired
    UserService userService;

    @Autowired
    EventLogService eventLogService;

    @BeforeEach
    void populateDataBase() {
        log.debug("Populating database...");

        User user = new User(null,
                "John",
                "Doe",
                "john.doe@email.com",
                "pass",
                null,
                null);
        User savedUser = userService.saveOrUpdate(user);

        EventLog eventLog1 = new EventLog(null,
                EventLog.OPERATION_INSERT,
                EventLog.STATUS_FAILED,
                "Insert topic with id of 123",
                new Timestamp(System.currentTimeMillis()),
                savedUser);
        eventLogService.saveOrUpdate(eventLog1);

        EventLog eventLog2 = new EventLog(null,
                EventLog.OPERATION_UPDATE,
                EventLog.STATUS_SUCCESS,
                "Update topic with id of 234",
                new Timestamp(System.currentTimeMillis()),
                savedUser);
        eventLogService.saveOrUpdate(eventLog2);

        EventLog eventLog3 = new EventLog(null,
                EventLog.OPERATION_DELETE,
                EventLog.STATUS_SUCCESS,
                "Delete topic with id of 456",
                new Timestamp(System.currentTimeMillis()),
                savedUser);
        eventLogService.saveOrUpdate(eventLog3);
    }

    @AfterEach
    void flushDataBase() {
        log.debug("Flushing database...");

        eventLogService.deleteAll();
        userService.deleteAll();
    }

    @Test
    @DisplayName("When calling findAll then all event logs should be retrieved")
    void findAll() {
        List<EventLog> eventLogs = eventLogService.findAll();
        assertEquals(3, eventLogs.size(), "Event logs not retrieved correctly");
    }

    @Test
    @DisplayName("When calling saveOrUpdate then the event log should be saved/updated accordingly")
    void saveOrUpdate() {
        Optional<User> user = userService.findByEmail("john.doe@email.com");
        assertTrue(user.isPresent(), "User not retrieved");

        EventLog newEventLog = new EventLog(null,
                EventLog.OPERATION_DELETE,
                EventLog.STATUS_SUCCESS,
                "Delete topic with id of 456",
                new Timestamp(System.currentTimeMillis()),
                user.get());

        EventLog savedEventLog = eventLogService.saveOrUpdate(newEventLog);
        assertNotNull(savedEventLog, "Retrieved saved event log is null");

        List<EventLog> eventLogsAfterSave = eventLogService.findAll();
        assertEquals(4, eventLogsAfterSave.size(), "Event log not saved");

        savedEventLog.setStatus(EventLog.STATUS_SUCCESS);

        EventLog updatedEventLog = eventLogService.saveOrUpdate(savedEventLog);
        assertNotNull(updatedEventLog, "Retrieved updated event log is null");
        assertEquals(EventLog.STATUS_SUCCESS, updatedEventLog.getStatus(), "Event log operation status not updated");

        List<EventLog> eventLogAfterUpdate = eventLogService.findAll();
        assertEquals(4, eventLogAfterUpdate.size(), "Event log saved instead of updating");
    }

    @Test
    @DisplayName("When calling deleteAll then all event logs should be deleted")
    void deleteAll() {
        List<EventLog> eventLogs = eventLogService.findAll();
        assertFalse(eventLogs.isEmpty(), "Event logs not retrieved");

        eventLogService.deleteAll();

        List<EventLog> eventLogsAfterDelete = eventLogService.findAll();
        assertTrue(eventLogsAfterDelete.isEmpty(), "Event log not deleted");
    }

    @Test
    void findAllByUserId() {
        List<User> users = userService.findAll();
        assertEquals(1, users.size(), "Users not retrieved");

        List<EventLog> eventLogs = eventLogService.findAllByUserId(users.get(0).getId());
        assertFalse(eventLogs.isEmpty(), "Event logs not retrieved");
    }

    @Test
    @DisplayName("When calling findAllByOperationAndStatus then all event logs with the specified fields should be retrieved")
    void findAllByOperationAndStatus() {
        Pageable paging = PageRequest.of(0, 5, Sort.by("date"));

        String operation = EventLog.OPERATION_UPDATE;
        String status = EventLog.STATUS_SUCCESS;

        Page<EventLog> allLogs = eventLogService.findAllByOperationAndStatus(null, null, paging);
        assertEquals(3, allLogs.getTotalElements(), "Logs not retrieved correctly");

        Page<EventLog> allByOperation = eventLogService.findAllByOperationAndStatus(operation, null, paging);
        assertEquals(1, allByOperation.getTotalElements(), "Logs not retrieved correctly");

        Page<EventLog> allByStatus = eventLogService.findAllByOperationAndStatus(null, status, paging);
        assertEquals(2, allByStatus.getTotalElements(), "Logs not retrieved correctly");

        Page<EventLog> allByOperationAndStatus = eventLogService.findAllByOperationAndStatus(operation, status, paging);
        assertEquals(1, allByOperationAndStatus.getTotalElements(), "Logs not retrieved correctly");
    }

}