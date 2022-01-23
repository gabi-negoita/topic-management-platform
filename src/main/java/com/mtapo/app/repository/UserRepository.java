package com.mtapo.app.repository;

import com.mtapo.app.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("select u " +
            "from User u " +
            "where u.email = :email")
    Optional<User> findByEmail(String email);

    @Query("select count(u) " +
            "from User u, Role r " +
            "where r.id = :roleId " +
            "and r member of u.roles")
    Integer getCountByRoleName(@Param("roleId") Integer roleId);

    @Query("select u " +
            "from User u, Role r " +
            "where r.name = :role " +
            "and r member of u.roles")
    List<User> getUsersByRole(@Param("role") String role);

    @Query("select st.user " +
            "from StudentTopic st " +
            "where st.topic.id = :topicId " +
            "and lower(st.status) = 'approved'")
    List<User> findAllByTopicId(@Param("topicId") Integer topicId);

    @Query("select count(st.user) " +
            "from StudentTopic st " +
            "where st.topic.id = :topicId " +
            "and lower(st.status) = :status")
    Integer getCountByTopicIdAndStatus(@Param("topicId") Integer topicId,
                                       @Param("status") String status);

    @Query("select u " +
            "from User u " +
            "where (:keywords is null " +
            "or (lower(u.firstName) like lower(concat('%', :keywords, '%')) " +
            "or lower(u.lastName) like lower(concat('%', :keywords, '%')) " +
            "or lower(u.email) like lower(concat('%', :keywords, '%'))))")
    Page<User> findAllByNameOrEmail(@Param("keywords") String keywords,
                                    Pageable pageable);

    @Query("select substring(el.date, 1, 16) as date " +
            "from EventLog el " +
            "where el.user.id = :userId " +
            "group by date " +
            "order by date asc")
    List<String> getActivityDatesByUserId(@Param("userId") Integer id);

    @Query("select count(el.id), substring(el.date, 1, 16) as date " +
            "from EventLog el " +
            "where el.user.id = :userId " +
            "group by date " +
            "order by date asc")
    List<Integer> getActivityActionsByUserId(@Param("userId") Integer id);
}
