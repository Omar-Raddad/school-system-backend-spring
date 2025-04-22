package com.example.demo.model;

import com.example.demo.model.Parent;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "child")
@Data
public class Child {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(nullable = false)
    private String grade;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Parent parent;
}