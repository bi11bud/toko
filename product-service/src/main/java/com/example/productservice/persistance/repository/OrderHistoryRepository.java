package com.example.productservice.persistance.repository;

import com.example.productservice.dto.content.ReportOrderHistoryResponse;
import com.example.productservice.persistance.model.OrderHistory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderHistoryRepository extends CrudRepository<OrderHistory, String> {

    @Query(value = "SELECT new com.example.productservice.dto.content.ReportOrderHistoryResponse (" +
            "hoh.orderId, hoh.orderBy, hoh.productCode, hoh.productName, hoh.price, hoh.quantity, hoh.cdate, hoh.cby, hoh.mdate, hoh.mby, dc.name) " +
            "FROM OrderHistory hoh " +
            "JOIN Product dp ON hoh.productCode = dp.code " +
            "JOIN dp.category dc " +
            "ORDER BY hoh.cdate desc")
    List<ReportOrderHistoryResponse> getOrderHistory();

}
