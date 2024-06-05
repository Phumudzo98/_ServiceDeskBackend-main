package com.servicedesk.usersservice.model.company;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="Account")
public class Account implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID accountId;
    @Column(name = "username", nullable = false,unique = true)
    private String username;
    @Column(name = "password", nullable = false)
    private String password;
    @OneToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name="companyId",nullable = false)
    @JsonIgnore
    private Company company;
}
