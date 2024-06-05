package com.servicedesk.usersservice.model;

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
@MappedSuperclass
public class Account implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID accountId;
    @Column(name = "fullName", nullable = false)
    private String fullName;
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    @Column(name = "contactNumber", nullable = false)
    private String contactNumber;
    @Column(name = "lastName", nullable = false)
    private String lastName;
    @Column(name = "position", nullable = false)
    private String position;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "companyId", nullable = false)
    private UUID companyId;
    @Column(name = "isExpired", nullable = false)

    private boolean isExpired;
    @Column(name = "status", nullable = false)
    private String Status;
    @Column(name = "isOnLeave", nullable = false)
    private boolean isOnLeave;
    @Column(name= "statusAgent", nullable = false)
    private String statusAgent;
    @Lob
    private Blob image;
}