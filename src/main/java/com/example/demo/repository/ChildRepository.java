package com.example.demo.repository;
import com.example.demo.model.Child;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChildRepository extends JpaRepository<Child, Long> {
    long countByParentId(Integer parentId);

}