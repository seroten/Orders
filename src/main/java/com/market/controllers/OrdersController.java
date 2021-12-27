package com.market.controllers;

import com.market.entities.Order;
import com.market.repositories.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrdersController {
    @Autowired
    private OrdersRepository repository;

    @GetMapping("/get")
    public Iterable<Order> greeting(
            @RequestParam(name = "orderId", required = false) Integer orderId,
            @RequestParam(name = "clientId", required = false) Integer clientId) {
        Iterable<Order> orders = null;
        if (orderId != null) {
            orders = repository.findByOrderId(orderId);
        } else if (clientId != null) {
            orders = repository.findByClientId(clientId);
        } else orders = repository.findAll();
        return orders;
    }

    @PostMapping("/save")
    public String addOrder(@RequestParam int clientId, @RequestParam int basketId,
                           @RequestParam int orderStatus, @RequestParam int paymentType,
                           @RequestParam String deliveryAddress) {
        Order order = new Order(clientId, basketId, orderStatus, paymentType, deliveryAddress);
        repository.save(order);
        return "0";
    }
}
