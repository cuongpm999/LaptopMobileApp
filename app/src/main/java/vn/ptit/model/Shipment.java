package vn.ptit.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Shipment implements Serializable {
    private String id;
    private String name;
    private String address;
    private double price;
    private boolean status;
}
