package com.mtapo.app.repository;

import com.mtapo.app.entity.TopicComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TopicCommentRepository extends JpaRepository<TopicComment, Integer> {

    @Query("select c " +
            "from TopicComment c " +
            "where c.topic.id = :topicId " +
            "order by c.date desc")
    List<TopicComment> findAllByTopicId(@Param("topicId") Integer topicId);

    @Query("select c " +
            "from TopicComment c " +
            "where c.user.id = :userId " +
            "order by c.date desc")
    List<TopicComment> findAllByUserId(@Param("userId") Integer userId);
}
