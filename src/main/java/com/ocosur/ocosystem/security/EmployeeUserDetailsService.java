package com.ocosur.ocosystem.security;

import com.ocosur.ocosystem.core.employee.model.Employee;
import com.ocosur.ocosystem.core.employee.repository.EmployeeRepository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class EmployeeUserDetailsService implements UserDetailsService {

    private final EmployeeRepository repo;

    public EmployeeUserDetailsService(EmployeeRepository repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employee emp = repo.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("No user: " + username));
        return new EmployeeDetails(emp);
    }
}
