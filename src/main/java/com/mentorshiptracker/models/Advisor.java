package com.mentorshiptracker.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "advisors")
@PrimaryKeyJoinColumn(name = "user_id")
public class Advisor extends User {
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @OneToMany(mappedBy = "advisor")
    private Set<Advisee> advisees;


}
