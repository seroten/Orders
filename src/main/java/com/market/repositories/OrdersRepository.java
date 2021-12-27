package com.market.repositories;

import com.market.entities.Order;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersRepository extends CrudRepository<Order, Long> {

    Iterable<Order> findByOrderId(Integer orderId);
    Iterable<Order> findByClientId(Integer clientId);
}
