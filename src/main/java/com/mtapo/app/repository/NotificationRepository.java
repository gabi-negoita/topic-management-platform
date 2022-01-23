package com.mtapo.app.repository;

import com.mtapo.app.entity.Notification;
import com.mtapo.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    @Query("select n " +
            "from Notification n " +
            "where n.type = 'student-topic-request' " +
            "and n.studentTopic.topic.user = :user " +
            "and n.description not in ('approved', 'declined') " +
            "order by n.id desc")
    List<Notification> findRequestsFromStudentToTeacher(@Param("user") User user);

    @Query("select n " +
            "from Notification n " +
            "where n.studentTopic.user = :user " +
            "and n.description not in ('waiting', 'canceled') " +
            "order by n.id desc")
    List<Notification> findRequestsFromTeacherToStudent(@Param("user") User user);

    @Query("select n " +
            "from Notification n " +
            "where n.type = 'student-application-cancel' " +
            "and n.studentTopic.topic.user = :user " +
            "and n.description = 'canceled' " +
            "order by n.id desc")
    List<Notification> findApplicationCancelFromStudentToTeacher(@Param("user") User user);

    @Query("select n " +
            "from Notification n " +
            "where n.comment.topic.user = :user " +
            "and n.comment.user <> :user " +
            "order by n.id desc")
    List<Notification> findCommentsForTopicsOfTeacher(@Param("user") User user);

    @Query("select n " +
            "from Notification n " +
            "where n.comment.topic in (select st.topic from StudentTopic st where st.user = :user and st.status in ('waiting', 'approved')) " +
            "and n.comment.user <> :user " +
            "order by n.id desc")
    List<Notification> findCommentsForTopicOfStudent(@Param("user") User user);
}
