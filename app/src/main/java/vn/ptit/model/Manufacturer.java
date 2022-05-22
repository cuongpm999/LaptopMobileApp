package vn.ptit.model;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Manufacturer implements Serializable {
    private String id;
    private String name;
    private String address;
    private String createdAt;
    private boolean status;
}
