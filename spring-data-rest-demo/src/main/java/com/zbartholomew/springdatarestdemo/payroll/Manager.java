package com.zbartholomew.springdatarestdemo.payroll;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Model where security controls are instituted
 */
@Data
@ToString(exclude = "password") // ensures that the Lombok-generated toString() method will NOT print out the password
@Entity
public class Manager {

    // Encrypt new passwords or to take password inputs and encrypt them before comparison
    public static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    /*-------------- parameters needed to restrict access ---------------- */
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    // Protects from Jackson serializing this field
    @JsonIgnore
    private String password;

    private String[] roles;

    /*-------------- parameters needed to restrict access ---------------- */

    /**
     * Ensures that passwords are never stored in the clear.
     */
    public void setPassword(String password) {
        this.password = PASSWORD_ENCODER.encode(password);
    }

    protected Manager() {
    }

    public Manager(String userName, String password, String... roles) {
        this.name = userName;
        this.setPassword(password);
        this.roles = roles;
    }


}
