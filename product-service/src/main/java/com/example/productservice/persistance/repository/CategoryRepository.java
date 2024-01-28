package com.example.productservice.persistance.repository;

import com.example.productservice.persistance.model.Category;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends CrudRepository<Category, String> {

    @Cacheable(cacheNames = { "category_getAllCategories" })
    @Query(value = "select p from Category p order by cast(p.code as int) asc")
    List<Category> getAllCategories();

    @Query(value = "select p from Category p where p.code = :categoryCode")
    Category findByCategoryCode(@Param("categoryCode") String categoryCode);

    @Query(value = "select p from Category p where p.name = :categoryName")
    Category findByCategoryName(@Param("categoryName") String categoryName);
}
