package br.com.curso.tasks.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.ArrayList;

@Entity
@Getter
@Setter
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String phone;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Guest> guests = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "created")
    private List<Task> tasksCreated = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<PendingGuest> pendingGuests;
}