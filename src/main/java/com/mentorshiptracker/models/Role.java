package com.mentorshiptracker.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private LocalDateTime dateCreated;

    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private LocalDateTime dateModified;

    @ManyToMany
    @JoinTable(name = "role_permissions",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<Permission> permissions;

    public Role(UUID id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
    public Role( String name, String description) {
        this.name = name;
        this.description = description;
    }
}
