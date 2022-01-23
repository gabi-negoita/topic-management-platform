package com.mtapo.app.repository;

import com.mtapo.app.entity.Category;
import com.mtapo.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    @Query("select c " +
            "from Category c " +
            "where c.name = :name")
    Optional<Category> findByName(@Param("name") String name);

    @Query("select c " +
            "from Category c " +
            "where c = :#{#user.category}")
    Optional<Category> findByUser(@Param("user") User user);
}
