package com.market.controllers;

import com.market.entities.Order;
import com.market.repositories.OrdersRepository;
import com.market.tools.mailer.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
        if (orderStatus == 0) {
            mailSender.sendSimpleMessage("seroten@mail.ru", "Заказ в онлайн магазине", "Ваш заказ №" + basketId + " сформирован. Ожидайте звонка менеджера.");
        }
        return "0";
    }

    @PostMapping("/update")
    public String updateOrder(@RequestParam Integer orderId,
                              @RequestParam(name = "orderStatus", required = false) Integer orderStatus,
                              @RequestParam(name = "deliveryAddress", required = false) String deliveryAddress) {
        List<Order> list = ((List<Order>) repository.findByOrderId(orderId));
        if (list.size() == 1) {
            Order order = list.get(0);
            if (orderStatus != null)
                order.setOrderStatus(orderStatus);
            if (deliveryAddress != null)
                order.setDeliveryAddress(deliveryAddress);
            repository.save(order);
            mailSender.sendSimpleMessage("seroten@mail.ru", "Заказ в онлайн магазине", "Ваш заказ №" + order.getOrderId() + " " + orderStatusString(order.getOrderStatus()));
            return "0";
        }
        return "1";
    }

    private String orderStatusString(Integer orderStatus) {
        switch (orderStatus) {
            case (0):
                return "создан";
            case (1):
                return "ожидает оплаты";
            case (2):
                return "оплачен";
            case (3):
                return "подтвержден";
            case (4):
                return "собран";
            case (5):
                return "отгружен";
            case (6):
                return "доставлен";
            case (7):
                return "отменен";
            default:
                return "обновлен";
        }
    }
}
