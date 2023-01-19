package vn.ptit.model;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    private String id;
    private String fullName;
    private String address;
    private String email;
    private String mobile;
    private boolean sex;
    private String dateOfBirth;
    private String username;
    private String password;
    private String position;
    private String image;
    private boolean status;
}
