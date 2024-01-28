package com.example.productservice.persistance.repository;

import com.example.productservice.persistance.model.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends CrudRepository<Product, String> {

    @Query(value = "select p from Product p order by p.name asc")
    List<Product> getAllProducts();

    @Query(value = "select p from Product p left join p.status s where s.code = 'active' order by p.name asc")
    List<Product> getAllActiveProducts();

    @Query(value = "select p from Product p where p.code = :productCode")
    Product findByProductCode(@Param("productCode") String productCode);

    @Query(value = "select p from Product p where UPPER(p.name) = :productName")
    Product findByProductName(@Param("productName") String productName);
}
