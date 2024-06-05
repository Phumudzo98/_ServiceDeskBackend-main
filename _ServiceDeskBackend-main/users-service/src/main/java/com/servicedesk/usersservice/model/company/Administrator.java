package com.servicedesk.usersservice.model.company;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Blob;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Administrator")
public class Administrator implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID adminId;
    @Column(name = "fullName", nullable = false)
    private String fullName;
    @Column(name = "email", nullable = false,unique = true)
    private String email;
    @Column(name = "contactNumber", nullable = false)
    private String contactNumber;
    @Column(name = "lastName", nullable = false)
    private String lastName;
    @Column(name = "position", nullable = false)
    private String position;
    @Column(name = "password", nullable = false)
    private String password;
    @OneToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name="companyId",nullable = false)
    @JsonIgnore
    private Company company;
    @Lob
    private Blob image;
}