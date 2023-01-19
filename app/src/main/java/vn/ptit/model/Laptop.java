package vn.ptit.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Laptop implements Serializable {
    private String id;
    private String name;
    private String cpu;
    private String hardDrive;
    private String ram;
    private String vga;
    private double price;
    private double discount;
    private double screen;
    private String video;
    private String image;
    private Manufacturer manufacturer;
    private String createdAt;
    private boolean status;
    private float averageStar;
    private List<Comment> comments;
}
