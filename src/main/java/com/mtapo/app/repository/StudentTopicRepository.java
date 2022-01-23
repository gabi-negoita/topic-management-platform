package com.mtapo.app.repository;

import com.mtapo.app.entity.StudentTopic;
import com.mtapo.app.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentTopicRepository extends PagingAndSortingRepository<StudentTopic, Integer> {

    @Query("select st " +
            "from StudentTopic st " +
            "where st.user.id = :userId " +
            "order by st.date desc")
    List<StudentTopic> findAllByUserId(@Param("userId") Integer user);

    @Query("select st " +
            "from StudentTopic st " +
            "where st.user.id = :userId " +
            "and st.status = :status " +
            "and st.reason = :reason")
    List<StudentTopic> findAllByUserIdAndStatusAndReason(@Param("userId") Integer userId,
                                                         @Param("status") String status,
                                                         @Param("reason") String reason);

    @Query("select st " +
            "from StudentTopic st " +
            "where st.user.id = :userId " +
            "and lower(st.status) = 'approved'")
    Optional<StudentTopic> findApprovedByUserId(@Param("userId") Integer user);

    @Query("select st " +
            "from StudentTopic st " +
            "where lower(st.status) = 'approved' " +
            "and st.user.id = :userId")
    Optional<StudentTopic> findAssignedByUserId(@Param("userId") Integer userId);

    @Query("select st " +
            "from StudentTopic st " +
            "where st.topic.id = :topicId " +
            "and st.user.id = :userId")
    Optional<StudentTopic> findByTopicIdAndUserId(
            @Param("topicId") Integer topicId,
            @Param("userId") Integer userId);

    @Query("select st " +
            "from StudentTopic st " +
            "where st.topic.user = :user " +
            "and (((upper(st.user.lastName) like upper(concat('%', :search, '%')) and :search is not null) or (:search is null)) " +
            "or ((upper(st.user.firstName) like upper(concat('%', :search, '%')) and :search is not null) or (:search is null)) " +
            "or ((upper(st.user.email) like upper(concat('%', :search, '%')) and :search is not null) or (:search is null))) " +
            "and st.status = 'waiting'")
    List<StudentTopic> findAllRequestsForTopicOfUser(
            @Param("user") User user,
            @Param("search") String search,
            Pageable pageable);
}
