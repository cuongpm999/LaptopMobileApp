package vn.ptit.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order implements Serializable {
    private String id;
    private Date createdAt;
    private String status;
    private User user;
    private Shipment shipment;
    private Cart cart;
    private Payment payment;
}
