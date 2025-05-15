package com.example.demo.service;

import com.example.demo.repository.ChildRepository;
import com.example.demo.repository.ParentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParentService {

    private final ChildRepository childRepository;
    private final ParentRepository parentRepository;

    @Autowired
    public ParentService(ChildRepository childRepository, ParentRepository parentRepository) {
        this.childRepository = childRepository;
        this.parentRepository = parentRepository;
    }


    public int countChildren(Integer parentId) {
        return parentRepository.getChildrenCount(parentId);
    }

}
