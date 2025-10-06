package com.ocosur.ocosystem.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ocosur.ocosystem.model.Branch;
import com.ocosur.ocosystem.repository.BranchRepository;

@Service
public class BranchService  {
    @Autowired
    private BranchRepository branchRepository;

    public List<Branch> getBranches(){
        return branchRepository.findAll();
    }
}
