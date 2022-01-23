package com.mtapo.app.repository;

import com.mtapo.app.entity.Category;
import com.mtapo.app.entity.Topic;
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
public interface TopicRepository extends JpaRepository<Topic, Integer> {

    @Query("select t " +
            "from Topic t " +
            "where (t.category = :category and :category is not null) " +
            "or (:category is null)")
    List<Topic> findAllByCategory(@Param("category") Category category);

    @Query("select t " +
            "from Topic t " +
            "where t.user.id = :userId")
    List<Topic> findAllByUserId(@Param("userId") Integer userId);

    @Query("select st.topic " +
            "from StudentTopic st " +
            "where st.user = :user " +
            "and st.status = :s")
    Optional<Topic> findTopicOfUser(User user,
                                    String s);

    @Query("select distinct t " +
            "from Topic t " +
            "inner join t.tags tag " +
            "where (:categoryName is null or t.category.name = :categoryName) " +
            "and ((:tags) is null or tag.name in (:tags))")
    List<Topic> findAllByCategoryAndTags(@Param("categoryName") String categoryName,
                                         @Param("tags") List<String> tags);

    @Query("select distinct t " +
            "from Topic t " +
            "inner join t.tags tag " +
            "where (:categoryName is null or t.category.name = :categoryName) " +
            "and (:userId is null or t.user.id = :userId) " +
            "and (tag.name in (:tags))")
    List<Topic> findAllByCategoryUserAndTags(@Param("categoryName") String categoryName,
                                             @Param("userId") Integer userId,
                                             @Param("tags") List<String> tags);

    @Query("select distinct t " +
            "from Topic t " +
            "inner join t.tags tag " +
            "where (:categoryName is null or t.category.name = :categoryName) " +
            "and (:userId is null or t.user.id = :userId)")
    List<Topic> findAllByCategoryAndUser(@Param("categoryName") String categoryName,
                                         @Param("userId") Integer userId);

    @Query("select distinct t " +
            "from Topic t " +
            "where (:teacher is null " +
            "or (lower(concat(t.user.firstName, ' ', t.user.lastName)) = lower(:teacher))) " +
            "AND (:category is null or t.category.name = :category)")
    Page<Topic> findAllByTeacherAndCategory(@Param("teacher") String teacher,
                                            @Param("category") String category,
                                            Pageable pageable);

    @Query("select distinct t " +
            "from Topic t " +
            "inner join t.tags tag " +
            "where (:teacher is null " +
            "or (lower(concat(t.user.firstName, ' ', t.user.lastName)) = lower(:teacher))) " +
            "AND (:category is null or t.category.name = :category) " +
            "AND tag.name in (:tags)")
    Page<Topic> findAllByTeacherCategoryAndTags(@Param("teacher") String teacher,
                                                @Param("category") String category,
                                                @Param("tags") List<String> tags,
                                                Pageable pageable);

    @Query("select count(t) " +
            "from Topic t " +
            "where t.category.name = :categoryName")
    Integer getCountByCategoryName(@Param("categoryName") String categoryName);
}
