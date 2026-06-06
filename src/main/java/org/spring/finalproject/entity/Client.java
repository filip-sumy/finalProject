package org.spring.finalproject.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clients")
@Getter
@Setter
@NoArgsConstructor
public class Client extends User {

    private String phone;

    private String address;

    @OneToMany(mappedBy = "client")
    private List<Order> orders = new ArrayList<>();
}
