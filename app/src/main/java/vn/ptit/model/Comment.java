package vn.ptit.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment implements Serializable {
    private String id;
    private Date createdAt;
    private int star;
    private User user;
    private int laptopId;
    private String content;
}
