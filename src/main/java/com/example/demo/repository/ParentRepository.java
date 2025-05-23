package com.example.demo.repository;// ParentRepository.java
import com.example.demo.model.Parent;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;

public interface ParentRepository extends JpaRepository<Parent, Long> {
    boolean existsByEmail(String email); // Fixed method name
    Optional<Parent> findByEmail(String email);

    Optional<Parent> findByResetCode(String resetCode); // <-- NEW METHOD
    @Query("SELECT p.childrenCount FROM Parent p WHERE p.id = :parentId")
    Integer getChildrenCount(Integer parentId);
}
