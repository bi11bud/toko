package com.example.purchaseservice.persistance.repository;

import com.example.purchaseservice.persistance.model.Order;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends CrudRepository<Order, String> {

    List<Order> findAllByOrderByCdateAsc();

    Order findByOrderId(String orderId);

    List<Order> findByProductCode(String productCode);

    @Query(value = "select p from Order p where UPPER(p.orderBy) like %:orderName%")
    List<Order> findAllByOrderByIgnoreCaseContaining(@Param("orderName") String orderName);

    @Query(value = "select p from Order p where p.productCode = :productCode and UPPER(p.orderBy) like %:orderName%")
    List<Order> findAllByProductCodeAndOrderByIgnoreCaseContaining(@Param("productCode") String productCode, @Param("orderName") String orderName);

}
