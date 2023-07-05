package com.mentorshiptracker.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "admins")
@PrimaryKeyJoinColumn(name = "user_id")
public class Admin extends User {
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;


    public Admin(String username, String email, String password) {
        super();
        this.setUsername(username);
        this.setEmail(email);
        this.setPassword(password);
    }

}
