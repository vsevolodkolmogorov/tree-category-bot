package kz.pandev.tree_category_bot.repository;

import kz.pandev.tree_category_bot.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);

    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.children WHERE c.parent IS NULL")
    List<Category> findRootsWithChildren();

    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.children WHERE c.name = :name")
    Optional<Category> findByNameWithChildren(@Param("name") String name);


}
