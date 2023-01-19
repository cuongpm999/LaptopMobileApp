package vn.ptit.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment implements Serializable {
    private String id;
    private double totalMoney;
    private String type_;
}
