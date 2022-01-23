package com.mtapo.app.repository;

import com.mtapo.app.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {

    @Query("select t " +
            "from Tag t " +
            "where t.name = :name")
    Optional<Tag> findByName(@Param("name") String name);

    @Query("select t.tags " +
            "from Topic t " +
            "where t.user.id = :userId")
    Set<Tag> findAllByUserId(@Param("userId") Integer userId);

    @Query("select t " +
            "from Tag t " +
            "where t.name <> 'uncategorized'")
    List<Tag> findAll();

    @Query("select count(tag.id) " +
            "from Topic topic " +
            "inner join topic.tags tag " +
            "where tag.name = :name")
    Integer countUsesByName(String name);
}
