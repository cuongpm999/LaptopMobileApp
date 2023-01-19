package vn.ptit.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cart implements Serializable {
    private String id;
    private String createdAt;
    private User user;
    private double totalAmount;
    private List<LineItem> lineItems;
}
