package com.macroshop.shop.Entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
@Table(name = "member")
public class User {

    @Id
    private Long id;
    private String username;
    private String password;
    private String ipAddress;

}
