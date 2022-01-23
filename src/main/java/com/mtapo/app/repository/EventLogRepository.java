package com.mtapo.app.repository;

import com.mtapo.app.entity.EventLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventLogRepository extends JpaRepository<EventLog, Integer> {

    @Query("select el " +
            "from EventLog el " +
            "where el.user.id = :userId")
    List<EventLog> findAllByUserId(@Param("userId") Integer userId);

    @Query("select distinct el " +
            "from EventLog el " +
            "where (:operation is null or (lower(el.operation) = lower(:operation))) " +
            "and (:status is null or lower(el.status) = lower(:status))")
    Page<EventLog> findAllByOperationAndStatus(@Param("operation") String operation,
                                               String status,
                                               Pageable pageable);
}
