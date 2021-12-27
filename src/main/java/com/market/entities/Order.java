package com.market.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer orderId;

    private int clientId;
    private int basketId;
    private String orderDate;
    private int orderStatus;
    private int paymentType;
    private String deliveryAddress;
    /*
        comment on column orders.order_status is
    0 - created
    1 - pending payment
    2 - paid
    3 - confirm
    4 - packed
    5 - shipped
    6 - delevired
    7 - canceled
    */
    public Order(int clientId, int basketId, int orderStatus,
                 int paymentType, String deliveryAddress) {
        this.clientId = clientId;
        this.basketId = basketId;
       // this.orderDate = new SimpleDateFormat("dd.MM.yy").format(new Date());
        this.orderStatus = orderStatus;
        this.paymentType = paymentType;
        this.deliveryAddress = deliveryAddress;
    }
}
