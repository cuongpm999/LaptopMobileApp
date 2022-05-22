package vn.ptit.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LineItem implements Serializable {
    private String id;
    private int quantity;
    private Laptop laptop;
}
