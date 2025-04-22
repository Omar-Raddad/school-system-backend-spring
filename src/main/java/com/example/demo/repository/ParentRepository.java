package com.example.demo.repository;// ParentRepository.java
import com.example.demo.model.Parent;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ParentRepository extends JpaRepository<Parent, Long> {
    Optional<Parent> findByEmail(String email);
}