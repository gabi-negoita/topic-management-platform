package com.mtapo.app.service;

import com.mtapo.app.entity.EventLog;
import com.mtapo.app.repository.EventLogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class EventLogService {

    private final EventLogRepository repository;

    @Autowired
    public EventLogService(EventLogRepository repository) {
        this.repository = repository;
    }

    public List<EventLog> findAll() {
        log.debug("findAll()");
        return repository.findAll();
    }

    public Page<EventLog> findAll(Pageable paging) {
        log.debug(String.format("findAll(%s)"), paging);
        return repository.findAll(paging);
    }

    public EventLog saveOrUpdate(EventLog category) {
        log.debug("saveOrUpdate(" + category + ")");
        return repository.save(category);
    }

    public void deleteAll() {
        log.debug("deleteAll()");
        repository.deleteAll();
    }

    public List<EventLog> findAllByUserId(@Param("userId") Integer userId) {
        log.debug("findAllByUserId(" + userId + ")");
        return this.repository.findAllByUserId(userId);
    }

    public Page<EventLog> findAllByOperationAndStatus(
            String operation,
            String status,
            Pageable paging) {
        log.debug(String.format("findAllByOperationAndStatus(%s, %s, %s)", operation, status, paging));
        return repository.findAllByOperationAndStatus(operation, status, paging);
    }
}
