package com.market.controllers;

import com.market.entities.Order;
import com.market.repositories.OrdersRepository;
import com.market.tools.mailer.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrdersController {
    @Autowired
    private OrdersRepository repository;
    @Autowired
    private EmailService mailSender;

    @GetMapping("/get")
    public Iterable<Order> getOrder(
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
    public String addOrder(@RequestParam Integer clientId, @RequestParam Integer basketId,
                           @RequestParam Integer orderStatus, @RequestParam Integer paymentType,
                           @RequestParam String deliveryAddress) {
        Order order = new Order(clientId, basketId, orderStatus, paymentType, deliveryAddress);
        repository.save(order);
        if(orderStatus == 0) {
            mailSender.sendSimpleMessage("seroten@mail.ru", "Заказ в онлайн магазине", "Ваш заказ №" + basketId + " сформирован. Ожидайте звонка менеджера.");
        }
        return "0";
    }

    @PostMapping("/update")
    public String updateOrder(@RequestParam Integer orderId, @RequestParam Integer clientId,
                           @RequestParam Integer basketId, @RequestParam Integer orderStatus,
                           @RequestParam Integer paymentType, @RequestParam String deliveryAddress) {
        if (repository.findByOrderId(orderId).iterator().next().getOrderId() != null) {
            Order updatedOrder = new Order(clientId, basketId, orderStatus,
                                        paymentType, deliveryAddress);
            updatedOrder.setOrderId(orderId);
            repository.save(updatedOrder);
        }
        mailSender.sendSimpleMessage("seroten@mail.ru", "Заказ в онлайн магазине", "Ваш заказ №" + basketId + " обновлен.");
        return "0";
    }
}
