package org.example.programming5project.domain;

import jakarta.persistence.*;

// TODO: User is an indpendent entity, it doesnt have relations with other domain classes directly
// TODO: One of the domain entities must have a relationship with user (one way relationship not bi-directional)
// ad username and password to doctor, use inheritance and make user extend and doctor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @OneToOne
    private Doctor doctor; //put it to doctor class.o r one to many

    @Enumerated(EnumType.STRING)
    private UserRole userRole;


    public User() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getUserRole() {
        return userRole;
    }
    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }
    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }
}
